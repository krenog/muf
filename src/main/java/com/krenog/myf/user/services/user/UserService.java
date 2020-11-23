package com.krenog.myf.user.services.user;

import com.krenog.myf.user.dto.user.FindUsersByUsernameParameters;
import com.krenog.myf.user.entities.User;

import java.util.List;

/**
 * Service for working with User entity
 */
public interface UserService {
    User getUserByPhoneNumber(String phoneNumber);

    User createUser(CreateUserData createUserData);

    void updateLastLogin(User user);

    void updatePushToken(Long userId, String pushToken);

    Boolean checkUsernameIsExist(String username);

    Boolean checkPhoneNumberIsExist(String phoneNumber);

    List<User> findUsersByUsername(FindUsersByUsernameParameters findUsersByUsernameDto);
}
