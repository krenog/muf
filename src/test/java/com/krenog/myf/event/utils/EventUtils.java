package com.krenog.myf.event.utils;

import com.krenog.myf.event.dto.event.CreateEventDto;
import com.krenog.myf.event.entities.Event;

import java.time.LocalDateTime;

public class EventUtils {
    private static final String TEST_EVENT_NAME = "name";
    private static final Long TEST_EVENT_ID = 1L;
    private static final String TEST_EVENT_DESCRIPTION = "description";
    private static final String TEST_EVENT_ADDRESS = "address";
    private static final Float TEST_EVENT_LATITUDE = 1.0f;
    private static final Float TEST_EVENT_LONGITUDE = 2.0f;
    private static final LocalDateTime TEST_EVENT_START_DATE = LocalDateTime.now();
    private static final LocalDateTime TEST_EVENT_END_DATE = LocalDateTime.now();

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
        event.setLatitude(TEST_EVENT_LATITUDE);
        event.setLongitude(TEST_EVENT_LONGITUDE);
        event.setStartDate(TEST_EVENT_START_DATE);
        event.setEndDate(TEST_EVENT_END_DATE);
        return event;
    }

    public static CreateEventDto getTestCreateEventDto() {
        CreateEventDto createEventDto = new CreateEventDto();
        createEventDto.setName(TEST_EVENT_NAME);
        createEventDto.setAddress(TEST_EVENT_ADDRESS);
        createEventDto.setDescription(TEST_EVENT_DESCRIPTION);
        createEventDto.setLatitude(TEST_EVENT_LATITUDE);
        createEventDto.setLongitude(TEST_EVENT_LONGITUDE);
        createEventDto.setStartDate(TEST_EVENT_START_DATE);
        createEventDto.setEndDate(TEST_EVENT_END_DATE);
        return createEventDto;
    }


}
