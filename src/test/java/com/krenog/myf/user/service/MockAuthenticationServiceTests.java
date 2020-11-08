package com.krenog.myf.user.service;

import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.security.detail.UserPrincipal;
import com.krenog.myf.user.security.jwt.JwtProvider;
import com.krenog.myf.user.services.authentication.AuthenticationData;
import com.krenog.myf.user.services.authentication.AuthenticationServiceImpl;
import com.krenog.myf.user.services.authentication.config.AuthenticationConfig;
import com.krenog.myf.user.services.authentication.exceptions.CodeDoesNotExistException;
import com.krenog.myf.user.services.authentication.exceptions.InvalidVerificationCodeException;
import com.krenog.myf.user.services.authentication.exceptions.NumberCodeAttemptsException;
import com.krenog.myf.user.services.cache.CacheService;
import com.krenog.myf.user.services.sms.SmsService;
import com.krenog.myf.user.services.user.CreateUserData;
import com.krenog.myf.user.services.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static com.krenog.myf.user.UserTestUtils.*;
import static com.krenog.myf.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
public class MockAuthenticationServiceTests {

    @InjectMocks
    AuthenticationServiceImpl authenticationService;
    @Mock
    private UserService userService;
    @Mock
    private CacheService cacheService;
    @Mock
    private SmsService smsService;
    @Mock
    private AuthenticationConfig config;
    @Mock
    private JwtProvider jwtProvider;

    @Test
    void sendSmsCodeTrafficIsDisabledTestTest() {
        //prepare data
        Mockito.when(config.isTrafficEnabled())
                .thenReturn(false);
        Mockito.when(config.getTestCode())
                .thenReturn(TEST_CODE);
        //call function
        authenticationService.sendSmsCode(TEST_PHONE_NUMBER);
        // check
        Mockito.verify(smsService, Mockito.times(0)).sendSms(anyString(), anyString());
        Mockito.verify(cacheService, Mockito.times(1)).setValue(TEST_PHONE_NUMBER, TEST_CODE);
        Mockito.verify(cacheService, Mockito.times(1)).setValue(TEST_CODE_TRY_KEY, TEST_START_COUNT_TRY_VALUE);
    }

    @Test
    void sendSmsTrafficIsEnabledWithTestPhoneTest() {
        //prepare data
        Mockito.when(config.isTrafficEnabled())
                .thenReturn(true);
        Mockito.when(config.getTestCode())
                .thenReturn(TEST_CODE);
        Mockito.when(config.getPhone())
                .thenReturn(TEST_PHONE_NUMBER);
        //call function
        authenticationService.sendSmsCode(TEST_PHONE_NUMBER);
        // check
        Mockito.verify(smsService, Mockito.times(0)).sendSms(anyString(), anyString());
        Mockito.verify(cacheService, Mockito.times(1)).setValue(TEST_PHONE_NUMBER, TEST_CODE);
        Mockito.verify(cacheService, Mockito.times(1)).setValue(TEST_CODE_TRY_KEY, TEST_START_COUNT_TRY_VALUE);
    }

    @Test
    void sendSmsTrafficIsEnabledTest() {
        //prepare data
        Mockito.when(config.isTrafficEnabled())
                .thenReturn(true);
        Mockito.when(config.getTestCode())
                .thenReturn(TEST_CODE);
        Mockito.when(config.getPhone())
                .thenReturn("12345678");
        //call function
        authenticationService.sendSmsCode(TEST_PHONE_NUMBER);
        // check
        Mockito.verify(smsService, Mockito.times(1)).sendSms(anyString(), any());
        Mockito.verify(cacheService, Mockito.times(1)).setValue(eq(TEST_PHONE_NUMBER), anyString());
        Mockito.verify(cacheService, Mockito.times(1)).setValue(TEST_CODE_TRY_KEY, TEST_START_COUNT_TRY_VALUE);
    }

    @Test
    void signInCodeDoesNotExistTest() {
        //prepare data
        Mockito.when(cacheService.getValue(anyString()))
                .thenReturn(null);
        //check
        Throwable exception = assertThrows(CodeDoesNotExistException.class, () -> authenticationService.signIn(SIGN_IN_REQUEST_DTO));
        Assertions.assertEquals(exception.getMessage(), "Code does not exist");
        Mockito.verify(cacheService, Mockito.times(1)).getValue(TEST_PHONE_NUMBER);
    }

    @Test
    void signInInvalidVerificationCodeTest() {
        //prepare data
        Mockito.when(cacheService.getValue(anyString()))
                .thenReturn("123321");
        Mockito.when(cacheService.getValue(TEST_CODE_TRY_KEY))
                .thenReturn("0");
        //check
        Throwable exception = assertThrows(InvalidVerificationCodeException.class, () -> authenticationService.signIn(SIGN_IN_REQUEST_DTO));
        Assertions.assertEquals(exception.getMessage(), "Invalid Verification Code");
        Mockito.verify(cacheService, Mockito.times(2)).getValue(anyString());
    }

    @Test
    void signInMaxCountTryTest() {
        //prepare data
        Mockito.when(cacheService.getValue(TEST_PHONE_NUMBER))
                .thenReturn(TEST_CODE);
        Mockito.when(cacheService.getValue(TEST_CODE_TRY_KEY))
                .thenReturn("10");
        //check
        Throwable exception = assertThrows(NumberCodeAttemptsException.class, () -> authenticationService.signIn(SIGN_IN_REQUEST_DTO));
        Assertions.assertEquals(exception.getMessage(), "Number code attempts");
        Mockito.verify(cacheService, Mockito.times(2)).getValue(anyString());
    }

