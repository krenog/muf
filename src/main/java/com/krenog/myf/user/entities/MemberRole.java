package com.krenog.myf.user.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MemberRole {
    OWNER,
    ADMIN,
    GUEST;

    @JsonValue
    public int getValue() {
        return ordinal();
    }
}
