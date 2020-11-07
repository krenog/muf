package com.krenog.myf.user.controller;

import com.krenog.myf.exceptions.NotFoundException;
import com.krenog.myf.user.controllers.AuthenticationController;
import com.krenog.myf.user.controllers.exceptions.UserExceptionHandler;
import com.krenog.myf.user.dto.authentication.SendSmsDto;
import com.krenog.myf.user.dto.authentication.SignInRequestDto;
import com.krenog.myf.user.dto.authentication.SignUpRequestDto;
import com.krenog.myf.user.services.authentication.AuthenticationData;
import com.krenog.myf.user.services.authentication.AuthenticationServiceImpl;
import com.krenog.myf.user.services.authentication.exceptions.CodeDoesNotExistException;
import com.krenog.myf.user.services.authentication.exceptions.InvalidVerificationCodeException;
import com.krenog.myf.user.services.authentication.exceptions.NumberCodeAttemptsException;
import com.krenog.myf.user.services.authentication.exceptions.UserAlreadyExistException;
import com.krenog.myf.user.services.sms.exceptions.SendSmsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.krenog.myf.user.UserTestUtils.*;
import static com.krenog.myf.utils.TestConverter.checkTestErrorCode;
import static com.krenog.myf.utils.TestConverter.mapToJson;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class MockAuthenticationControllerTests {
    private MockMvc mockMvc;
    @Mock
    private AuthenticationServiceImpl authenticationService;
    @InjectMocks
    AuthenticationController authenticationController;
    private static final SendSmsDto sendSmsDto;
    private static final SignInRequestDto signInRequestDto;
    private static final SignUpRequestDto signUpRequestDto;
    private static final AuthenticationData authenticationData;

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
                .content(mapToJson(sendSmsDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void sendSms_thenSendSmsException()
            throws Exception {
        doThrow(SendSmsException.class).when(authenticationService).sendSmsCode(anyString());

        MvcResult result = mockMvc.perform(post("/api/v1/auth/send-sms-code/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(sendSmsDto)))
                .andExpect(status().isBadGateway()).andReturn();
        checkTestErrorCode(result, "sms_traffic_unavailable");
    }

    @Test
    void signInTest_thenReturnOK() throws Exception {
        //define data
        Mockito.when(authenticationService.signIn(any(SignInRequestDto.class)))
                .thenReturn(authenticationData);

        mockMvc.perform(post("/api/v1/auth/sign-in/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(signInRequestDto)))
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
                .content(mapToJson(signInRequestDto)))
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
                .content(mapToJson(signInRequestDto)))
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
                .content(mapToJson(signInRequestDto)))
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
                .content(mapToJson(signInRequestDto)))
                .andExpect(status().isTooManyRequests()).andReturn();
        checkTestErrorCode(result, "number_code_attempts");
    }

    @Test
    void signUpTest_thenReturnOK() throws Exception {
        //define data
        Mockito.when(authenticationService.signUp(any(SignUpRequestDto.class)))
                .thenReturn(authenticationData);

        mockMvc.perform(post("/api/v1/auth/sign-up/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(signUpRequestDto)))
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
                .content(mapToJson(signUpRequestDto)))
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
                .content(mapToJson(signUpRequestDto)))
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
                .content(mapToJson(signUpRequestDto)))
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
                .content(mapToJson(signUpRequestDto)))
                .andExpect(status().isTooManyRequests()).andReturn();
        checkTestErrorCode(result, "number_code_attempts");
    }
}
