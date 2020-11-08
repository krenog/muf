package com.krenog.myf.user.service;

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

import static com.krenog.myf.user.UserTestUtils.*;
import static com.krenog.myf.utils.TestUtils.TEST_PHONE_NUMBER;
import static com.krenog.myf.utils.TestUtils.getTestUser;

@SpringBootTest
public class AuthenticationServiceTests {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setBeforeEach() {
        Optional<User> user = userRepository.getByPhoneNumber(TEST_PHONE_NUMBER);
        user.ifPresent(value -> userRepository.delete(value));
    }

    private User saveUser() {
        User user = getTestUser();
        return userRepository.save(user);
    }

    @Test
    void sendSmsTest() {
        authenticationService.sendSmsCode(TEST_PHONE_NUMBER);
        String cacheCode = cacheService.getValue(TEST_PHONE_NUMBER);
        String codeTryKey = TEST_PHONE_NUMBER + TEST_CACHE_COUNT_STRING;
        String count = cacheService.getValue(codeTryKey);
        Assertions.assertEquals(TEST_CODE, cacheCode);
        Assertions.assertEquals("0", count);
    }

    @Test
    void signInTest() {
        //prepare data
        User user = saveUser();
        cacheService.setValue(TEST_PHONE_NUMBER, TEST_CODE);
        String codeTryKey = TEST_PHONE_NUMBER + TEST_CACHE_COUNT_STRING;
        cacheService.setValue(codeTryKey, "0");
        //call
        AuthenticationData authenticationData = authenticationService.signIn(SIGN_IN_REQUEST_DTO);
        //check
        Assertions.assertEquals(user.getId(), authenticationData.getId());
        Assertions.assertNotNull(user.getLastLogin());
        Assertions.assertNotNull(authenticationData.getToken());
    }

    @Test
    void signUpTest() {
        //prepare data
        cacheService.setValue(TEST_PHONE_NUMBER, TEST_CODE);
        String codeTryKey = TEST_PHONE_NUMBER + TEST_CACHE_COUNT_STRING;
        cacheService.setValue(codeTryKey, "0");
        //call
        AuthenticationData authenticationData = authenticationService.signUp(SIGN_UP_REQUEST_DTO);
        //check
        Assertions.assertNotNull(authenticationData.getId());
        Assertions.assertNotNull(authenticationData.getToken());
    }

}
