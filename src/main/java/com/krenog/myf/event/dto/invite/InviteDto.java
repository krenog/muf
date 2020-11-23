package com.krenog.myf.event.dto.invite;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krenog.myf.event.dto.event.CommonEventDto;
import com.krenog.myf.event.entities.Invite;
import com.krenog.myf.user.dto.user.CommonUserDataDto;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

public class InviteDto {
    @JsonProperty("createdDate")
    @ApiModelProperty(notes = "Дата приглашения")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime createdDate;
    @JsonProperty("user")
    @ApiModelProperty(notes = "Пригласивший пользователь")
    private CommonUserDataDto user;
    @JsonProperty("event")
    @ApiModelProperty(notes = "Событие")
    private CommonEventDto event;

    public InviteDto(Invite invite) {
        this.createdDate = invite.getCreatedDate();
        this.user = new CommonUserDataDto(invite.getUser());
        this.event = new CommonEventDto(invite.getEvent());
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public CommonUserDataDto getUser() {
        return user;
    }

    public void setUser(CommonUserDataDto user) {
        this.user = user;
    }

    public CommonEventDto getEvent() {
        return event;
    }

    public void setEvent(CommonEventDto event) {
        this.event = event;
    }
}
