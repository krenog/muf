package com.krenog.myf.event.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EventStatus {
    CREATED,
    ACTIVE,
    CANCELED,
    PASSED;

    @JsonValue
    public int getValue() {
        return ordinal();
    }
}
