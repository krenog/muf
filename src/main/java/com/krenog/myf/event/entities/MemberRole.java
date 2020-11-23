package com.krenog.myf.event.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MemberRole {
    OWNER("owner"),
    ADMIN("admin"),
    GUEST("guest");

    private final String filteringValue;

    public String getFilteringValue() {
        return filteringValue;
    }

    MemberRole(String filteringValue) {
        this.filteringValue = filteringValue;
    }

    @JsonValue
    public String getValue() {
        return this.getFilteringValue();
    }
}
