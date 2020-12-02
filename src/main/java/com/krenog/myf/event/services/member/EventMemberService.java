package com.krenog.myf.event.services.member;

import com.krenog.myf.event.entities.EventMember;
import com.krenog.myf.event.entities.Invite;

import java.util.List;

public interface EventMemberService {
    List<EventMember> getMemberships(MembershipFilter membershipFilter);

    EventMember createEventMember(Invite invite);

    void deleteMembership(Long userId,Long eventId);
}
