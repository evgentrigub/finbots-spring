package com.finbots.services;

import com.finbots.models.User;
import com.finbots.models.UserDto;
import com.finbots.models.UserTokenDto;
import com.finbots.models.UserUpdateProfileDto;
import com.finbots.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    private static final String DEMO_TINKOFF_TOKEN_SANDBOX = "t.xYg4kVvi-ragDR7gBnw6a6aI54S4MSGq_zYn7UFSMj0h0TeJJxzr-Z3C2g9x3NXvDtvfEopq_RBxANi61cFZZQ";

    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> get(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Email or password are incorrect");
        }
    }

    public UserTokenDto login(UserDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Email or password are incorrect"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email or password are incorrect");
        }

        return new UserTokenDto("gsjkdgjkds");
    }

    @Transactional
    public void create(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already used");
        }

        User newUser = new User();
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        try {
            userRepository.save(newUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating new user: " + e.getMessage());
        }
    }

    @Transactional
    public void update(String id, UserUpdateProfileDto userProfile) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setEmail(userProfile.getEmail());
        user.setTinkoffToken(userProfile.getTinkoffToken());

        userRepository.save(user);
    }

    @Transactional
    public void remove(String id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        userRepository.delete(user);
    }
}
