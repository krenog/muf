package com.krenog.myf.user.services.user;

import com.krenog.myf.user.entities.User;

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
}
