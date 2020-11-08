package com.krenog.myf.user.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

public class CheckExistResponseDto {
    @JsonProperty("exist")
    @NotBlank
    @ApiModelProperty(notes = "Существует ли объект в системе")
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
