package com.krenog.myf.event.controller;

import com.krenog.myf.event.dto.event.*;
import com.krenog.myf.event.entities.Event;
import com.krenog.myf.event.services.event.EventService;
import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.security.detail.UserPrincipal;
import com.krenog.myf.user.services.user.CommonUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/event")
@Api(value = "Сервис событий")
public class EventController {
    private final EventService eventService;
    private final CommonUserService userService;

    public EventController(EventService eventService, CommonUserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "Создание  события")
    public ResponseEntity<EventDto> saveEvent(@RequestBody CreateEventDto createEventDto,
                                              Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userService.getUserById(userPrincipal.getId());
        Event event = eventService.createEvent(createEventDto, user);
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

    @GetMapping(value = "/member")
    @ApiOperation(value = "Получение списка событий")
    public ResponseEntity<List<EventWithMembershipDto>> getEventList(EventFilterParameters eventFilterDto,
                                                                     Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<EventWithMembershipDto> eventsWithMembership = eventService.getUserEventsWithMembership(eventFilterDto, userPrincipal.getId());
        return new ResponseEntity<>(eventsWithMembership, HttpStatus.OK);
    }

    @PostMapping(value = "/nearest")
    @ApiOperation(value = "Получение ближайших событий по локации")
    private ResponseEntity<List<EventDto>> findNearestEvents(@RequestBody LocationDto locationDto){
        List<Event> events = eventService.getNearestEventsByUserLocation(locationDto.getLatitude(),locationDto.getLongitude());
        List<EventDto> eventDtoList = events.stream().map(EventDto::new).collect(Collectors.toList());
        return new ResponseEntity<>(eventDtoList,HttpStatus.OK);
    }

    @PostMapping(value = "{id}/leave")
    @ApiOperation(value = "Получение ближайших событий по локации")
    private ResponseEntity<String> findNearestEvents(@ApiParam(value = "Event Id", required = true) @PathVariable(name = "id") Long eventId,
                                                      Authentication authentication){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        eventService.leaveEvent(userPrincipal.getId(),eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
