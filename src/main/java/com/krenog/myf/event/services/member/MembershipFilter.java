package com.krenog.myf.event.services.member;

import com.krenog.myf.event.dto.event.EventFilterParameters;
import com.krenog.myf.event.entities.MemberRole;

import javax.validation.constraints.NotNull;

public class MembershipFilter {
    private Long userId;
    private Integer offset;
    private Integer limit;
    private MemberRole role;


    public MembershipFilter(@NotNull Long userId,@NotNull EventFilterParameters eventFilterDto) {
        this.userId = userId;
        this.offset = eventFilterDto.getOffset();
        this.limit = eventFilterDto.getLimit();
        this.role = eventFilterDto.getRole();
    }

    public MembershipFilter(Long userId, Integer offset, Integer limit, MemberRole role) {
        this.userId = userId;
        this.offset = offset;
        this.limit = limit;
        this.role = role;
    }

    public MemberRole getRole() {
        return role;
    }

    public void setRole(MemberRole role) {
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
