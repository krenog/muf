package com.krenog.myf.event.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krenog.myf.event.entities.Event;
import com.krenog.myf.event.entities.MemberRole;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventWithMembershipDto)) return false;
        if (!super.equals(o)) return false;
        EventWithMembershipDto that = (EventWithMembershipDto) o;
        return role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }
}
