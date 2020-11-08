package com.krenog.myf.event.entities;

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
