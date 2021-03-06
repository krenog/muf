package com.krenog.myf.user.security.detail;

import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return UserPrincipal.build(user.get());
        } else {
            throw new UsernameNotFoundException("User Not Found with -> id : " + id);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = userRepository.getByUsername(username);
        if (user.isPresent()) {
            return UserPrincipal.build(user.get());
        } else {
            throw new UsernameNotFoundException("User Not Found with -> username : " + username);
        }
    }
}
