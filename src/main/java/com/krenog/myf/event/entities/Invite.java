package com.krenog.myf.event.entities;

import com.krenog.myf.entity.BaseEntity;
import com.krenog.myf.event.services.invite.InviteData;
import com.krenog.myf.user.entities.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "myf_invite")
public class Invite extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "invited_user_id")
    private User invitedUser;

    @ManyToOne
    @JoinColumn(name = "event")
    private Event event;

    private InviteStatus inviteStatus = InviteStatus.CREATED;

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        super.setUpdateDate(LocalDateTime.now());
    }

    public Invite() {
    }

    public Invite(InviteData inviteData) {
        this.user = inviteData.getFromUser();
        this.invitedUser = inviteData.getToUser();
        this.event = inviteData.getEvent();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User referringUser) {
        this.user = referringUser;
    }

    public User getInvitedUser() {
        return invitedUser;
    }

    public void setInvitedUser(User invitedUser) {
        this.invitedUser = invitedUser;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public InviteStatus getInviteStatus() {
        return inviteStatus;
    }

    public void setInviteStatus(InviteStatus inviteStatus) {
        this.inviteStatus = inviteStatus;
    }
}
