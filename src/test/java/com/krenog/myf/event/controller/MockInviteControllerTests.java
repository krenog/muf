package com.krenog.myf.event.controller;

import com.krenog.myf.dto.FilterParameters;
import com.krenog.myf.event.controller.exceptions.EventExceptionHandler;
import com.krenog.myf.event.dto.event.CreateEventDto;
import com.krenog.myf.event.dto.event.EventFilterParameters;
import com.krenog.myf.event.dto.event.EventWithMembershipDto;
import com.krenog.myf.event.dto.invite.CreateInviteDto;
import com.krenog.myf.event.entities.Event;
import com.krenog.myf.event.entities.Invite;
import com.krenog.myf.event.entities.MemberRole;
import com.krenog.myf.event.services.event.EventService;
import com.krenog.myf.event.services.invite.InviteService;
import com.krenog.myf.exceptions.NotFoundException;
import com.krenog.myf.user.services.sms.exceptions.SendSmsException;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@Tag("CommonTest")
public class MockInviteControllerTests {
    private MockMvc mockMvc;
    @InjectMocks
    InviteController inviteController;
    @Mock
    private InviteService inviteService;
    @Mock
    private CommonUserService userService;
    @Mock
    private EventService eventService;
    static  final  CreateInviteDto CREATE_INVITE_DTO;

    static {
        CREATE_INVITE_DTO = new CreateInviteDto();
        CREATE_INVITE_DTO.setEventId(1L);
        CREATE_INVITE_DTO.setInvitedUserId(1L);
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(inviteController)
                .setControllerAdvice(new EventExceptionHandler())
                .build();
    }

    @Test
    public void getInvites_thenReturnOK()
            throws Exception {
        Invite testInvite = getTestInvite();
        List<Invite> inviteList = new ArrayList<Invite>(){{
            add(testInvite);
        }};
        Mockito.when(inviteService.getUserInvites(anyLong(),any(FilterParameters.class))).thenReturn(inviteList);

        mockMvc.perform(get("/api/v1/invite/")
                .contentType(MediaType.APPLICATION_JSON)
                .principal(new AuthenticationForTest()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user.id", is(testInvite.getUser().getId().intValue())))
                .andExpect(jsonPath("$[0].event.id", is(testInvite.getEvent().getId().intValue())));
        Mockito.verify(inviteService,Mockito.times(1)).getUserInvites(anyLong(),any(FilterParameters.class));
    }

    @Test
    public void sendInvite_thenReturnOK()
            throws Exception {
        Event testEvent = getTestEventWithId();
        Mockito.when(eventService.getById(anyLong())).thenReturn(testEvent);
        Mockito.when(userService.getUserById(anyLong())).thenReturn(getTestUserWithId());

        mockMvc.perform(post("/api/v1/invite/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(CREATE_INVITE_DTO))
                .principal(new AuthenticationForTest()))
                .andExpect(status().isCreated());
        Mockito.verify(eventService,Mockito.times(1)).getById(anyLong());
        Mockito.verify(userService,Mockito.times(2)).getUserById(anyLong());
    }

    @Test
    public void sendInvite_thenReturnNotFoundExceptionEvent()
            throws Exception {
        Mockito.when(eventService.getById(anyLong())).thenThrow(NotFoundException.class);
        Mockito.when(userService.getUserById(anyLong())).thenReturn(getTestUserWithId());

        MvcResult result = mockMvc.perform(post("/api/v1/invite/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(CREATE_INVITE_DTO))
                .principal(new AuthenticationForTest()))
                .andExpect(status().isNotFound()).andReturn();
        checkTestErrorCode(result, "not_found");
    }

    @Test
    public void sendInvite_thenReturnNotFoundExceptionUser()
            throws Exception {
        Event testEvent = getTestEventWithId();
        Mockito.when(userService.getUserById(anyLong())).thenThrow(NotFoundException.class);

        MvcResult result = mockMvc.perform(post("/api/v1/invite/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(CREATE_INVITE_DTO))
                .principal(new AuthenticationForTest()))
                .andExpect(status().isNotFound()).andReturn();
        checkTestErrorCode(result, "not_found");
    }

    @Test
    public void acceptInvite_thenReturnOK()
            throws Exception {
        mockMvc.perform(post("/api/v1/invite/{id}/accept",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(new AuthenticationForTest()))
                .andExpect(status().isOk());
        Mockito.verify(inviteService,Mockito.times(1)).acceptInvite(anyLong(),anyLong());
    }

    @Test
    public void acceptInvite_thenReturnNotFoundException()
            throws Exception {
        doThrow(NotFoundException.class).when(inviteService).acceptInvite(anyLong(),anyLong());
        MvcResult result =  mockMvc.perform(post("/api/v1/invite/{id}/accept",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(new AuthenticationForTest()))
                .andExpect(status().isNotFound()).andReturn();
        checkTestErrorCode(result, "not_found");
    }

    @Test
    public void rejectInvite_thenReturnOK()
            throws Exception {
        mockMvc.perform(post("/api/v1/invite/{id}/reject",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(new AuthenticationForTest()))
                .andExpect(status().isOk());
        Mockito.verify(inviteService,Mockito.times(1)).rejectInvite(anyLong(),anyLong());
    }

    @Test
    public void rejectInvite_thenReturnNotFoundException()
            throws Exception {
        doThrow(NotFoundException.class).when(inviteService).rejectInvite(anyLong(),anyLong());
        MvcResult result =  mockMvc.perform(post("/api/v1/invite/{id}/reject",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(new AuthenticationForTest()))
                .andExpect(status().isNotFound()).andReturn();
        checkTestErrorCode(result, "not_found");
    }

}
