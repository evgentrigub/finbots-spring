package com.finbots.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.finbots.models.user.*;
import com.finbots.repositories.UserRepository;
import com.finbots.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login() {
        UserDto userDto = new UserDto("test@test.com", "password");
        User user = new User("test@test.com", "password");
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user.getId())).thenReturn("token");

        UserTokenDto result = userService.login(userDto);

        assertEquals("token", result.getToken());
    }

    @Test
    void create() {
        UserDto userDto = new UserDto("test@test.com", "password");
        User user = new User("test@test.com", "password");
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.create(userDto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getIUserInfoByEmail() {
        String email = "test@test.com";
        User user = new User(email, "password");
        user.setTinkoffToken("token");
        user.setRole("ROLE_USER");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserInfoDto result = userService.getIUserInfoByEmail(email);

        assertEquals(email, result.getEmail());
        assertEquals("token", result.getTinkoffToken());
        assertEquals("ROLE_USER", result.getRole());
    }

    @Test
    void update() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@test.com");
        UserUpdateProfileDto updateProfileDto = new UserUpdateProfileDto("new@test.com", "newToken");
        User user = new User("test@test.com", "password");
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("new@test.com")).thenReturn(Optional.empty());

        userService.update(userDetails, updateProfileDto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void delete() {
        String email = "test@test.com";
        User user = new User(email, "password");
        user.setId("id");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        userService.delete(email);

        verify(userRepository, times(1)).deleteById(any(String.class));
    }

    @Test
    void changePassword() {
        String email = "test@test.com";
        UserUpdatePasswordRequestDto updatePasswordDto = new UserUpdatePasswordRequestDto("oldPassword", "newPassword");
        User user = new User(email, "oldPassword");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", user.getPassword())).thenReturn(true);

        userService.changePassword(updatePasswordDto, email);

        verify(userRepository, times(1)).save(any(User.class));
    }
}