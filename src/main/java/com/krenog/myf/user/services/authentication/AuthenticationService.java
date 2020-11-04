package com.krenog.myf.user.services.authentication;

import com.krenog.myf.user.dto.authentication.SignInRequestDto;
import com.krenog.myf.user.dto.authentication.SignUpRequestDto;

public interface AuthenticationService {

    void sendSmsCode(String phone);

    AuthenticationData signIn(SignInRequestDto signInRequestDto);

    AuthenticationData signUp(SignUpRequestDto signUpRequestDto);
}
