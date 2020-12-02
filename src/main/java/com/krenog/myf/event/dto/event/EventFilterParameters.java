package com.krenog.myf.event.dto.event;

import com.krenog.myf.dto.FilterParameters;
import com.krenog.myf.event.entities.MemberRole;
import io.swagger.annotations.ApiParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class EventFilterParameters extends FilterParameters {
    @ApiParam(value = "Фильтр по ролям")
    private MemberRole role ;

    public EventFilterParameters() {
    }

    public EventFilterParameters(MemberRole role) {
        this.role = role;
    }

    public EventFilterParameters(@Max(value = 100, message = "limit should be less than 100") Integer limit, @Min(value = 1, message = "limit should be more than 0") Integer offset, MemberRole role) {
        super(limit, offset);
        this.role = role;
    }

    public MemberRole getRole() {
        return role;
    }

    public void setRole(MemberRole role) {
        this.role = role;
    }
}
