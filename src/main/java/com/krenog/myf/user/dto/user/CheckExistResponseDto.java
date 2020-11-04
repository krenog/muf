package com.krenog.myf.user.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckExistResponseDto {
    @JsonProperty("exist")
    private Boolean isExist;

    public CheckExistResponseDto() {
    }

    public CheckExistResponseDto(Boolean isExist) {
        this.isExist = isExist;
    }

    public Boolean getExist() {
        return isExist;
    }

    public void setExist(Boolean exist) {
        isExist = exist;
    }
}
