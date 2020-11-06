package com.krenog.myf.user.service;

import com.krenog.myf.exceptions.NotFoundException;
import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.repositories.UserRepository;
import com.krenog.myf.user.services.authentication.exceptions.UserAlreadyExistException;
import com.krenog.myf.user.services.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceTests {
    private final static String PHONE_NUMBER = "7999999999";
    private static final String USERNAME = "test";
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


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
    void getByPhoneNumberNotFoundExceptionTest() {
        Exception exception = assertThrows(NotFoundException.class, () -> userService.getUserByPhoneNumber(PHONE_NUMBER));
    }

    @Test
    void getByPhoneNumberTest() {
        //prepare data
        User user = saveUser();
        //call
        User found = userService.getUserByPhoneNumber(PHONE_NUMBER);
        // check
        Assertions.assertEquals(
                user.getPhoneNumber(), found.getPhoneNumber());
        Assertions.assertEquals(
                user.getUsername(), found.getUsername());
        Assertions.assertEquals(
                user.getId(), found.getId());
    }

    @Test
    void createUserTest() {
        //call
        User created = userService.createUser(PHONE_NUMBER, USERNAME);
        // check
        Assertions.assertEquals(
                PHONE_NUMBER, created.getPhoneNumber());
        Assertions.assertEquals(
                USERNAME, created.getUsername());
        Assertions.assertNotNull(created.getId());
    }

    @Test
    void createUserUserAlreadyExistExceptionTest() {
        //prepare data
        User user = saveUser();
        //call function
        Exception exception = assertThrows(UserAlreadyExistException.class, () -> userService.createUser(PHONE_NUMBER, USERNAME));
    }

    @Test
    void updatePushTokenTest() {
        //prepare data
        User user = saveUser();
        //call function
        userService.updatePushToken(user.getId(), "test");
    }

    @Test
    void updateLastLoginTest() {
        //prepare data
        User user = saveUser();
        //call function
        userService.updateLastLogin(user);
        // check
        Assertions.assertNotNull(user.getLastLogin());
    }

    @Test
    void checkUsernameIsExistTrueTest() {
        //prepare data
        saveUser();
        //call function
        Boolean exist = userService.checkUsernameIsExist(USERNAME);
        // check
        Assertions.assertTrue(exist);
    }

    @Test
    void checkUsernameIsExistFalseTest() {
        //call function
        Boolean exist = userService.checkUsernameIsExist(USERNAME);
        // check
        Assertions.assertFalse(exist);
    }

    @Test
    void checkPhoneNumberIsExistTrueTest() {
        //prepare data
        saveUser();
        //call function
        Boolean exist = userService.checkPhoneNumberIsExist(PHONE_NUMBER);
        // check
        Assertions.assertTrue(exist);
    }

    @Test
    void checkPhoneNumberIsExistFalseTest() {
        //call function
        Boolean exist = userService.checkPhoneNumberIsExist(PHONE_NUMBER);
        // check
        Assertions.assertFalse(exist);
    }

}
