package com.krenog.myf.user.controller;

import com.krenog.myf.user.dto.user.CheckPhoneNumberRequestDto;
import com.krenog.myf.user.dto.user.CheckUsernameRequestDto;
import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.repositories.UserRepository;
import com.krenog.myf.utils.AbstractControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Optional;

import static com.krenog.myf.user.UserTestUtils.TEST_PHONE_NUMBER;
import static com.krenog.myf.user.UserTestUtils.TEST_USERNAME;
import static com.krenog.myf.utils.TestConverter.mapToJson;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTests extends AbstractControllerTest {
    @Autowired
    private UserRepository userRepository;
    private static final CheckPhoneNumberRequestDto checkPhoneNumberRequestDto;
    private static final CheckUsernameRequestDto checkUsernameRequestDto;

    static {
        checkPhoneNumberRequestDto = new CheckPhoneNumberRequestDto();
        checkPhoneNumberRequestDto.setPhoneNumber(TEST_PHONE_NUMBER);
        checkUsernameRequestDto = new CheckUsernameRequestDto();
        checkUsernameRequestDto.setUsername(TEST_USERNAME);
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
    public void checkPhoneNumber_thenTrueReturnOK()
            throws Exception {
        saveUser();
        mockMvc.perform(post("/api/v1/user/check-phone/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(checkPhoneNumberRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exist", is(true)));
    }

    @Test
    public void checkPhoneNumber_thenFalseReturnOK()
            throws Exception {
        mockMvc.perform(post("/api/v1/user/check-phone/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(checkPhoneNumberRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exist", is(false)));
    }

    @Test
    public void checkUsername_thenTrueReturnOK()
            throws Exception {
        saveUser();
        mockMvc.perform(post("/api/v1/user/check-username/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(checkUsernameRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exist", is(true)));
    }

    @Test
    public void checkUsername_thenFalseReturnOK()
            throws Exception {
        mockMvc.perform(post("/api/v1/user/check-username/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(checkUsernameRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exist", is(false)));
    }
}
