package com.krenog.myf.user.service;

import com.krenog.myf.user.dto.authentication.SignInRequestDto;
import com.krenog.myf.user.dto.authentication.SignUpRequestDto;
import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.repositories.UserRepository;
import com.krenog.myf.user.services.authentication.AuthenticationData;
import com.krenog.myf.user.services.authentication.AuthenticationService;
import com.krenog.myf.user.services.cache.CacheService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class AuthenticationServiceTests {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;


    private static final String PHONE_NUMBER = "7999999999";
    private static final String USERNAME = "test";
    private static final String TEST_CODE = "389153";
    private static final String CACHE_COUNT_STRING = "_count_try_code";
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


    @BeforeEach
    void setBeforeEach() {
        Optional<User> user = userRepository.getByPhoneNumber(PHONE_NUMBER);
        user.ifPresent(value -> userRepository.delete(value));
    }

    private User saveUser() {
        User user = new User();
        user.setPhoneNumber(PHONE_NUMBER);
        user.setUsername(USERNAME);
        return userRepository.save(user);
    }

    @Test
    void sendSmsTest() {
        authenticationService.sendSmsCode(PHONE_NUMBER);
        String cacheCode = cacheService.getValue(PHONE_NUMBER);
        String codeTryKey = PHONE_NUMBER + CACHE_COUNT_STRING;
        String count = cacheService.getValue(codeTryKey);
        Assertions.assertEquals(TEST_CODE, cacheCode);
        Assertions.assertEquals("0", count);
    }

    @Test
    void signInTest() {
        //prepare data
        User user = saveUser();
        cacheService.setValue(PHONE_NUMBER, TEST_CODE);
        String codeTryKey = PHONE_NUMBER + CACHE_COUNT_STRING;
        cacheService.setValue(codeTryKey, "0");
        //call
        AuthenticationData authenticationData = authenticationService.signIn(signInRequestDto);
        //check
        Assertions.assertEquals(user.getId(), authenticationData.getId());
        Assertions.assertNotNull(user.getLastLogin());
        Assertions.assertNotNull(authenticationData.getToken());
    }

    @Test
    void signUpTest() {
        //prepare data
        cacheService.setValue(PHONE_NUMBER, TEST_CODE);
        String codeTryKey = PHONE_NUMBER + CACHE_COUNT_STRING;
        cacheService.setValue(codeTryKey, "0");
        //call
        AuthenticationData authenticationData = authenticationService.signUp(signUpRequestDto);
        //check
        Assertions.assertNotNull(authenticationData.getId());
        Assertions.assertNotNull(authenticationData.getToken());
    }

}
