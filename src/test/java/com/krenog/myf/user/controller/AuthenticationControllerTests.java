package com.krenog.myf.user.controller;

import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.repositories.UserRepository;
import com.krenog.myf.user.services.cache.CacheService;
import com.krenog.myf.utils.AbstractControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Optional;

import static com.krenog.myf.user.UserTestUtils.*;
import static com.krenog.myf.utils.TestConverter.mapToJson;
import static com.krenog.myf.utils.TestUtils.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("RealTest")
public class AuthenticationControllerTests extends AbstractControllerTest {
    @Autowired
    CacheService cacheService;
    @Autowired
    private UserRepository userRepository;


    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        Optional<User> user = userRepository.getByPhoneNumber(TEST_PHONE_NUMBER);
        user.ifPresent(value -> userRepository.delete(value));
    }

    private User saveUser() {
        User user = getTestUser();
        return userRepository.save(user);
    }

    @Test
    public void sendSms_thenReturnOK()
            throws Exception {
        mockMvc.perform(post("/api/v1/auth/send-sms-code/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(SEND_SMS_DTO)))
                .andExpect(status().isOk());
    }

    @Test
    void signInTest_thenReturnOK() throws Exception {
        User user = saveUser();
        cacheService.setValue(TEST_PHONE_NUMBER, TEST_CODE);
        cacheService.setValue(TEST_CODE_TRY_KEY, TEST_START_COUNT_TRY_VALUE);
        mockMvc.perform(post("/api/v1/auth/sign-in/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(SIGN_IN_REQUEST_DTO)))
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
                .content(mapToJson(SIGN_UP_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id", notNullValue()))
                .andExpect(jsonPath("$.user.phoneNumber", is(TEST_PHONE_NUMBER)))
                .andExpect(jsonPath("$.user.username", is(TEST_USERNAME)))
                .andExpect(jsonPath("$.token", notNullValue()));
    }
}
