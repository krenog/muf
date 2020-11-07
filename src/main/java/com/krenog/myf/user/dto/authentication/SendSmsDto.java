package com.krenog.myf.user.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SendSmsDto {
    @JsonProperty("phoneNumber")
    @NotBlank
    @Size(max = 11)
    private String phoneNumber;

    public SendSmsDto(@NotBlank @Size(max = 11) String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public SendSmsDto() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
