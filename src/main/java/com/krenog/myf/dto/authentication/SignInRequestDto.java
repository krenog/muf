package com.krenog.myf.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignInRequestDto {
    @JsonProperty("phoneNumber")
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
