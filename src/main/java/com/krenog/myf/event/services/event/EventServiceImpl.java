package com.krenog.myf.event.services.event;

import com.krenog.myf.event.dto.event.CreateEventDto;
import com.krenog.myf.event.entities.Event;
import com.krenog.myf.event.entities.EventMember;
import com.krenog.myf.event.repositories.EventRepository;
import com.krenog.myf.exceptions.NotFoundException;
import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.services.user.CommonUserService;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CommonUserService userService;

    public EventServiceImpl(EventRepository eventRepository, CommonUserService userService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
    }

    @Override
    public Event createEvent(CreateEventDto createEventDto, Long userId) {
        User user = userService.getUserById(userId);
        Event event = buildEvent(user, createEventDto);
        return saveEvent(event);
    }

    private Event buildEvent(User user, CreateEventDto createEventDto) {
        Event event = new Event(user, createEventDto);
        addOwnerMember(event);
        return event;
    }

    private void addOwnerMember(Event event) {
        EventMember member = EventMember.createOwner(event);
        event.addMember(member);
    }

    @Override
    public Event updateEvent(Long eventId, CreateEventDto createEventDto, Long userId) {
        Event event = getByEventIdAndUserId(eventId, userId);
        event.update(createEventDto);
        return saveEvent(event);
    }

    private Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    private Event getByEventIdAndUserId(Long eventId, Long userId) {
        return eventRepository.getEventByIdAndOwnerId(eventId, userId)
                .orElseThrow(
                        () -> new NotFoundException("Event not found userId " + userId.toString() + " eventId " + eventId.toString())
                );
    }
}
