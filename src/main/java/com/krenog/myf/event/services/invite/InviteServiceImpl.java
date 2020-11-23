package com.krenog.myf.event.services.invite;

import com.krenog.myf.dto.FilterParameters;
import com.krenog.myf.event.entities.Event;
import com.krenog.myf.event.entities.Invite;
import com.krenog.myf.event.entities.InviteStatus;
import com.krenog.myf.event.exceptions.UserAlreadyInvitedException;
import com.krenog.myf.event.repositories.InviteRepository;
import com.krenog.myf.event.services.member.EventMemberService;
import com.krenog.myf.exceptions.NotFoundException;
import com.krenog.myf.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InviteServiceImpl implements InviteService {
    private final InviteRepository inviteRepository;
    @Autowired
    private EventMemberService memberService;


    public InviteServiceImpl(InviteRepository inviteRepository) {
        this.inviteRepository = inviteRepository;
    }

    @Override
    public List<Invite> getUserInvites(Long userId, FilterParameters filterParameters) {
        Pageable pageable = PageRequest.of(filterParameters.getOffset(), filterParameters.getLimit());
        return inviteRepository.findAllByInvitedUserOrderByCreatedDate(userId, pageable);
    }

    @Override
    public void checkAndCreateInvite(InviteData inviteData) {
        checkInviteExistence(inviteData.getToUser(), inviteData.getEvent());
        createInvite(inviteData);

    }

    private void checkInviteExistence(User invitedUser, Event event) {
        boolean hasInvite = inviteRepository.existsByInvitedUserAndEvent(invitedUser, event);
        if (hasInvite) {
            throw new UserAlreadyInvitedException("User already invited userId: " + invitedUser.getId().toString() + " eventId: " + event.getId().toString());
        }
    }

    private void createInvite(InviteData inviteData) {
        Invite invite = new Invite(inviteData);
        inviteRepository.save(invite);
    }

    @Override
    @Transactional
    public void acceptInvite(Long userId, Long inviteId) {
        Invite invite = getByIdAndInvitedUserId(inviteId, userId);
        memberService.createEventMember(invite);
        updateStatus(invite, InviteStatus.ACCEPTED);
    }

    @Override
    public void rejectInvite(Long userId, Long inviteId) {
        Invite invite = getByIdAndInvitedUserId(inviteId, userId);
        updateStatus(invite, InviteStatus.REJECTED);
    }

    private Invite getByIdAndInvitedUserId(Long inviteId, Long userId) {
        return inviteRepository.getInviteByIdAndInvitedUserId(inviteId, userId)
                .orElseThrow(
                        () -> new NotFoundException("Invite")
                );
    }

    private void updateStatus(Invite invite, InviteStatus inviteStatus) {
        invite.setInviteStatus(inviteStatus);
        inviteRepository.save(invite);
    }
}
