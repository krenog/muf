package com.krenog.myf.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "muf_invite")
public class Invite extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "referring_user")
    private User referringUser;

    @ManyToOne
    @JoinColumn(name = "invitee_user")
    private User inviteeUser;

    @ManyToOne
    @JoinColumn(name = "event")
    private Event event;

    private InviteStatus inviteStatus = InviteStatus.CREATED;

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        super.setUpdateDate(LocalDateTime.now());
    }

    public User getReferringUser() {
        return referringUser;
    }

    public void setReferringUser(User referringUser) {
        this.referringUser = referringUser;
    }

    public User getInviteeUser() {
        return inviteeUser;
    }

    public void setInviteeUser(User inviteeUser) {
        this.inviteeUser = inviteeUser;
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
