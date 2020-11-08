package com.krenog.myf.user.controller;

import com.krenog.myf.user.controllers.UserController;
import com.krenog.myf.user.controllers.exceptions.UserExceptionHandler;
import com.krenog.myf.user.services.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.krenog.myf.user.UserTestUtils.CHECK_PHONE_NUMBER_REQUEST_DTO;
import static com.krenog.myf.user.UserTestUtils.CHECK_USERNAME_REQUEST_DTO;
import static com.krenog.myf.utils.TestConverter.mapToJson;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class MockUserControllerTests {
    private MockMvc mockMvc;
    @Mock
    private UserServiceImpl userService;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new UserExceptionHandler())
                .build();
    }

    @Test
    public void checkPhoneNumber_thenTrueReturnOK()
            throws Exception {
        Mockito.when(userService.checkPhoneNumberIsExist(anyString()))
                .thenReturn(true);
        mockMvc.perform(post("/api/v1/user/check-phone/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(CHECK_PHONE_NUMBER_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exist", is(true)));
    }

    @Test
    public void checkPhoneNumber_thenFalseReturnOK()
            throws Exception {
        Mockito.when(userService.checkPhoneNumberIsExist(anyString()))
                .thenReturn(false);
        mockMvc.perform(post("/api/v1/user/check-phone/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(CHECK_PHONE_NUMBER_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exist", is(false)));
    }

    @Test
    public void checkUsername_thenTrueReturnOK()
            throws Exception {
        Mockito.when(userService.checkUsernameIsExist(anyString()))
                .thenReturn(true);
        mockMvc.perform(post("/api/v1/user/check-username/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(CHECK_USERNAME_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exist", is(true)));
    }

    @Test
    public void checkUsername_thenFalseReturnOK()
            throws Exception {
        Mockito.when(userService.checkUsernameIsExist(anyString()))
                .thenReturn(false);
        mockMvc.perform(post("/api/v1/user/check-username/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(CHECK_USERNAME_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exist", is(false)));
    }
}
