package com.krenog.myf.event.service;

import com.krenog.myf.dto.FilterParameters;
import com.krenog.myf.event.entities.Event;
import com.krenog.myf.event.entities.Invite;
import com.krenog.myf.event.exceptions.UserAlreadyInvitedException;
import com.krenog.myf.event.repositories.InviteRepository;
import com.krenog.myf.event.services.invite.InviteData;
import com.krenog.myf.event.services.invite.InviteServiceImpl;
import com.krenog.myf.event.services.member.EventMemberService;
import com.krenog.myf.exceptions.NotFoundException;
import com.krenog.myf.user.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.krenog.myf.event.utils.EventUtils.getTestEventWithId;
import static com.krenog.myf.event.utils.EventUtils.getTestInvite;
import static com.krenog.myf.utils.TestUtils.getTestUserWithId;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
@Tag("CommonTest")
public class MockInviteServiceTests {
    @InjectMocks
    InviteServiceImpl inviteService;
    @Mock
    InviteRepository inviteRepository;
    @Mock
    EventMemberService eventMemberService;

    @Test
    void getUserInvitesTest(){
        List<Invite> inviteList = new ArrayList<>();
        Invite testInvite = getTestInvite();
        inviteList.add(testInvite);
        Mockito.when(inviteRepository.findAllByInvitedUserOrderByCreatedDate(anyLong(),any(Pageable.class))).thenReturn(inviteList);
        List<Invite> returnedInvites = inviteService.getUserInvites(1L,new FilterParameters());
        Mockito.verify(inviteRepository,Mockito.times(1)).findAllByInvitedUserOrderByCreatedDate(anyLong(),any(Pageable.class));
        Assertions.assertEquals(1,returnedInvites.size());
        Assertions.assertEquals(testInvite.getId(),returnedInvites.get(0).getId());

    }
    @Test
    void checkAndCreateInviteTest(){
        Invite testInvite = getTestInvite();
        Mockito.when(inviteRepository.save(any(Invite.class))).thenReturn(testInvite);
        Mockito.when(inviteRepository.existsByInvitedUserAndEvent(any(User.class),any(Event.class))).thenReturn(false);
        inviteService.checkAndCreateInvite(new InviteData(getTestUserWithId(),getTestUserWithId(),getTestEventWithId()));
        Mockito.verify(inviteRepository,Mockito.times(1)).existsByInvitedUserAndEvent(any(User.class),any(Event.class));
        Mockito.verify(inviteRepository,Mockito.times(1)).save(any(Invite.class));
    }
    @Test
    void checkAndCreateInviteTestUserAlreadyInvitedException(){
        Mockito.when(inviteRepository.existsByInvitedUserAndEvent(any(User.class),any(Event.class))).thenReturn(true);
       Throwable throwable = assertThrows(UserAlreadyInvitedException.class,()->{
           inviteService.checkAndCreateInvite(new InviteData(getTestUserWithId(),getTestUserWithId(),getTestEventWithId()));
               });
        Mockito.verify(inviteRepository,Mockito.times(1)).existsByInvitedUserAndEvent(any(User.class),any(Event.class));
        Mockito.verify(inviteRepository,Mockito.times(0)).save(any(Invite.class));
    }

    @Test
    void acceptInviteTest(){
        Invite invite = getTestInvite();
        Mockito.when(inviteRepository.getInviteByIdAndInvitedUserId(anyLong(),anyLong())).thenReturn(Optional.of(invite));
        inviteService.acceptInvite(1L,1L);
        Mockito.verify(inviteRepository,Mockito.times(1)).getInviteByIdAndInvitedUserId(anyLong(),anyLong());
        Mockito.verify(inviteRepository,Mockito.times(1)).save(any(Invite.class));
        Mockito.verify(eventMemberService,Mockito.times(1)).createEventMember(any(Invite.class));
    }

    @Test
    void acceptInviteTestNotFoundException(){
        Mockito.when(inviteRepository.getInviteByIdAndInvitedUserId(anyLong(),anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,()->{inviteService.acceptInvite(1L,1L);});
        Mockito.verify(inviteRepository,Mockito.times(1)).getInviteByIdAndInvitedUserId(anyLong(),anyLong());
        Mockito.verify(inviteRepository,Mockito.times(0)).save(any(Invite.class));
        Mockito.verify(eventMemberService,Mockito.times(0)).createEventMember(any(Invite.class));
    }
    @Test
    void rejectInvite(){
        Invite invite = getTestInvite();
        Mockito.when(inviteRepository.getInviteByIdAndInvitedUserId(anyLong(),anyLong())).thenReturn(Optional.of(invite));
        inviteService.rejectInvite(1L,1L);
        Mockito.verify(inviteRepository,Mockito.times(1)).getInviteByIdAndInvitedUserId(anyLong(),anyLong());
        Mockito.verify(inviteRepository,Mockito.times(1)).save(any(Invite.class));
    }

    @Test
    void rejectInviteNotFoundException(){
        Mockito.when(inviteRepository.getInviteByIdAndInvitedUserId(anyLong(),anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,()->{inviteService.acceptInvite(1L,1L);});
        Mockito.verify(inviteRepository,Mockito.times(1)).getInviteByIdAndInvitedUserId(anyLong(),anyLong());
        Mockito.verify(inviteRepository,Mockito.times(0)).save(any(Invite.class));
    }

}
