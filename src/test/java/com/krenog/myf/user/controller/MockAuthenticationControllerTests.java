package com.krenog.myf.user.controller;

import com.krenog.myf.exceptions.NotFoundException;
import com.krenog.myf.user.controllers.AuthenticationController;
import com.krenog.myf.user.controllers.exceptions.UserExceptionHandler;
import com.krenog.myf.user.dto.authentication.SignInRequestDto;
import com.krenog.myf.user.dto.authentication.SignUpRequestDto;
import com.krenog.myf.user.services.authentication.AuthenticationServiceImpl;
import com.krenog.myf.user.services.authentication.exceptions.CodeDoesNotExistException;
import com.krenog.myf.user.services.authentication.exceptions.InvalidVerificationCodeException;
import com.krenog.myf.user.services.authentication.exceptions.NumberCodeAttemptsException;
import com.krenog.myf.user.services.authentication.exceptions.UserAlreadyExistException;
import com.krenog.myf.user.services.sms.exceptions.SendSmsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.krenog.myf.user.UserTestUtils.*;
import static com.krenog.myf.utils.TestConverter.checkTestErrorCode;
import static com.krenog.myf.utils.TestConverter.mapToJson;
import static com.krenog.myf.utils.TestUtils.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@Tag("CommonTest")
public class MockAuthenticationControllerTests {
    private MockMvc mockMvc;
    @Mock
    private AuthenticationServiceImpl authenticationService;
    @InjectMocks
    AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(authenticationController)
                .setControllerAdvice(new UserExceptionHandler())
                .build();
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
    public void sendSms_thenSendSmsException()
            throws Exception {
        doThrow(SendSmsException.class).when(authenticationService).sendSmsCode(anyString());

        MvcResult result = mockMvc.perform(post("/api/v1/auth/send-sms-code/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(SEND_SMS_DTO)))
                .andExpect(status().isBadGateway()).andReturn();
        checkTestErrorCode(result, "sms_traffic_unavailable");
    }

    @Test
    void signInTest_thenReturnOK() throws Exception {
        //define data
        Mockito.when(authenticationService.signIn(any(SignInRequestDto.class)))
                .thenReturn(AUTHENTICATION_DATA);

        mockMvc.perform(post("/api/v1/auth/sign-in/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(SIGN_IN_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id", is(TEST_ID.intValue())))
                .andExpect(jsonPath("$.user.phoneNumber", is(TEST_PHONE_NUMBER)))
                .andExpect(jsonPath("$.user.username", is(TEST_USERNAME)))
                .andExpect(jsonPath("$.token", is(TEST_TOKEN)));
    }

    @Test
    public void signInTest_thenNotFoundException()
            throws Exception {
        Mockito.when(authenticationService.signIn(any(SignInRequestDto.class)))
                .thenThrow(NotFoundException.class);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/sign-in/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(SIGN_IN_REQUEST_DTO)))
                .andExpect(status().isBadRequest()).andReturn();
        checkTestErrorCode(result, "user_not_found");
    }

    @Test
    public void signInTest_thenCodeDoesNotExistException()
            throws Exception {
        Mockito.when(authenticationService.signIn(any(SignInRequestDto.class)))
                .thenThrow(CodeDoesNotExistException.class);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/sign-in/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(SIGN_IN_REQUEST_DTO)))
                .andExpect(status().isInternalServerError()).andReturn();
        checkTestErrorCode(result, "code_does_not_exist");
    }


    @Test
    public void signInTest_thenInvalidVerificationCodeException()
            throws Exception {
        Mockito.when(authenticationService.signIn(any(SignInRequestDto.class)))
                .thenThrow(InvalidVerificationCodeException.class);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/sign-in/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(SIGN_IN_REQUEST_DTO)))
                .andExpect(status().isUnauthorized()).andReturn();
        checkTestErrorCode(result, "invalid_verification_code");
    }


    @Test
    public void signInTest_thenNumberCodeAttemptsException()
            throws Exception {
        Mockito.when(authenticationService.signIn(any(SignInRequestDto.class)))
                .thenThrow(NumberCodeAttemptsException.class);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/sign-in/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(SIGN_IN_REQUEST_DTO)))
                .andExpect(status().isTooManyRequests()).andReturn();
        checkTestErrorCode(result, "number_code_attempts");
    }

    @Test
    void signUpTest_thenReturnOK() throws Exception {
        //define data
        Mockito.when(authenticationService.signUp(any(SignUpRequestDto.class)))
                .thenReturn(AUTHENTICATION_DATA);

        mockMvc.perform(post("/api/v1/auth/sign-up/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(SIGN_UP_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id", is(TEST_ID.intValue())))
                .andExpect(jsonPath("$.user.phoneNumber", is(TEST_PHONE_NUMBER)))
                .andExpect(jsonPath("$.user.username", is(TEST_USERNAME)))
                .andExpect(jsonPath("$.token", is(TEST_TOKEN)));
    }

    @Test
    public void signUpTest_thenUserAlreadyExistException()
            throws Exception {
        Mockito.when(authenticationService.signUp(any(SignUpRequestDto.class)))
                .thenThrow(UserAlreadyExistException.class);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/sign-up/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(SIGN_UP_REQUEST_DTO)))
                .andExpect(status().isBadRequest()).andReturn();
        checkTestErrorCode(result, "user_already_exist");
    }

    @Test
    public void signUpTest_thenCodeDoesNotExistException()
            throws Exception {
        Mockito.when(authenticationService.signUp(any(SignUpRequestDto.class)))
                .thenThrow(CodeDoesNotExistException.class);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/sign-up/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(SIGN_UP_REQUEST_DTO)))
                .andExpect(status().isInternalServerError()).andReturn();
        checkTestErrorCode(result, "code_does_not_exist");
    }


    @Test
    public void signUpTest_thenInvalidVerificationCodeException()
            throws Exception {
        Mockito.when(authenticationService.signUp(any(SignUpRequestDto.class)))
                .thenThrow(InvalidVerificationCodeException.class);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/sign-up/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(SIGN_UP_REQUEST_DTO)))
                .andExpect(status().isUnauthorized()).andReturn();
        checkTestErrorCode(result, "invalid_verification_code");
    }


    @Test
    public void signUpTest_thenNumberCodeAttemptsException()
            throws Exception {
        Mockito.when(authenticationService.signUp(any(SignUpRequestDto.class)))
                .thenThrow(NumberCodeAttemptsException.class);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/sign-up/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(SIGN_UP_REQUEST_DTO)))
                .andExpect(status().isTooManyRequests()).andReturn();
        checkTestErrorCode(result, "number_code_attempts");
    }
}
