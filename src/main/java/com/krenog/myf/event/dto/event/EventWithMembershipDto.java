package com.krenog.myf.event.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krenog.myf.event.entities.Event;
import com.krenog.myf.event.entities.MemberRole;

public class EventWithMembershipDto extends EventDto {
    @JsonProperty("role")
    private MemberRole role;

    public EventWithMembershipDto(Event event, MemberRole role) {
        super(event);
        this.role = role;
    }

    public MemberRole getRole() {
        return role;
    }

    public void setRole(MemberRole role) {
        this.role = role;
    }
}