    @Test
    void signInCountTryNullTest() {
        //prepare data
        Mockito.when(cacheService.getValue(TEST_PHONE_NUMBER))
                .thenReturn(TEST_CODE);
        Mockito.when(cacheService.getValue(TEST_CODE_TRY_KEY))
                .thenReturn(null);
        //check
        Throwable exception = assertThrows(NumberCodeAttemptsException.class, () -> authenticationService.signIn(SIGN_IN_REQUEST_DTO));
        Assertions.assertEquals("Checking sms code attempts error", exception.getMessage());
        Mockito.verify(cacheService, Mockito.times(2)).getValue(anyString());
    }


    @Test
    void signInTest() {
        String session = "test";
        User user = getTestUserWithId();

        //prepare data
        Mockito.when(cacheService.getValue(TEST_PHONE_NUMBER))
                .thenReturn(TEST_CODE);
        Mockito.when(cacheService.getValue(TEST_CODE_TRY_KEY))
                .thenReturn("0");
        Mockito.when(userService.getUserByPhoneNumber(TEST_PHONE_NUMBER))
                .thenReturn(user);
        Mockito.when(jwtProvider.generateJwtToken(any(UserPrincipal.class)))
                .thenReturn(session);
        //check
        AuthenticationData authenticationData = authenticationService.signIn(SIGN_IN_REQUEST_DTO);

        Mockito.verify(cacheService, Mockito.times(2)).getValue(anyString());
        Mockito.verify(cacheService, Mockito.times(1)).incrementValue(anyString());
        Mockito.verify(cacheService, Mockito.times(2)).deleteValue(anyString());
        Assertions.assertEquals(TEST_ID, authenticationData.getId());
        Assertions.assertNotNull(user.getLastLogin());
        Assertions.assertEquals(session, authenticationData.getToken());
    }


    @Test
    void signUpCodeDoesNotExistTest() {
        //prepare data
        Mockito.when(cacheService.getValue(anyString()))
                .thenReturn(null);
        //check
        Throwable exception = assertThrows(CodeDoesNotExistException.class, () -> authenticationService.signUp(SIGN_UP_REQUEST_DTO));
        Assertions.assertEquals(exception.getMessage(), "Code does not exist");
        Mockito.verify(cacheService, Mockito.times(1)).getValue(TEST_PHONE_NUMBER);
    }

    @Test
    void signUpInvalidVerificationCodeTest() {
        //prepare data
        Mockito.when(cacheService.getValue(anyString()))
                .thenReturn("123321");
        Mockito.when(cacheService.getValue(TEST_CODE_TRY_KEY))
                .thenReturn("0");
        //check
        Throwable exception = assertThrows(InvalidVerificationCodeException.class, () -> authenticationService.signUp(SIGN_UP_REQUEST_DTO));
        Assertions.assertEquals(exception.getMessage(), "Invalid Verification Code");
        Mockito.verify(cacheService, Mockito.times(2)).getValue(anyString());
    }

    @Test
    void signUpMaxCountTryTest() {
        //prepare data
        Mockito.when(cacheService.getValue(TEST_PHONE_NUMBER))
                .thenReturn(TEST_CODE);
        Mockito.when(cacheService.getValue(TEST_CODE_TRY_KEY))
                .thenReturn("10");
        //check
        Throwable exception = assertThrows(NumberCodeAttemptsException.class, () -> authenticationService.signUp(SIGN_UP_REQUEST_DTO));
        Assertions.assertEquals(exception.getMessage(), "Number code attempts");
        Mockito.verify(cacheService, Mockito.times(2)).getValue(anyString());
    }

    @Test
    void signUpCountTryNullTest() {
        //prepare data
        Mockito.when(cacheService.getValue(TEST_PHONE_NUMBER))
                .thenReturn(TEST_CODE);
        Mockito.when(cacheService.getValue(TEST_CODE_TRY_KEY))
                .thenReturn(null);
        //check
        Throwable exception = assertThrows(NumberCodeAttemptsException.class, () -> authenticationService.signUp(SIGN_UP_REQUEST_DTO));
        Assertions.assertEquals(exception.getMessage(), "Checking sms code attempts error");
        Mockito.verify(cacheService, Mockito.times(2)).getValue(anyString());
    }


    @Test
    void signUpTest() {
        String session = "test";
        User user = getTestUserWithId();

        //prepare data
        Mockito.when(cacheService.getValue(TEST_PHONE_NUMBER))
                .thenReturn(TEST_CODE);
        Mockito.when(cacheService.getValue(TEST_CODE_TRY_KEY))
                .thenReturn("0");
        Mockito.when(userService.createUser(any(CreateUserData.class)))
                .thenReturn(user);
        Mockito.when(jwtProvider.generateJwtToken(any(UserPrincipal.class)))
                .thenReturn(session);
        //check
        AuthenticationData authenticationData = authenticationService.signUp(SIGN_UP_REQUEST_DTO);

        Mockito.verify(cacheService, Mockito.times(2)).getValue(anyString());
        Mockito.verify(cacheService, Mockito.times(1)).incrementValue(anyString());
        Mockito.verify(cacheService, Mockito.times(2)).deleteValue(anyString());
        Assertions.assertEquals(TEST_ID, authenticationData.getId());
        Assertions.assertEquals(session, authenticationData.getToken());
    }

}
