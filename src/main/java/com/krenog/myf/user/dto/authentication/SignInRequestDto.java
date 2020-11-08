package com.krenog.myf.user.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignInRequestDto {
    @JsonProperty("phoneNumber")
    @NotBlank
    @Size(max = 11,message = "Phone should be 11 length size")
    @ApiModelProperty(notes = "Номер телефона")
    private String phoneNumber;
    @JsonProperty("code")
    @ApiModelProperty(notes = "Код из смс")
    private String code;

    public SignInRequestDto() {
    }

    public SignInRequestDto(@NotBlank @Size(max = 11) String phoneNumber, String code) {
        this.phoneNumber = phoneNumber;
        this.code = code;
    }

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
