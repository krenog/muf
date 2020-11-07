package com.krenog.myf.user.service;

import com.krenog.myf.exceptions.NotFoundException;
import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.repositories.UserRepository;
import com.krenog.myf.user.services.authentication.exceptions.UserAlreadyExistException;
import com.krenog.myf.user.services.user.CreateUserData;
import com.krenog.myf.user.services.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.krenog.myf.user.UserTestUtils.TEST_PHONE_NUMBER;
import static com.krenog.myf.user.UserTestUtils.TEST_USERNAME;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setBeforeEach() {
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
    void getByPhoneNumberNotFoundExceptionTest() {
        Exception exception = assertThrows(NotFoundException.class, () -> userService.getUserByPhoneNumber(TEST_PHONE_NUMBER));
    }

    @Test
    void getByPhoneNumberTest() {
        //prepare data
        User user = saveUser();
        //call
        User found = userService.getUserByPhoneNumber(TEST_PHONE_NUMBER);
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
        User created = userService.createUser(new CreateUserData(TEST_USERNAME, TEST_PHONE_NUMBER));
        // check
        Assertions.assertEquals(
                TEST_PHONE_NUMBER, created.getPhoneNumber());
        Assertions.assertEquals(
                TEST_USERNAME, created.getUsername());
        Assertions.assertNotNull(created.getId());
    }

    @Test
    void createUserUserAlreadyExistExceptionTest() {
        //prepare data
        saveUser();
        //call function
        Exception exception = assertThrows(UserAlreadyExistException.class, () -> userService.createUser(new CreateUserData(TEST_USERNAME, TEST_PHONE_NUMBER)));
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
        Boolean exist = userService.checkUsernameIsExist(TEST_USERNAME);
        // check
        Assertions.assertTrue(exist);
    }

    @Test
    void checkUsernameIsExistFalseTest() {
        //call function
        Boolean exist = userService.checkUsernameIsExist(TEST_USERNAME);
        // check
        Assertions.assertFalse(exist);
    }

    @Test
    void checkPhoneNumberIsExistTrueTest() {
        //prepare data
        saveUser();
        //call function
        Boolean exist = userService.checkPhoneNumberIsExist(TEST_PHONE_NUMBER);
        // check
        Assertions.assertTrue(exist);
    }

    @Test
    void checkPhoneNumberIsExistFalseTest() {
        //call function
        Boolean exist = userService.checkPhoneNumberIsExist(TEST_PHONE_NUMBER);
        // check
        Assertions.assertFalse(exist);
    }

}
