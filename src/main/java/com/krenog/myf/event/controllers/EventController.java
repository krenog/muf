package com.krenog.myf.event.controllers;

import com.krenog.myf.event.dto.event.EventDto;
import com.krenog.myf.event.dto.event.EventFilterParameters;
import com.krenog.myf.event.dto.event.EventWithMembershipDto;
import com.krenog.myf.event.dto.event.CreateEventDto;
import com.krenog.myf.event.entities.Event;
import com.krenog.myf.event.services.event.EventService;
import com.krenog.myf.user.security.detail.UserPrincipal;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event")
@Api(value = "Сервис событий")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "Создание  события")
    public ResponseEntity<EventDto> saveEvent(@RequestBody CreateEventDto createEventDto,
                                              Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Event event = eventService.createEvent(createEventDto, userPrincipal.getId());
        return new ResponseEntity<>(new EventDto(event), HttpStatus.CREATED);
    }

    @PutMapping(value = "/save/{id}")
    @ApiOperation(value = "Обновление  события")
    public ResponseEntity<EventDto> updateEvent(@ApiParam(value = "Event Id", required = true) @PathVariable(name = "id") Long eventId,
                                                @RequestBody CreateEventDto createEventDto,
                                                Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Event event = eventService.updateEvent(eventId, createEventDto, userPrincipal.getId());
        return new ResponseEntity<>(new EventDto(event), HttpStatus.OK);
    }

    @GetMapping(value = "")
    @ApiOperation(value = "Получение списка событий")
    public ResponseEntity<List<EventWithMembershipDto>> getEventList(EventFilterParameters eventFilterDto,
                                                                     Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<EventWithMembershipDto> eventsWithMembership = eventService.getUserEventsWithMembership(eventFilterDto, userPrincipal.getId());
        return new ResponseEntity<>(eventsWithMembership, HttpStatus.OK);
    }
}
