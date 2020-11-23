package com.krenog.myf.event.dto.event;

import com.krenog.myf.dto.FilterParameters;
import com.krenog.myf.event.entities.MemberRole;
import io.swagger.annotations.ApiParam;

public class EventFilterParameters extends FilterParameters {
    @ApiParam(value = "Фильтр по ролям")
    private MemberRole role ;


    public MemberRole getRole() {
        return role;
    }

    public void setRole(MemberRole role) {
        this.role = role;
    }
}
