package com.krenog.myf.event.controller;

import com.krenog.myf.event.controller.exceptions.EventExceptionHandler;
import com.krenog.myf.event.dto.event.CreateEventDto;
import com.krenog.myf.event.dto.event.EventFilterParameters;
import com.krenog.myf.event.dto.event.EventWithMembershipDto;
import com.krenog.myf.event.dto.event.LocationDto;
import com.krenog.myf.event.entities.Event;
import com.krenog.myf.event.entities.MemberRole;
import com.krenog.myf.event.services.event.EventService;
import com.krenog.myf.exceptions.NotFoundException;
import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.services.user.CommonUserService;
import com.krenog.myf.utils.AuthenticationForTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.krenog.myf.event.utils.EventUtils.*;
import static com.krenog.myf.utils.TestConverter.checkTestErrorCode;
import static com.krenog.myf.utils.TestConverter.mapToJson;
import static com.krenog.myf.utils.TestUtils.getTestUserWithId;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@Tag("CommonTest")
public class MockEventControllerTests {
    private MockMvc mockMvc;
    @InjectMocks
    EventController eventController;
    @Mock
    EventService eventService;
    @Mock
    CommonUserService userService;
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(eventController)
                .setControllerAdvice(new EventExceptionHandler())
                .build();
    }

    @Test
    public void saveEvent_thenReturnOK()
            throws Exception {
        Event testEvent = getTestEventWithId();
        User user = getTestUserWithId();
        Mockito.when(eventService.createEvent(any(CreateEventDto.class),any(User.class))).thenReturn(testEvent);
        Mockito.when(userService.getUserById(anyLong())).thenReturn(user);

        mockMvc.perform(post("/api/v1/event/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(getTestCreateEventDto()))
            .principal(new AuthenticationForTest()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(testEvent.getId().intValue())))
                .andExpect(jsonPath("$.status", is(0)));
        Mockito.verify(eventService,Mockito.times(1)).createEvent(any(CreateEventDto.class),any(User.class));
        Mockito.verify(userService,Mockito.times(1)).getUserById(anyLong());
    }

    @Test
    public void updateEvent_thenReturnOK()
            throws Exception {
        Event testEvent = getTestEventWithId();
        Mockito.when(eventService.updateEvent(anyLong(),any(CreateEventDto.class),anyLong())).thenReturn(testEvent);

        mockMvc.perform(put("/api/v1/event/save/{id}",testEvent.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(getTestCreateEventDto()))
                .principal(new AuthenticationForTest()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testEvent.getId().intValue())))
                .andExpect(jsonPath("$.status", is(0)));
        Mockito.verify(eventService,Mockito.times(1)).updateEvent(anyLong(),any(CreateEventDto.class),anyLong());
    }

    @Test
    public void updateEvent_thenReturnNotFound()
            throws Exception {
        Event testEvent = getTestEventWithId();
        Mockito.when(eventService.updateEvent(anyLong(),any(CreateEventDto.class),anyLong())).thenThrow(NotFoundException.class);

        MvcResult result = mockMvc.perform(put("/api/v1/event/save/{id}",testEvent.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(getTestCreateEventDto()))
                .principal(new AuthenticationForTest()))
                .andExpect(status().isNotFound()).andReturn();
        checkTestErrorCode(result, "not_found");
        Mockito.verify(eventService,Mockito.times(1)).updateEvent(anyLong(),any(CreateEventDto.class),anyLong());
    }

    @Test
    public void getEventList_thenReturnOK()
            throws Exception {
        EventWithMembershipDto testEventWithMembershipDto = getTestEventWithMembershipDto();
        List<EventWithMembershipDto> eventList = new ArrayList<EventWithMembershipDto>(){{
            add(testEventWithMembershipDto);
        }};
        Mockito.when(eventService.getUserEventsWithMembership(any(EventFilterParameters.class),anyLong())).thenReturn(eventList);

        mockMvc.perform(get("/api/v1/event/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(getTestCreateEventDto()))
                .principal(new AuthenticationForTest()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].role", is(MemberRole.GUEST.getValue())));
        Mockito.verify(eventService,Mockito.times(1)).getUserEventsWithMembership(any(EventFilterParameters.class),anyLong());
    }

    @Test
    public void getNearestEvent_thenReturnOK()
            throws Exception {
        Event testEventWithId = getTestEventWithId();
        List<Event> events = new ArrayList<Event>(){{
            add(testEventWithId);
        }};
        Mockito.when(eventService.getNearestEventsByUserLocation(anyFloat(),anyFloat())).thenReturn(events);

        mockMvc.perform(post("/api/v1/event/nearest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(new LocationDto(1f,1f)))
                .principal(new AuthenticationForTest()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(testEventWithId.getId().intValue())));
        Mockito.verify(eventService,Mockito.times(1)).getNearestEventsByUserLocation(anyFloat(),anyFloat());
    }

    @Test
    public void leaveEvent_thenReturnOK()
            throws Exception {

        mockMvc.perform(post("/api/v1/event/{id}/leave",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(new AuthenticationForTest()))
                .andExpect(status().isOk());
        Mockito.verify(eventService,Mockito.times(1)).leaveEvent(anyLong(),anyLong());
    }
}
