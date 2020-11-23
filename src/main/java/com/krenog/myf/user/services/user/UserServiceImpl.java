package com.krenog.myf.user.services.user;

import com.krenog.myf.user.dto.user.FindUsersByUsernameParameters;
import com.krenog.myf.user.entities.User;
import com.krenog.myf.user.exceptions.UserNotFoundException;
import com.krenog.myf.user.repositories.UserRepository;
import com.krenog.myf.user.services.authentication.exceptions.UserAlreadyExistException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, CommonUserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        Optional<User> optionalUser = userRepository.getByPhoneNumber(phoneNumber);
        return optionalUser.orElseGet(() -> {
            throw new UserNotFoundException("User not found by phoneNumber: " + phoneNumber);
        });
    }

    public User createUser(CreateUserData createUserData) {
        User user = new User(createUserData);
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

    @Override
    public List<User> findUsersByUsername(FindUsersByUsernameParameters findUsersByUsernameDto) {
        Pageable pageable = PageRequest.of(findUsersByUsernameDto.getOffset(), findUsersByUsernameDto.getLimit());
        return userRepository.findByUsernameStartsWith(findUsersByUsernameDto.getUsername(), pageable);
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException("User not found");
        }
    }
}
