package com.krenog.myf.event.repositories;

import com.krenog.myf.event.entities.Event;
import com.krenog.myf.event.entities.Invite;
import com.krenog.myf.user.entities.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InviteRepository extends JpaRepository<Invite, Long> {
    @Query("select inv from Invite inv join inv.event ev " +
            "where inv.invitedUser.id = :userId " +
            "and inv.inviteStatus = com.krenog.myf.event.entities.InviteStatus.CREATED " +
            "and  ev.status = com.krenog.myf.event.entities.EventStatus.ACTIVE order by inv.createdDate")
    List<Invite> findAllByInvitedUserOrderByCreatedDate(@Param("userId") Long userId, Pageable pageable);

    Boolean existsByInvitedUserAndEvent(User invitedUser, Event event);

    Optional<Invite> getInviteByIdAndInvitedUserId(Long inviteId, Long userId);
}
