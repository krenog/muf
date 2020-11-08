package com.krenog.myf.event.entities;

import com.krenog.myf.entity.BaseEntity;
import com.krenog.myf.event.dto.event.CreateEventDto;
import com.krenog.myf.user.entities.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "muf_event")
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

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @Column(name = "type")
    private EventType type = EventType.PUBLIC;

    @Column(name = "status")
    private EventStatus status = EventStatus.CREATED;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
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
    }

    public void update(CreateEventDto createEventDto) {
        this.name = createEventDto.getName();
        this.description = createEventDto.getDescription();
        this.address = createEventDto.getAddress();
        this.latitude = createEventDto.getLatitude();
        this.longitude = createEventDto.getLongitude();
        this.type = createEventDto.getType();
        this.startDate = createEventDto.getStartDate();
        this.endDate = createEventDto.getEndDate();
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

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
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
}
