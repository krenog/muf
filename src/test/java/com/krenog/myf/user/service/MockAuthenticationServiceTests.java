package com.krenog.myf.user.service;

import com.krenog.myf.user.dto.authentication.SignInRequestDto;
import com.krenog.myf.user.dto.authentication.SignUpRequestDto;
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
import com.krenog.myf.user.services.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static com.krenog.myf.user.services.authentication.config.Constants.CACHE_COUNT_STRING;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
public class MockAuthenticationServiceTests {
    private static final String PHONE_NUMBER = "7999999999";
    private static final String USERNAME = "krenog";
    private static final String TEST_CODE = "389153";
    private static final Long ID = 1L;
    private static final String codeTryKey = PHONE_NUMBER + CACHE_COUNT_STRING;
    private static final String START_COUNT_TRY_VALUE = "0";
    private static final SignInRequestDto signInRequestDto;
    private static final SignUpRequestDto signUpRequestDto;

    static {
        signInRequestDto = new SignInRequestDto();
        signInRequestDto.setPhoneNumber(PHONE_NUMBER);
        signInRequestDto.setCode(TEST_CODE);

        signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setPhoneNumber(PHONE_NUMBER);
        signUpRequestDto.setCode(TEST_CODE);
        signUpRequestDto.setUsername(USERNAME);
    }

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
        authenticationService.sendSmsCode(PHONE_NUMBER);
        // check
        Mockito.verify(smsService, Mockito.times(0)).sendSms(anyString(), anyString());
        Mockito.verify(cacheService, Mockito.times(1)).setValue(PHONE_NUMBER, TEST_CODE);
        Mockito.verify(cacheService, Mockito.times(1)).setValue(codeTryKey, START_COUNT_TRY_VALUE);
    }

    @Test
    void sendSmsTrafficIsEnabledWithTestPhoneTest() {
        //prepare data
        Mockito.when(config.isTrafficEnabled())
                .thenReturn(true);
        Mockito.when(config.getTestCode())
                .thenReturn(TEST_CODE);
        Mockito.when(config.getPhone())
                .thenReturn(PHONE_NUMBER);
        //call function
        authenticationService.sendSmsCode(PHONE_NUMBER);
        // check
        Mockito.verify(smsService, Mockito.times(0)).sendSms(anyString(), anyString());
        Mockito.verify(cacheService, Mockito.times(1)).setValue(PHONE_NUMBER, TEST_CODE);
        Mockito.verify(cacheService, Mockito.times(1)).setValue(codeTryKey, START_COUNT_TRY_VALUE);
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
        authenticationService.sendSmsCode(PHONE_NUMBER);
        // check
        Mockito.verify(smsService, Mockito.times(1)).sendSms(anyString(), any());
        Mockito.verify(cacheService, Mockito.times(1)).setValue(eq(PHONE_NUMBER), anyString());
        Mockito.verify(cacheService, Mockito.times(1)).setValue(codeTryKey, START_COUNT_TRY_VALUE);
    }

    @Test
    void signInCodeDoesNotExistTest() {
        //prepare data
        Mockito.when(cacheService.getValue(anyString()))
                .thenReturn(null);
        //check
        Throwable exception = assertThrows(CodeDoesNotExistException.class, () -> authenticationService.signIn(signInRequestDto));
        Assertions.assertEquals(exception.getMessage(), "Code does not exist");
        Mockito.verify(cacheService, Mockito.times(1)).getValue(PHONE_NUMBER);
    }

    @Test
    void signInInvalidVerificationCodeTest() {
        //prepare data
        Mockito.when(cacheService.getValue(anyString()))
                .thenReturn("123321");
        Mockito.when(cacheService.getValue(codeTryKey))
                .thenReturn("0");
        //check
        Throwable exception = assertThrows(InvalidVerificationCodeException.class, () -> authenticationService.signIn(signInRequestDto));
        Assertions.assertEquals(exception.getMessage(), "Invalid Verification Code");
        Mockito.verify(cacheService, Mockito.times(2)).getValue(anyString());
    }

    @Test
    void signInMaxCountTryTest() {
        //prepare data
        Mockito.when(cacheService.getValue(PHONE_NUMBER))
                .thenReturn(TEST_CODE);
        Mockito.when(cacheService.getValue(codeTryKey))
                .thenReturn("10");
        //check
        Throwable exception = assertThrows(NumberCodeAttemptsException.class, () -> authenticationService.signIn(signInRequestDto));
        Assertions.assertEquals(exception.getMessage(), "Number code attempts");
        Mockito.verify(cacheService, Mockito.times(2)).getValue(anyString());
    }

    @Test
    void signInCountTryNullTest() {
        //prepare data
        Mockito.when(cacheService.getValue(PHONE_NUMBER))
                .thenReturn(TEST_CODE);
        Mockito.when(cacheService.getValue(codeTryKey))
                .thenReturn(null);
        //check
        Throwable exception = assertThrows(NumberCodeAttemptsException.class, () -> authenticationService.signIn(signInRequestDto));
        Assertions.assertEquals(exception.getMessage(), "Checking sms code attempts error");
        Mockito.verify(cacheService, Mockito.times(2)).getValue(anyString());
    }


    @Test
    void signInTest() {
        String session = "test";
        User user = new User();
        user.setId(1L);
        user.setPhoneNumber(PHONE_NUMBER);

        //prepare data
        Mockito.when(cacheService.getValue(PHONE_NUMBER))
                .thenReturn(TEST_CODE);
        Mockito.when(cacheService.getValue(codeTryKey))
                .thenReturn("0");
        Mockito.when(userService.getUserByPhoneNumber(PHONE_NUMBER))
                .thenReturn(user);
        Mockito.when(jwtProvider.generateJwtToken(any(UserPrincipal.class)))
                .thenReturn(session);
        //check
        AuthenticationData authenticationData = authenticationService.signIn(signInRequestDto);

        Mockito.verify(cacheService, Mockito.times(2)).getValue(anyString());
        Mockito.verify(cacheService, Mockito.times(1)).incrementValue(anyString());
        Mockito.verify(cacheService, Mockito.times(2)).deleteValue(anyString());
        Assertions.assertEquals(ID, authenticationData.getId());
        Assertions.assertNotNull(user.getLastLogin());
        Assertions.assertEquals(session, authenticationData.getToken());
    }


    @Test
    void signUpCodeDoesNotExistTest() {
        //prepare data
        Mockito.when(cacheService.getValue(anyString()))
                .thenReturn(null);
        //check
        Throwable exception = assertThrows(CodeDoesNotExistException.class, () -> authenticationService.signUp(signUpRequestDto));
        Assertions.assertEquals(exception.getMessage(), "Code does not exist");
        Mockito.verify(cacheService, Mockito.times(1)).getValue(PHONE_NUMBER);
    }

    @Test
    void signUpInvalidVerificationCodeTest() {
        //prepare data
        Mockito.when(cacheService.getValue(anyString()))
                .thenReturn("123321");
        Mockito.when(cacheService.getValue(codeTryKey))
                .thenReturn("0");
        //check
        Throwable exception = assertThrows(InvalidVerificationCodeException.class, () -> authenticationService.signUp(signUpRequestDto));
        Assertions.assertEquals(exception.getMessage(), "Invalid Verification Code");
        Mockito.verify(cacheService, Mockito.times(2)).getValue(anyString());
    }

    @Test
    void signUpMaxCountTryTest() {
        //prepare data
        Mockito.when(cacheService.getValue(PHONE_NUMBER))
                .thenReturn(TEST_CODE);
        Mockito.when(cacheService.getValue(codeTryKey))
                .thenReturn("10");
        //check
        Throwable exception = assertThrows(NumberCodeAttemptsException.class, () -> authenticationService.signUp(signUpRequestDto));
        Assertions.assertEquals(exception.getMessage(), "Number code attempts");
        Mockito.verify(cacheService, Mockito.times(2)).getValue(anyString());
    }

    @Test
    void signUpCountTryNullTest() {
        //prepare data
        Mockito.when(cacheService.getValue(PHONE_NUMBER))
                .thenReturn(TEST_CODE);
        Mockito.when(cacheService.getValue(codeTryKey))
                .thenReturn(null);
        //check
        Throwable exception = assertThrows(NumberCodeAttemptsException.class, () -> authenticationService.signUp(signUpRequestDto));
        Assertions.assertEquals(exception.getMessage(), "Checking sms code attempts error");
        Mockito.verify(cacheService, Mockito.times(2)).getValue(anyString());
    }


    @Test
    void signUpTest() {
        String session = "test";
        User user = new User();
        user.setId(1L);
        user.setPhoneNumber(PHONE_NUMBER);

        //prepare data
        Mockito.when(cacheService.getValue(PHONE_NUMBER))
                .thenReturn(TEST_CODE);
        Mockito.when(cacheService.getValue(codeTryKey))
                .thenReturn("0");
        Mockito.when(userService.createUser(PHONE_NUMBER, USERNAME))
                .thenReturn(user);
        Mockito.when(jwtProvider.generateJwtToken(any(UserPrincipal.class)))
                .thenReturn(session);
        //check
        AuthenticationData authenticationData = authenticationService.signUp(signUpRequestDto);

        Mockito.verify(cacheService, Mockito.times(2)).getValue(anyString());
        Mockito.verify(cacheService, Mockito.times(1)).incrementValue(anyString());
        Mockito.verify(cacheService, Mockito.times(2)).deleteValue(anyString());
        Assertions.assertEquals(ID, authenticationData.getId());
        Assertions.assertEquals(session, authenticationData.getToken());
    }

}
