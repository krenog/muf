package com.krenog.myf.event.entity;

import com.krenog.myf.event.dto.event.CreateEventDto;
import com.krenog.myf.event.entities.Event;
import com.krenog.myf.event.entities.EventMember;
import com.krenog.myf.user.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.krenog.myf.event.utils.EventUtils.getTestCreateEventDto;
import static com.krenog.myf.event.utils.EventUtils.getTestEventWithId;
import static com.krenog.myf.utils.TestUtils.getTestUserWithId;

@Tag("CommonTest")
public class EventTests {
    @Test
    void eventConstructorTest() {
        User user = getTestUserWithId();
        CreateEventDto createEventDto = getTestCreateEventDto();
        Event event = new Event(user, createEventDto);
        Assertions.assertEquals(user.getId(), event.getOwner().getId());
        Assertions.assertEquals(createEventDto.getAddress(), event.getAddress());
        Assertions.assertEquals(createEventDto.getDescription(), event.getDescription());
        Assertions.assertEquals(createEventDto.getEndDate(), event.getEndDate());
        Assertions.assertEquals(createEventDto.getLatitude(), event.getLatitude());
        Assertions.assertEquals(createEventDto.getLongitude(), event.getLongitude());
        Assertions.assertEquals(createEventDto.getName(), event.getName());
        Assertions.assertEquals(createEventDto.getStartDate(), event.getStartDate());
        Assertions.assertEquals(createEventDto.getType(), event.getType());
    }

    @Test
    void eventUpdateTest() {
        Event event = new Event();
        CreateEventDto createEventDto = getTestCreateEventDto();
        event.update(createEventDto);
        Assertions.assertEquals(createEventDto.getAddress(), event.getAddress());
        Assertions.assertEquals(createEventDto.getDescription(), event.getDescription());
        Assertions.assertEquals(createEventDto.getEndDate(), event.getEndDate());
        Assertions.assertEquals(createEventDto.getLatitude(), event.getLatitude());
        Assertions.assertEquals(createEventDto.getLongitude(), event.getLongitude());
        Assertions.assertEquals(createEventDto.getName(), event.getName());
        Assertions.assertEquals(createEventDto.getStartDate(), event.getStartDate());
        Assertions.assertEquals(createEventDto.getType(), event.getType());
    }


    @Test
    void eventAddMemberTest() {
        Event event = getTestEventWithId();
        EventMember eventMember = new EventMember();
        eventMember.setId(1L);
        event.addMember(eventMember);
        Assertions.assertEquals(1, event.getMembers().size());
        Assertions.assertEquals(eventMember.getId(), event.getMembers().get(0).getId());
    }
}
