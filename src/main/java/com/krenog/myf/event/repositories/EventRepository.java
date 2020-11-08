package com.krenog.myf.event.repositories;

import com.krenog.myf.event.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> getEventByIdAndOwnerId(Long eventId, Long userId);
}
