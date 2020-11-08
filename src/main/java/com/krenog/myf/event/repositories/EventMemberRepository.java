package com.krenog.myf.event.repositories;

import com.krenog.myf.event.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventMemberRepository extends JpaRepository<Event, Long> {
}
