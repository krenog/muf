package com.krenog.myf.event.utils;

import com.krenog.myf.event.dto.event.CreateEventDto;
import com.krenog.myf.event.dto.event.EventWithMembershipDto;
import com.krenog.myf.event.entities.Event;
import com.krenog.myf.event.entities.EventMember;
import com.krenog.myf.event.entities.Invite;
import com.krenog.myf.event.entities.MemberRole;
import com.krenog.myf.user.entities.User;
import com.krenog.myf.utils.LocationUtils;

import java.time.LocalDateTime;

import static com.krenog.myf.utils.TestUtils.getTestUserWithId;

public class EventUtils {
    private static final String TEST_EVENT_NAME = "names";
    private static final Long TEST_EVENT_ID = 1L;
    private static final String TEST_EVENT_DESCRIPTION = "description";
    private static final String TEST_EVENT_ADDRESS = "address";
    private static final Float TEST_EVENT_LATITUDE = 1.0f;
    private static final Float TEST_EVENT_LONGITUDE = 2.0f;
    private static final LocalDateTime TEST_EVENT_START_DATE = LocalDateTime.now();
    private static final LocalDateTime TEST_EVENT_END_DATE = LocalDateTime.now();

    public static EventWithMembershipDto getTestEventWithMembershipDto(){
        return new EventWithMembershipDto(getTestEventWithId(), MemberRole.GUEST);
    }
    public static Event getTestEventWithId() {
        Event event = getTestEvent();
        event.setId(TEST_EVENT_ID);
        return event;
    }

    public static Event getTestEvent() {
        Event event = new Event();
        event.setName(TEST_EVENT_NAME);
        event.setAddress(TEST_EVENT_ADDRESS);
        event.setDescription(TEST_EVENT_DESCRIPTION);
        event.setPoint(LocationUtils.buildPoint(TEST_EVENT_LATITUDE, TEST_EVENT_LONGITUDE));
        event.setStartDate(TEST_EVENT_START_DATE);
        event.setEndDate(TEST_EVENT_END_DATE);
        event.setOwner(getTestUserWithId());
        return event;
    }

    public static CreateEventDto getTestCreateEventDto() {
        CreateEventDto createEventDto = new CreateEventDto();
        createEventDto.setName(TEST_EVENT_NAME);
        createEventDto.setDescription(TEST_EVENT_DESCRIPTION);
        createEventDto.setAddress(TEST_EVENT_ADDRESS);
        createEventDto.setLatitude(TEST_EVENT_LATITUDE);
        createEventDto.setLongitude(TEST_EVENT_LONGITUDE);
        createEventDto.setStartDate(TEST_EVENT_START_DATE);
        createEventDto.setEndDate(TEST_EVENT_END_DATE);
        return createEventDto;
    }

    public static EventMember getTestEventMember(){
        EventMember eventMember = new EventMember();
        eventMember.setId(TEST_EVENT_ID);
        eventMember.setEvent(getTestEventWithId());
        eventMember.setUser(getTestUserWithId());
        return eventMember;
    }

    public static Invite getTestInvite(){
        Invite invite = new Invite();
        invite.setEvent(getTestEventWithId());
        invite.setUser(getTestUserWithId());

        User invUser = getTestUserWithId();
        invUser.setId(2L);
        invite.setInvitedUser(invUser);
        return invite;
    }

}
