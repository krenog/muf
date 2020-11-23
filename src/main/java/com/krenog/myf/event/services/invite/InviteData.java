package com.krenog.myf.event.services.invite;

import com.krenog.myf.event.entities.Event;
import com.krenog.myf.user.entities.User;

public class InviteData {
    private User fromUser;
    private User toUser;
    private Event event;

    public InviteData(User fromUser, User toUser, Event event) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.event = event;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
