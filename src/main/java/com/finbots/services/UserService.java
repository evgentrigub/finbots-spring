package com.finbots.services;

import com.finbots.models.bot.BotInfoDto;
import com.finbots.models.user.*;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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

    private static final String DEMO_TINKOFF_TOKEN_SANDBOX = "t.xYg4kVvi-ragDR7gBnw6a6aI54S4MSGq_zYn7UFSMj0h0TeJJxzr-Z3C2g9x3NXvDtvfEopq_RBxANi61cFZZQ";

    public UserTokenDto login(UserDto dto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        } catch (BadCredentialsException e) {
            throw new IncorrectCredentialsException("Email or password is incorrect");
        }

        var userId = getUserByEmail(dto.getEmail()).getId();
        final String jwt = jwtUtil.generateToken(userId);

        return new UserTokenDto(jwt);
    }

    @Transactional
    public void create(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new BadRequestException("User with this email already exists");
        }

        User newUser = new User();
        newUser.setEmail(userDto.getEmail());
        newUser.setRole("ROLE_USER");
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        try {
            userRepository.save(newUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating new user: " + e.getMessage());
        }
    }

    public UserInfoDto getIUserInfoByEmail(String email) {
        User user = getUserByEmail(email);
        return new UserInfoDto(user.getEmail(), user.getTinkoffToken(), user.getRole());
    }

    @Transactional
    public void update(UserDetails details, UserUpdateProfileDto updateProfileDto) {
        User user = getUserByEmail(details.getUsername());

        if (!Objects.equals(updateProfileDto.getEmail(), user.getEmail()) && updateProfileDto.getEmail() != null  && !updateProfileDto.getEmail().isEmpty()) {
            Optional<User> userWithNewUsername = userRepository.findByEmail(updateProfileDto.getEmail());
            if (userWithNewUsername.isPresent()) {
                throw new BadRequestException("Email is already taken");
            }
            user.setEmail(updateProfileDto.getEmail());
        }

        user.setTinkoffToken(updateProfileDto.getTinkoffToken());

        userRepository.save(user);
    }

    @Transactional
    public void delete(String email) {
        User user = getUserByEmail(email);
        userRepository.deleteById(user.getId());
    }

    @Transactional
    public void changePassword(UserUpdatePasswordRequestDto updatePasswordDto, String email) {
        User user = getUserByEmail(email);

        if (!passwordEncoder.matches(updatePasswordDto.getOldPass(), user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPass()));
        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public List<BotInfoDto> getBotsByUser(String email) {
        User user = getUserByEmail(email);
        return user.getBots().stream().map(BotInfoDto::new).toList();
    }
}
