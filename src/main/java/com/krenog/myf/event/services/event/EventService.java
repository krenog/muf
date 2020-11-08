package com.krenog.myf.event.services.event;

import com.krenog.myf.event.dto.event.CreateEventDto;
import com.krenog.myf.event.entities.Event;

public interface EventService {
    Event createEvent(CreateEventDto createEventDto, Long userId);

    Event updateEvent(Long eventId, CreateEventDto createEventDto, Long userId);
}
