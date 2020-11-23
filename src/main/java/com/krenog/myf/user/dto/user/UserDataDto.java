package com.krenog.myf.user.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krenog.myf.user.services.authentication.AuthenticationData;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;

public class UserDataDto extends CommonUserDataDto {
    @JsonProperty("phoneNumber")
    @ApiModelProperty(notes = "Номер телефона")
    private String phoneNumber;
    @JsonProperty("email")
    @Email(regexp = ".*@.*\\..*", message = "Email should be valid")
    @ApiModelProperty(notes = "Почта")
    private String email;

    public UserDataDto(AuthenticationData authenticationData) {
        super(authenticationData);
        this.phoneNumber = authenticationData.getPhoneNumber();
        this.email = authenticationData.getEmail();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
