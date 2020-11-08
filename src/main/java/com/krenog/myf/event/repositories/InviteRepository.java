package com.krenog.myf.event.repositories;

import com.krenog.myf.event.entities.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteRepository extends JpaRepository<Invite, Long> {
}
