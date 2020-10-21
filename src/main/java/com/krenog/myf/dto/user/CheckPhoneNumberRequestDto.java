package com.krenog.myf.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CheckPhoneNumberRequestDto {
    @JsonProperty("phoneNumber")
    @NotBlank
    @Size(max = 11)
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
