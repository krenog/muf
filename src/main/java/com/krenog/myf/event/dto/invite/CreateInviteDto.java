package com.krenog.myf.event.dto.invite;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateInviteDto {
    @JsonProperty("invitedUserId")
    private Long invitedUserId;
    @JsonProperty("eventId")
    private Long eventId;

    public Long getInvitedUserId() {
        return invitedUserId;
    }

    public void setInvitedUserId(Long invitedUserId) {
        this.invitedUserId = invitedUserId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}
