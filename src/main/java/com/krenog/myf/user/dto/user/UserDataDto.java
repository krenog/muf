package com.krenog.myf.user.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krenog.myf.user.services.authentication.AuthenticationData;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;

public class UserDataDto {
    @JsonProperty("id")
    @ApiModelProperty(notes = "Идентификатор пользователя")
    private Long id;
    @JsonProperty("phoneNumber")
    @ApiModelProperty(notes = "Номер телефона")
    private String phoneNumber;
    @JsonProperty("username")
    @ApiModelProperty(notes = "Никнейм")
    private String username;
    @JsonProperty("email")
    @Email(regexp=".*@.*\\..*", message = "Email should be valid")
    @ApiModelProperty(notes = "Почта")
    private String email;
    @JsonProperty("firstName")
    @ApiModelProperty(notes = "Имя")
    private String firstName;
    @JsonProperty("lastName")
    @ApiModelProperty(notes = "Фамилия")
    private String lastName;

    public UserDataDto(AuthenticationData authenticationData) {
        this.id = authenticationData.getId();
        this.phoneNumber = authenticationData.getPhoneNumber();
        this.username = authenticationData.getUsername();
        this.email = authenticationData.getEmail();
        this.firstName = authenticationData.getFirstName();
        this.lastName = authenticationData.getLastName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
