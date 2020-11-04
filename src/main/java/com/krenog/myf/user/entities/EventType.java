package com.krenog.myf.user.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EventType {
    PUBLIC,
    PRIVATE;

    @JsonValue
    public int getValue() {
        return ordinal();
    }
}
