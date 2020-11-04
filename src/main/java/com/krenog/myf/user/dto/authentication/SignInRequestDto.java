package com.krenog.myf.user.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignInRequestDto {
    @JsonProperty("phoneNumber")
    @NotBlank
    @Size(max = 11)
    private String phoneNumber;
    @JsonProperty("code")
    private String code;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
