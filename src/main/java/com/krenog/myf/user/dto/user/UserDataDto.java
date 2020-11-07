package com.krenog.myf.user.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krenog.myf.user.services.authentication.AuthenticationData;

public class UserDataDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("username")
    private String username;
    @JsonProperty("email")
    private String email;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
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
