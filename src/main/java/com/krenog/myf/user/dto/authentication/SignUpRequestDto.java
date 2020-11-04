package com.krenog.myf.user.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignUpRequestDto extends SignInRequestDto {
    @JsonProperty("username")
    @NotBlank
    @Size(min = 5)
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
