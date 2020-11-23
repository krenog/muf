package com.krenog.myf.user.controller;

import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.repositories.UserRepository;
import com.krenog.myf.utils.AbstractControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Optional;

import static com.krenog.myf.user.UserTestUtils.CHECK_PHONE_NUMBER_REQUEST_DTO;
import static com.krenog.myf.user.UserTestUtils.CHECK_USERNAME_REQUEST_DTO;
import static com.krenog.myf.utils.TestConverter.mapToJson;
import static com.krenog.myf.utils.TestUtils.TEST_PHONE_NUMBER;
import static com.krenog.myf.utils.TestUtils.getTestUser;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("RealTest")
public class UserControllerTests extends AbstractControllerTest {
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
    public void checkPhoneNumber_thenTrueReturnOK()
            throws Exception {
        saveUser();
        mockMvc.perform(post("/api/v1/user/check-phone/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(CHECK_PHONE_NUMBER_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exist", is(true)));
    }

    @Test
    public void checkPhoneNumber_thenFalseReturnOK()
            throws Exception {
        mockMvc.perform(post("/api/v1/user/check-phone/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(CHECK_PHONE_NUMBER_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exist", is(false)));
    }

    @Test
    public void checkUsername_thenTrueReturnOK()
            throws Exception {
        saveUser();
        mockMvc.perform(post("/api/v1/user/check-username/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(CHECK_USERNAME_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exist", is(true)));
    }

    @Test
    public void checkUsername_thenFalseReturnOK()
            throws Exception {
        mockMvc.perform(post("/api/v1/user/check-username/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(CHECK_USERNAME_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exist", is(false)));
    }
}
