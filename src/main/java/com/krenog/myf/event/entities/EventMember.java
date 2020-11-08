package com.krenog.myf.event.entities;

import com.krenog.myf.entity.BaseEntity;
import com.krenog.myf.user.entities.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "muf_event_members")
public class EventMember extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "role")
    private MemberRole role = MemberRole.GUEST;

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        super.setUpdateDate(LocalDateTime.now());
    }

    public EventMember() {
    }

    public EventMember(User user, Event event, MemberRole role) {
        this.user = user;
        this.event = event;
        this.role = role;
    }

    public static EventMember createAdmin(User user, Event event) {
        return new EventMember(user, event, MemberRole.ADMIN);
    }

    public static EventMember createGuest(User user, Event event) {
        return new EventMember(user, event, MemberRole.GUEST);
    }

    public static EventMember createOwner(Event event) {
        return new EventMember(event.getOwner(), event, MemberRole.OWNER);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public MemberRole getRole() {
        return role;
    }

    public void setRole(MemberRole role) {
        this.role = role;
    }
}
