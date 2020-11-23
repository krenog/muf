package com.krenog.myf.event.repositories;

import com.krenog.myf.event.entities.Event;
import com.krenog.myf.event.entities.EventMember;
import com.krenog.myf.event.entities.MemberRole;
import com.krenog.myf.user.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventMemberRepository extends JpaRepository<EventMember, Long> {
    List<EventMember> findByUserIdOrderByCreatedDate(Long userId, Pageable pageable);

    List<EventMember> findByUserIdAndRoleOrderByCreatedDate(Long userId, MemberRole memberRole, Pageable pageable);

    boolean existsByEventAndUser(Event event, User user);
}
