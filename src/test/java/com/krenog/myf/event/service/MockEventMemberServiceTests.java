package com.krenog.myf.event.service;

import com.krenog.myf.event.entities.EventMember;
import com.krenog.myf.event.entities.Invite;
import com.krenog.myf.event.entities.MemberRole;
import com.krenog.myf.event.repositories.EventMemberRepository;
import com.krenog.myf.event.services.member.EventMemberServiceImpl;
import com.krenog.myf.event.services.member.MembershipFilter;
import com.krenog.myf.event.services.member.exceptions.MemberAlreadyExistException;
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

import static com.krenog.myf.event.utils.EventUtils.getTestEventMember;
import static com.krenog.myf.event.utils.EventUtils.getTestInvite;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
@Tag("CommonTest")
public class MockEventMemberServiceTests {
    @InjectMocks
    private EventMemberServiceImpl eventMemberService;
    @Mock
    private EventMemberRepository eventMemberRepository;

    @Test
    void getMembershipsWithRoleTest(){
        MembershipFilter membershipFilter = new MembershipFilter(1L,10,5, MemberRole.GUEST);
        List<EventMember> eventMembers = new ArrayList<>();
        EventMember eventMember = getTestEventMember();
        eventMembers.add(eventMember);
        Mockito.when(eventMemberRepository.findByUserIdAndRoleOrderByCreatedDate(anyLong(),eq(MemberRole.GUEST),any(Pageable.class))).thenReturn(eventMembers);
        List<EventMember> returned = eventMemberService.getMemberships(membershipFilter);
        Mockito.verify(eventMemberRepository, Mockito.times(1)).findByUserIdAndRoleOrderByCreatedDate(anyLong(),any(MemberRole.class),any(Pageable.class));
        Mockito.verify(eventMemberRepository, Mockito.times(0)).findByUserIdOrderByCreatedDate(anyLong(),any(Pageable.class));
    }

    @Test
    void getMembershipsTest(){
        MembershipFilter membershipFilter = new MembershipFilter(1L,10,5, null);
        List<EventMember> eventMembers = new ArrayList<>();
        EventMember eventMember = getTestEventMember();
        eventMembers.add(eventMember);
        Mockito.when(eventMemberRepository.findByUserIdOrderByCreatedDate(anyLong(),any(Pageable.class))).thenReturn(eventMembers);
        List<EventMember> returned = eventMemberService.getMemberships(membershipFilter);
        Mockito.verify(eventMemberRepository, Mockito.times(0)).findByUserIdAndRoleOrderByCreatedDate(anyLong(),any(MemberRole.class),any(Pageable.class));
        Mockito.verify(eventMemberRepository, Mockito.times(1)).findByUserIdOrderByCreatedDate(anyLong(),any(Pageable.class));
    }

    @Test
    void createEventMember(){
        EventMember eventMember = getTestEventMember();
        Invite invite = getTestInvite();
        Mockito.when(eventMemberRepository.existsByEventAndUser(eq(invite.getEvent()),eq(invite.getInvitedUser()))).thenReturn(false);
        Mockito.when(eventMemberRepository.save(any(EventMember.class))).thenReturn(eventMember);
        EventMember returnedMember = eventMemberService.createEventMember(invite);
        Mockito.verify(eventMemberRepository, Mockito.times(1)).existsByEventAndUser(eq(invite.getEvent()),eq(invite.getInvitedUser()));
        Mockito.verify(eventMemberRepository, Mockito.times(1)).save(any(EventMember.class));
        Assertions.assertEquals(eventMember.getId(),returnedMember.getId());
    }

    @Test
    void createEventMemberAlreadyExistException(){
        EventMember eventMember = getTestEventMember();
        Invite invite = getTestInvite();
        Mockito.when(eventMemberRepository.existsByEventAndUser(eq(invite.getEvent()),eq(invite.getInvitedUser()))).thenReturn(true);
        Throwable throwable = assertThrows(MemberAlreadyExistException.class,()->
            {
                eventMemberService.createEventMember(invite);
            });
        Mockito.verify(eventMemberRepository, Mockito.times(1)).existsByEventAndUser(eq(invite.getEvent()),eq(invite.getInvitedUser()));
        Mockito.verify(eventMemberRepository, Mockito.times(0)).save(any(EventMember.class));
    }

    @Test
    void deleteMembership(){
        eventMemberService.deleteMembership(1L,1L);
        Mockito.verify(eventMemberRepository, Mockito.times(1)).deleteMembership(eq(1L),eq(1L));
    }
}
