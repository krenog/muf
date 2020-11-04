package com.krenog.myf.user.repositories;

import com.krenog.myf.user.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getByPhoneNumber(String phoneNumber);

    Optional<User> getByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByPhoneNumber(String phoneNumber);

    List<User> findByUsernameStartsWith(String username, Pageable pageable);
}
