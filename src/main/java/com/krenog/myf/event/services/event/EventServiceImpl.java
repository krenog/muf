package com.krenog.myf.event.services.event;

import com.krenog.myf.event.dto.event.CreateEventDto;
import com.krenog.myf.event.dto.event.EventFilterParameters;
import com.krenog.myf.event.dto.event.EventWithMembershipDto;
import com.krenog.myf.event.entities.Event;
import com.krenog.myf.event.entities.EventMember;
import com.krenog.myf.event.repositories.EventRepository;
import com.krenog.myf.event.services.member.EventMemberService;
import com.krenog.myf.event.services.member.MembershipFilter;
import com.krenog.myf.exceptions.NotFoundException;
import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.services.user.CommonUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CommonUserService userService;
    private final EventMemberService eventMemberService;

    public EventServiceImpl(EventRepository eventRepository, CommonUserService userService, EventMemberService eventMemberService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.eventMemberService = eventMemberService;
    }

    @Override
    public Event createEvent(CreateEventDto createEventDto, Long userId) {
        User user = userService.getUserById(userId);
        Event event = new Event(user, createEventDto);
        return saveEvent(event);
    }

    @Override
    public Event updateEvent(Long eventId, CreateEventDto createEventDto, Long userId) {
        Event event = getByEventByIdAndUserId(eventId, userId);
        event.update(createEventDto);
        return saveEvent(event);
    }

    @Override
    public List<EventWithMembershipDto> getUserEventsWithMembership(EventFilterParameters eventFilterDto, Long userId) {
        List<EventMember> memberships = getMemberships(eventFilterDto, userId);
        return memberships.stream().map(x -> new EventWithMembershipDto(x.getEvent(), x.getRole())).collect(Collectors.toList());
    }

    private List<EventMember> getMemberships(EventFilterParameters eventFilterDto, Long userId) {
        MembershipFilter filter = new MembershipFilter(userId, eventFilterDto);
        return eventMemberService.getMemberships(filter);
    }


    private Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    private Event getByEventByIdAndUserId(Long eventId, Long userId) {
        return eventRepository.getEventByIdAndOwnerId(eventId, userId)
                .orElseThrow(
                        () -> new NotFoundException("Event not found userId " + userId.toString() + " eventId " + eventId.toString())
                );
    }

    @Override
    public Event getById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(
                        () -> new NotFoundException("Event not found eventId " + eventId.toString())
                );
    }
}
