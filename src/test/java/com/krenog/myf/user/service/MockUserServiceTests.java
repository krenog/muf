package com.krenog.myf.user.service;

import com.krenog.myf.exceptions.NotFoundException;
import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.repositories.UserRepository;
import com.krenog.myf.user.services.user.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
public class MockUserServiceTests {
    private static final String PHONE_NUMBER = "7999999999";
    private static final Long ID = 1L;
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;

    @Test
    void getByPhoneNumberTest() {
        //prepare data
        User user = new User();
        user.setPhoneNumber(PHONE_NUMBER);
        user.setId(ID);
        Mockito.when(userRepository.getByPhoneNumber(user.getPhoneNumber()))
                .thenReturn(Optional.of(user));
        //call function
        User found = userService.getUserByPhoneNumber(PHONE_NUMBER);
        // check
        Assertions.assertEquals(
                user.getPhoneNumber(), found.getPhoneNumber());
        Mockito.verify(userRepository, Mockito.times(1)).getByPhoneNumber(anyString());
    }

    @Test
    void getByPhoneNumberNotFoundExceptionTest() {
        //prepare data
        User user = new User();
        user.setPhoneNumber(PHONE_NUMBER);
        user.setId(ID);
        Mockito.when(userRepository.getByPhoneNumber(user.getPhoneNumber()))
                .thenReturn(Optional.empty());
        //call function
        Exception exception = assertThrows(NotFoundException.class, () -> userService.getUserByPhoneNumber(PHONE_NUMBER));
        // check
        Mockito.verify(userRepository, Mockito.times(1)).getByPhoneNumber(anyString());
    }

}
