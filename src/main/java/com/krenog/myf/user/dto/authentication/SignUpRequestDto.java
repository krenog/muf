package com.krenog.myf.user.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignUpRequestDto extends SignInRequestDto {
    @JsonProperty("username")
    @NotBlank
    @Size(min = 5)
    private String username;

    public SignUpRequestDto() {
    }

    public SignUpRequestDto(@NotBlank @Size(max = 11) String phoneNumber, String code, @NotBlank @Size(min = 5) String username) {
        super(phoneNumber, code);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
