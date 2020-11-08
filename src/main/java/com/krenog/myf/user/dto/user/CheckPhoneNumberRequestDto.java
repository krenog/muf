package com.krenog.myf.user.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CheckPhoneNumberRequestDto {
    @JsonProperty("phoneNumber")
    @NotBlank
    @Size(max = 11,message = "Phone should be 11 length size")
    @ApiModelProperty(notes = "Номер телефона")
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
