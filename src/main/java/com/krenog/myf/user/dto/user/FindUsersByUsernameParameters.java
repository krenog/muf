package com.krenog.myf.user.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krenog.myf.dto.FilterParameters;
import io.swagger.annotations.ApiParam;

public class FindUsersByUsernameParameters extends FilterParameters {
    @JsonProperty(value = "username")
    @ApiParam(value = "Поиск по юзернейму")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
