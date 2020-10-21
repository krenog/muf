package com.krenog.myf.services.user;

import com.krenog.myf.entities.User;

/**
 * Service for working with User entity
 */
public interface UserService {
    User getUserByPhoneNumber(String phoneNumber);

    User createUser(String phoneNumber, String username);

    void updateLastLogin(User user);

    void updatePushToken(Long userId, String pushToken);

    Boolean checkUsernameIsExist(String username);

    Boolean checkPhoneNumberIsExist(String phoneNumber);
}
