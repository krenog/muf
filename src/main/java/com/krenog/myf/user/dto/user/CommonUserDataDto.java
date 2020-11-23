package com.krenog.myf.user.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.services.authentication.AuthenticationData;
import io.swagger.annotations.ApiModelProperty;

public class CommonUserDataDto {
    @JsonProperty("id")
    @ApiModelProperty(notes = "Идентификатор пользователя")
    private Long id;
    @JsonProperty("username")
    @ApiModelProperty(notes = "Никнейм")
    private String username;
    @JsonProperty("firstName")
    @ApiModelProperty(notes = "Имя")
    private String firstName;
    @JsonProperty("lastName")
    @ApiModelProperty(notes = "Фамилия")
    private String lastName;

    public CommonUserDataDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    public CommonUserDataDto(AuthenticationData authenticationData) {
        this.id = authenticationData.getId();
        this.username = authenticationData.getUsername();
        this.firstName = authenticationData.getFirstName();
        this.lastName = authenticationData.getLastName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
