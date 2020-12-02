package com.krenog.myf.event.repositories;

import com.krenog.myf.event.entities.Event;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.locationtech.jts.geom.Polygon;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> getEventByIdAndOwnerId(Long eventId, Long userId);

    @Query(value = "select e from Event e where e.status = com.krenog.myf.event.entities.EventStatus.ACTIVE " +
            "and e.type = com.krenog.myf.event.entities.EventType.PUBLIC " +
            "and e.startDate > now() " +
            "and  within(e.point, :polygon) = true")
    List<Event> getNearestAllEvent(@Param("polygon") Polygon polygon);
}
