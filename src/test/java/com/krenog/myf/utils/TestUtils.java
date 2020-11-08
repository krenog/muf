package com.krenog.myf.utils;

import com.krenog.myf.user.entities.User;

public class TestUtils {
    public final static String TEST_PHONE_NUMBER = "7999999999";
    public static final String TEST_USERNAME = "test";
    public static final Long TEST_ID = 1L;

    public static User getTestUserWithId() {
        User user = getTestUser();
        user.setId(TEST_ID);
        return user;
    }

    public static User getTestUser() {
        User user = new User();
        user.setPhoneNumber(TEST_PHONE_NUMBER);
        user.setUsername(TEST_USERNAME);
        return user;
    }
}
