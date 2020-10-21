package com.krenog.myf.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignUpRequestDto {
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("code")
    private String code;
    @JsonProperty("username")
    private String username;
}
