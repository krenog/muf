package com.krenog.myf.user.services.user;

import com.krenog.myf.exceptions.NotFoundException;
import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.repositories.UserRepository;
import com.krenog.myf.user.services.authentication.exceptions.UserAlreadyExistException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        Optional<User> optionalUser = userRepository.getByPhoneNumber(phoneNumber);
        return optionalUser.orElseGet(() -> {
            throw new NotFoundException("User not found by phoneNumber: " + phoneNumber);
        });
    }

    public User createUser(String phoneNumber, String username) {
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setUsername(username);
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new UserAlreadyExistException();
        }
    }

    @Override
    public void updateLastLogin(User user) {
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void updatePushToken(Long userId, String pushToken) {
        User user = getUserById(userId);
        user.setPushToken(pushToken);
        userRepository.save(user);
    }

    @Override
    public Boolean checkUsernameIsExist(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean checkPhoneNumberIsExist(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    private User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException("User not found");
        }
    }
}
