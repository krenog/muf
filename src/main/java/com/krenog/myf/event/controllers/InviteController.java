package com.krenog.myf.event.controllers;

import com.krenog.myf.dto.FilterParameters;
import com.krenog.myf.event.dto.invite.CreateInviteDto;
import com.krenog.myf.event.dto.invite.InviteDto;
import com.krenog.myf.event.entities.Invite;
import com.krenog.myf.event.services.event.EventService;
import com.krenog.myf.event.services.invite.InviteData;
import com.krenog.myf.event.services.invite.InviteService;
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
@RequestMapping("/api/v1/invite")
@Api(value = "Сервис приглашений")
public class InviteController {
    private final InviteService inviteService;
    private final CommonUserService userService;
    private final EventService eventService;

    public InviteController(InviteService inviteService, CommonUserService userService, EventService eventService) {
        this.inviteService = inviteService;
        this.userService = userService;
        this.eventService = eventService;
    }

    @GetMapping(value = "")
    @ApiOperation(value = "Получить список приглашений")
    public ResponseEntity<List<InviteDto>> saveEvent(FilterParameters filterParameters,
                                                     Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<Invite> invites = inviteService.getUserInvites(userPrincipal.getId(), filterParameters);
        List<InviteDto> dtoList = invites.stream().map(InviteDto::new).collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping(value = "/send")
    @ApiOperation(value = "Получить список приглашений")
    public ResponseEntity<String> sendInvite(@RequestBody CreateInviteDto createInviteDto,
                                             Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        InviteData inviteData = new InviteData(
                userService.getUserById(userPrincipal.getId()),
                userService.getUserById(createInviteDto.getInvitedUserId()),
                eventService.getById(createInviteDto.getEventId())

        );
        inviteService.checkAndCreateInvite(inviteData);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/accept/{id}")
    @ApiOperation(value = "Принять приглашение")
    public ResponseEntity<String> acceptInvite(@ApiParam(value = "Invite Id", required = true) @PathVariable(name = "id") Long inviteId,
                                               Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        inviteService.acceptInvite(userPrincipal.getId(), inviteId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/reject/{id}")
    @ApiOperation(value = "Принять приглашение")
    public ResponseEntity<String> rejectInvite(@ApiParam(value = "Invite Id", required = true) @PathVariable(name = "id") Long inviteId,
                                               Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        inviteService.rejectInvite(userPrincipal.getId(), inviteId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
