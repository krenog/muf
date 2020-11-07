package com.krenog.myf.user.controller;

import com.krenog.myf.user.dto.authentication.SendSmsDto;
import com.krenog.myf.user.dto.authentication.SignInRequestDto;
import com.krenog.myf.user.dto.authentication.SignUpRequestDto;
import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.repositories.UserRepository;
import com.krenog.myf.user.services.authentication.AuthenticationData;
import com.krenog.myf.user.services.cache.CacheService;
import com.krenog.myf.utils.AbstractControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Optional;

import static com.krenog.myf.user.UserTestUtils.*;
import static com.krenog.myf.utils.TestConverter.mapToJson;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTests extends AbstractControllerTest {

    private static final SendSmsDto sendSmsDto;
    private static final SignInRequestDto signInRequestDto;
    private static final SignUpRequestDto signUpRequestDto;
    private static final AuthenticationData authenticationData;
    @Autowired
    CacheService cacheService;
    @Autowired
    private UserRepository userRepository;

    static {
        sendSmsDto = new SendSmsDto(TEST_PHONE_NUMBER);

        signInRequestDto = new SignInRequestDto(TEST_PHONE_NUMBER, TEST_CODE);

        signUpRequestDto = new SignUpRequestDto(TEST_PHONE_NUMBER, TEST_CODE, TEST_USERNAME);

        authenticationData = new AuthenticationData();
        authenticationData.setId(TEST_ID);
        authenticationData.setPhoneNumber(TEST_PHONE_NUMBER);
        authenticationData.setToken(TEST_TOKEN);
        authenticationData.setUsername(TEST_USERNAME);
    }

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        Optional<User> user = userRepository.getByPhoneNumber(TEST_PHONE_NUMBER);
        user.ifPresent(value -> userRepository.delete(value));
    }

    private User saveUser() {
        User user = new User();
        user.setPhoneNumber(TEST_PHONE_NUMBER);
        user.setUsername(TEST_USERNAME);
        return userRepository.save(user);
    }

    @Test
    public void sendSms_thenReturnOK()
            throws Exception {
        mockMvc.perform(post("/api/v1/auth/send-sms-code/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(sendSmsDto)))
                .andExpect(status().isOk());
    }

    @Test
    void signInTest_thenReturnOK() throws Exception {
        User user = saveUser();
        cacheService.setValue(TEST_PHONE_NUMBER, TEST_CODE);
        cacheService.setValue(TEST_CODE_TRY_KEY, TEST_START_COUNT_TRY_VALUE);
        mockMvc.perform(post("/api/v1/auth/sign-in/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(signInRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.user.phoneNumber", is(user.getPhoneNumber())))
                .andExpect(jsonPath("$.user.username", is(user.getUsername())))
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    @Test
    void signUpTest_thenReturnOK() throws Exception {
        //define data
        cacheService.setValue(TEST_PHONE_NUMBER, TEST_CODE);
        cacheService.setValue(TEST_CODE_TRY_KEY, TEST_START_COUNT_TRY_VALUE);

        mockMvc.perform(post("/api/v1/auth/sign-up/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(signUpRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id", notNullValue()))
                .andExpect(jsonPath("$.user.phoneNumber", is(TEST_PHONE_NUMBER)))
                .andExpect(jsonPath("$.user.username", is(TEST_USERNAME)))
                .andExpect(jsonPath("$.token", notNullValue()));
    }
}
