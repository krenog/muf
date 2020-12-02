package com.krenog.myf.user.service;

import com.krenog.myf.exceptions.NotFoundException;
import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.repositories.UserRepository;
import com.krenog.myf.user.services.authentication.exceptions.UserAlreadyExistException;
import com.krenog.myf.user.services.user.CreateUserData;
import com.krenog.myf.user.services.user.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static com.krenog.myf.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
@Tag("CommonTest")
public class MockUserServiceTests {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;

    @Test
    void getByPhoneNumberTest() {
        //prepare data
        User user = getTestUserWithId();
        Mockito.when(userRepository.getByPhoneNumber(user.getPhoneNumber()))
                .thenReturn(Optional.of(user));
        //call function
        User found = userService.getUserByPhoneNumber(TEST_PHONE_NUMBER);
        // check
        Assertions.assertEquals(
                user.getPhoneNumber(), found.getPhoneNumber());
        Mockito.verify(userRepository, Mockito.times(1)).getByPhoneNumber(anyString());
    }

    @Test
    void getByPhoneNumberNotFoundExceptionTest() {
        //prepare data
        User user = getTestUserWithId();
        Mockito.when(userRepository.getByPhoneNumber(user.getPhoneNumber()))
                .thenReturn(Optional.empty());
        //call function
        Exception exception = assertThrows(NotFoundException.class, () -> userService.getUserByPhoneNumber(TEST_PHONE_NUMBER));
        // check
        Mockito.verify(userRepository, Mockito.times(1)).getByPhoneNumber(anyString());
    }

    @Test
    void createUserTest() {
        //prepare data
        User user = getTestUserWithId();
        Mockito.when(userRepository.save(any(User.class)))
                .thenReturn(user);
        //call function
        User created = userService.createUser(new CreateUserData(TEST_USERNAME, TEST_PHONE_NUMBER));
        // check
        Assertions.assertEquals(
                user.getPhoneNumber(), created.getPhoneNumber());
        Assertions.assertEquals(
                user.getUsername(), created.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).save(any());
    }

    @Test
    void createUserUserAlreadyExistExceptionTest() {
        //prepare data
        Mockito.when(userRepository.save(any(User.class)))
                .thenThrow(DataIntegrityViolationException.class);
        //call function
        Exception exception = assertThrows(UserAlreadyExistException.class, () -> userService.createUser(new CreateUserData(TEST_USERNAME, TEST_PHONE_NUMBER)));
        // check
        Mockito.verify(userRepository, Mockito.times(1)).save(any());
    }

    @Test
    void updatePushTokenTest() {
        //prepare data
        User user = getTestUserWithId();
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(any(User.class)))
                .thenReturn(user);
        //call function
        userService.updatePushToken(TEST_ID, "test");
        // check
        Assertions.assertEquals(
                "test", user.getPushToken());
        Mockito.verify(userRepository, Mockito.times(1)).save(any());
        Mockito.verify(userRepository, Mockito.times(1)).findById(any());
    }

    @Test
    void updatePushTokenTestNotFoundException() {
        //prepare data
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        //call function
        Exception exception = assertThrows(NotFoundException.class, () -> userService.updatePushToken(TEST_ID, "test"));
        // check
        Assertions.assertEquals("User not found", exception.getMessage());
        Mockito.verify(userRepository, Mockito.times(1)).findById(any());
    }


    @Test
    void updateLastLoginTest() {
        //prepare data
        User user = getTestUser();
        Mockito.when(userRepository.save(any(User.class)))
                .thenReturn(user);
        //call function
        userService.updateLastLogin(user);
        // check
        Assertions.assertNotNull(user.getLastLogin());
        Mockito.verify(userRepository, Mockito.times(1)).save(any());
    }

    @Test
    void checkUsernameIsExistTrueTest() {
        //prepare data
        Mockito.when(userRepository.existsByUsername(any()))
                .thenReturn(true);
        //call function
        Boolean exist = userService.checkUsernameIsExist(TEST_USERNAME);
        // check
        Assertions.assertTrue(exist);
        Mockito.verify(userRepository, Mockito.times(1)).existsByUsername(any());
    }

    @Test
    void checkUsernameIsExistFalseTest() {
        //prepare data
        Mockito.when(userRepository.existsByUsername(any()))
                .thenReturn(false);
        //call function
        Boolean exist = userService.checkUsernameIsExist(TEST_USERNAME);
        // check
        Assertions.assertFalse(exist);
        Mockito.verify(userRepository, Mockito.times(1)).existsByUsername(any());
    }

    @Test
    void checkPhoneNumberIsExistTrueTest() {
        //prepare data
        Mockito.when(userRepository.existsByPhoneNumber(any()))
                .thenReturn(true);
        //call function
        Boolean exist = userService.checkPhoneNumberIsExist(TEST_PHONE_NUMBER);
        // check
        Assertions.assertTrue(exist);
        Mockito.verify(userRepository, Mockito.times(1)).existsByPhoneNumber(any());
    }

    @Test
    void checkPhoneNumberIsExistFalseTest() {
        //prepare data
        Mockito.when(userRepository.existsByPhoneNumber(any()))
                .thenReturn(false);
        //call function
        Boolean exist = userService.checkPhoneNumberIsExist(TEST_PHONE_NUMBER);
        // check
        Assertions.assertFalse(exist);
        Mockito.verify(userRepository, Mockito.times(1)).existsByPhoneNumber(any());
    }

    @Test
    void getByIdTest() {
        //prepare data
        User user = getTestUserWithId();
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        //call
        User foundedUser = userService.getUserById(user.getId());
        // check
        Assertions.assertEquals(
                user.getId(), foundedUser.getId());
        Assertions.assertEquals(
                user.getPhoneNumber(), foundedUser.getPhoneNumber());
    }

    @Test
    void getByIdNotFoundException() {
        //prepare data
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        //call function
        Exception exception = assertThrows(NotFoundException.class, () -> userService.getUserById(TEST_ID));
        Assertions.assertEquals(
                "User not found", exception.getMessage());
    }
}
