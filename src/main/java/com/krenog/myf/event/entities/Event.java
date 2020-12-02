package com.krenog.myf.event.entities;

import com.krenog.myf.entity.BaseEntity;
import com.krenog.myf.event.dto.event.CreateEventDto;
import com.krenog.myf.user.entities.User;
import com.krenog.myf.utils.LocationUtils;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "myf_event")
public class Event extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "point")
    private Point point;

    @Column(name = "type")
    private EventType type = EventType.PUBLIC;

    @Column(name = "status")
    private EventStatus status = EventStatus.CREATED;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private List<EventMember> members = new ArrayList<>();

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        super.setUpdateDate(LocalDateTime.now());
    }

    public Event() {
    }

    public Event(User owner, CreateEventDto createEventDto) {
        this.update(createEventDto);
        this.owner = owner;
        addOwnerMember();
    }

    public void update(CreateEventDto createEventDto) {
        this.name = createEventDto.getName();
        this.description = createEventDto.getDescription();
        this.address = createEventDto.getAddress();
        this.point = LocationUtils.buildPoint(createEventDto.getLatitude(), createEventDto.getLongitude());
        this.type = createEventDto.getType();
        this.startDate = createEventDto.getStartDate();
        this.endDate = createEventDto.getEndDate();
    }

    private void addOwnerMember() {
        EventMember member = EventMember.createOwner(this);
        this.addMember(member);
    }

    public void addMember(EventMember member) {
        members.add(member);
    }


    public List<EventMember> getMembers() {
        return members;
    }

    public void setMembers(List<EventMember> members) {
        this.members = members;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getLatitude() {
        return (float) point.getX();
    }

    public Float getLongitude() {
        return (float) point.getY();
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(owner, event.owner) &&
                Objects.equals(name, event.name) &&
                Objects.equals(description, event.description) &&
                Objects.equals(address, event.address) &&
                Objects.equals(point, event.point) &&
                type == event.type &&
                status == event.status &&
                Objects.equals(startDate, event.startDate) &&
                Objects.equals(endDate, event.endDate) &&
                Objects.equals(members, event.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, name, description, address, point, type, status, startDate, endDate, members);
    }
}
