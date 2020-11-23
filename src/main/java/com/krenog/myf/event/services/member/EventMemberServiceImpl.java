package com.krenog.myf.event.services.member;

import com.krenog.myf.event.entities.EventMember;
import com.krenog.myf.event.entities.Invite;
import com.krenog.myf.event.repositories.EventMemberRepository;
import com.krenog.myf.event.services.member.exceptions.MemberAlreadyExistException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventMemberServiceImpl implements EventMemberService {
    private final EventMemberRepository eventMemberRepository;

    public EventMemberServiceImpl(EventMemberRepository eventMemberRepository) {
        this.eventMemberRepository = eventMemberRepository;
    }

    @Override
    public List<EventMember> getMemberships(MembershipFilter membershipFilter) {
        Pageable pageable = PageRequest.of(membershipFilter.getOffset(), membershipFilter.getLimit());
        if (membershipFilter.getRole() != null) {
            return eventMemberRepository.findByUserIdAndRoleOrderByCreatedDate(
                    membershipFilter.getUserId(),
                    membershipFilter.getRole(),
                    pageable);
        } else {
            return eventMemberRepository.findByUserIdOrderByCreatedDate(membershipFilter.getUserId(), pageable);
        }
    }

    @Override
    public EventMember createEventMember(Invite invite) {
        if (!eventMemberRepository.existsByEventAndUser(invite.getEvent(),invite.getUser())){
            return eventMemberRepository.save(EventMember.createGuest(invite.getUser(),invite.getEvent()));
        }else{
            throw new MemberAlreadyExistException();
        }
    }
}
