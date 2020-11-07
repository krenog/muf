package com.krenog.myf.user.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krenog.myf.user.dto.user.UserDataDto;
import com.krenog.myf.user.services.authentication.AuthenticationData;

public class SignInResponseDto {
    @JsonProperty("user")
    private UserDataDto user;

    @JsonProperty("token")
    private String token;

    public SignInResponseDto(AuthenticationData data) {
        this.user = new UserDataDto(data);
        this.token = data.getToken();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDataDto getUser() {
        return user;
    }

    public void setUser(UserDataDto user) {
        this.user = user;
    }
}
