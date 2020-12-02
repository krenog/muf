package com.krenog.myf.event.repositories;

import com.krenog.myf.event.entities.Event;
import com.krenog.myf.event.entities.EventMember;
import com.krenog.myf.event.entities.MemberRole;
import com.krenog.myf.user.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventMemberRepository extends JpaRepository<EventMember, Long> {
    List<EventMember> findByUserIdOrderByCreatedDate(Long userId, Pageable pageable);

    List<EventMember> findByUserIdAndRoleOrderByCreatedDate(Long userId, MemberRole memberRole, Pageable pageable);

    boolean existsByEventAndUser(Event event, User user);
    @Modifying
    @Query("delete from EventMember ev where ev.user.id=:userId and ev.event.id=:eventId")
    void deleteMembership(@Param("userId") Long userId,@Param("eventId") Long eventId);
}
