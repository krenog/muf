package com.krenog.myf.event.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krenog.myf.event.entities.Event;
import com.krenog.myf.event.entities.EventStatus;
import com.krenog.myf.event.entities.EventType;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

public class EventDto extends CommonEventDto {
    @JsonProperty("createdDate")
    @ApiModelProperty(notes = "Дата создания")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime createdDate;
    @JsonProperty("description")
    @ApiModelProperty(notes = "Описание")
    private String description;
    @JsonProperty("address")
    @ApiModelProperty(notes = "Адрес")
    private String address;
    @JsonProperty("latitude")
    @ApiModelProperty(notes = "Широта")
    private Float latitude;
    @JsonProperty("longitude")
    @ApiModelProperty(notes = "Долгота")
    private Float longitude;
    @JsonProperty("type")
    @ApiModelProperty(notes = "Тип события", dataType = "Integer", allowableValues = "0 - public, 1 - private")
    private EventType type = EventType.PUBLIC;
    @JsonProperty("startDate")
    @ApiModelProperty(notes = "Дата и время начала события")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime startDate;
    @JsonProperty("endDate")
    @ApiModelProperty(notes = "Дата и время конца события")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime endDate;
    @JsonProperty("status")
    @ApiModelProperty(notes = "Стаутс события", dataType = "Integer", allowableValues = "0 - Created, 1 - Active, 2 - Canceled, 3 - passed")
    private EventStatus status;

    public EventDto(Event event) {
        super(event);
        this.createdDate = event.getCreatedDate();
        this.description = event.getDescription();
        this.address = event.getAddress();
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.type = event.getType();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
        this.status = event.getStatus();
    }

    public EventDto() {
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }
}
