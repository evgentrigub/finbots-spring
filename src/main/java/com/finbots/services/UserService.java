package com.finbots.services;

import com.finbots.models.*;
import com.finbots.repositories.UserRepository;
import com.finbots.security.JwtUtil;
import com.finbots.security.exceptions.BadRequestException;
import com.finbots.security.exceptions.IncorrectCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final String DEMO_TINKOFF_TOKEN_SANDBOX = "t.xYg4kVvi-ragDR7gBnw6a6aI54S4MSGq_zYn7UFSMj0h0TeJJxzr-Z3C2g9x3NXvDtvfEopq_RBxANi61cFZZQ";

    public User getByEmail(String email) {
        var userOptional = userRepository.findByEmail(email);
        return userOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public UserTokenDto login(UserDto dto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        } catch (BadCredentialsException e) {
            throw new IncorrectCredentialsException("Email or password is incorrect");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        return new UserTokenDto(jwt);
    }

    @Transactional
    public void create(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new BadRequestException("User with this email already exists");
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
    public void update(String email, UserUpdateProfileDto updateProfileDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (updateProfileDto.getEmail() != null && !updateProfileDto.getEmail().isEmpty()) {
            Optional<User> userWithNewUsername = userRepository.findByEmail(updateProfileDto.getEmail());
            if (userWithNewUsername.isPresent()) {
                throw new BadRequestException("Username already taken");
            }
            user.setEmail(updateProfileDto.getEmail());
        }

        user.setTinkoffToken(updateProfileDto.getTinkoffToken());

        userRepository.save(user);
    }

    @Transactional
    public void remove(String id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        userRepository.delete(user);
    }

    public void changePassword(UserUpdatePasswordRequestDto updatePasswordDto, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(updatePasswordDto.getOldPass(), user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPass()));
        userRepository.save(user);
    }
}
