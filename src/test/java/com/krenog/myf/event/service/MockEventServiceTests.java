package com.krenog.myf.event.service;

import com.krenog.myf.event.dto.event.EventFilterParameters;
import com.krenog.myf.event.dto.event.EventWithMembershipDto;
import com.krenog.myf.event.entities.Event;
import com.krenog.myf.event.entities.EventMember;
import com.krenog.myf.event.entities.MemberRole;
import com.krenog.myf.event.repositories.EventRepository;
import com.krenog.myf.event.services.event.EventServiceImpl;
import com.krenog.myf.event.services.member.EventMemberService;
import com.krenog.myf.event.services.member.MembershipFilter;
import com.krenog.myf.exceptions.NotFoundException;
import com.krenog.myf.user.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Polygon;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.krenog.myf.event.utils.EventUtils.*;
import static com.krenog.myf.utils.TestUtils.getTestUserWithId;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
@Tag("CommonTest")
public class MockEventServiceTests {
    @InjectMocks
    EventServiceImpl eventService;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventMemberService eventMemberService;

    @Test
    void createEventTest(){
        User testUser = getTestUserWithId();
        Event testEvent = getTestEventWithId();
        Mockito.when(eventRepository.save(any(Event.class)))
                .thenReturn(testEvent);
        Event event = eventService.createEvent(getTestCreateEventDto(),testUser);
        Mockito.verify(eventRepository, Mockito.times(1)).save(any());
        Assertions.assertEquals(testEvent, event);
    }

    @Test
    void updateEventTest(){
        User testUser = getTestUserWithId();
        Event testEvent = getTestEventWithId();
        Mockito.when(eventRepository.getEventByIdAndOwnerId(anyLong(),anyLong()))
                .thenReturn(Optional.of(testEvent));
        Mockito.when(eventRepository.save(any(Event.class)))
                .thenReturn(testEvent);
        Event event = eventService.updateEvent(testEvent.getId(),getTestCreateEventDto(),testUser.getId());
        Mockito.verify(eventRepository, Mockito.times(1)).save(any());
        Assertions.assertEquals(testEvent, event);
    }

    @Test
    void updateEventTestNotFoundException(){
        User testUser = getTestUserWithId();
        Event testEvent = getTestEventWithId();
        Mockito.when(eventRepository.getEventByIdAndOwnerId(anyLong(),anyLong()))
                .thenReturn(Optional.empty());
        Throwable exception = assertThrows(NotFoundException.class,
                () -> eventService.updateEvent(testEvent.getId(),getTestCreateEventDto(),testUser.getId())
        );
        Mockito.verify(eventRepository, Mockito.times(0)).save(any());
        Assertions.assertEquals("Event not found userId " + testUser.getId().toString() + " eventId " + testEvent.getId().toString(), exception.getMessage());
    }

    @Test
    void getUserEventsWithMembershipTest(){
        List<EventMember> eventMembers = new ArrayList<>();
        EventMember eventMember = getTestEventMember();
        eventMembers.add(eventMember);
        Mockito.when(eventMemberService.getMemberships(any(MembershipFilter.class))).thenReturn(eventMembers);
        List<EventWithMembershipDto> eventWithMembershipDtoList = eventService.getUserEventsWithMembership(new EventFilterParameters(), 1L);
        Mockito.verify(eventMemberService, Mockito.times(1)).getMemberships(any(MembershipFilter.class));
        Assertions.assertEquals(1, eventWithMembershipDtoList.size());
        EventWithMembershipDto returnedDto = eventWithMembershipDtoList.get(0);
        Assertions.assertEquals(new EventWithMembershipDto(eventMember.getEvent(),MemberRole.GUEST), returnedDto);
    }

    @Test
    void getNearestEventTest(){
        Event testEventWithId = getTestEventWithId();
        List<Event> events = new ArrayList<Event>(){{
            add(testEventWithId);
        }};
        Mockito.when(eventRepository.getNearestAllEvent(any(Polygon.class))).thenReturn(events);
        List<Event> returnedEvents = eventService.getNearestEventsByUserLocation(1f,2f);
        Assertions.assertEquals(1, returnedEvents.size());
        Assertions.assertEquals(testEventWithId,returnedEvents.get(0));
    }

    @Test
    void getByIdTest(){
        Event testEvent= getTestEventWithId();
        Mockito.when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));
        Event returnedEvent = eventService.getById(1L);
        Assertions.assertEquals(testEvent,returnedEvent);
    }

    @Test
    void getByIdTestNotFound(){
        Mockito.when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(NotFoundException.class,()->{
            eventService.getById(1L);
        });
        Assertions.assertEquals("Event not found eventId 1",throwable.getMessage());
    }

    @Test
    void leaveEventTest(){
        eventService.leaveEvent(1L,1L);
        Mockito.verify(eventMemberService, Mockito.times(1)).deleteMembership(anyLong(),anyLong());

    }
}
