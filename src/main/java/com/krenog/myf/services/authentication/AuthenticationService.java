package com.krenog.myf.services.authentication;

public interface AuthenticationService {

    void sendSmsCode(String phone);

    AuthenticationData findUserAndAuthenticate(String phoneNumber, String code);

    AuthenticationData createUserAndAuthenticate(String phoneNumber, String username, String code);
}
