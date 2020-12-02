package com.krenog.myf.event.services.event;

import com.krenog.myf.event.dto.event.CreateEventDto;
import com.krenog.myf.event.dto.event.EventFilterParameters;
import com.krenog.myf.event.dto.event.EventWithMembershipDto;
import com.krenog.myf.event.entities.Event;
import com.krenog.myf.user.entities.User;

import java.util.List;

public interface EventService {
    Event createEvent(CreateEventDto createEventDto, User user);

    Event updateEvent(Long eventId, CreateEventDto createEventDto, Long userId);

    List<EventWithMembershipDto> getUserEventsWithMembership(EventFilterParameters eventFilterDto, Long userId);

    Event getById(Long eventId);

    List<Event> getNearestEventsByUserLocation(Float latitude,Float longitude);

    void leaveEvent(Long userId,Long eventId);
}
