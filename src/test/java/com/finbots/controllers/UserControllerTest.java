package com.finbots.controllers;

import com.finbots.models.*;
import com.finbots.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDto userDto;
    private UserUpdateProfileDto updateUserProfileDto;
    private UserPrincipalPayload userPrincipalPayload;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setPassword("password");

        updateUserProfileDto = new UserUpdateProfileDto();
        updateUserProfileDto.setEmail("updated@test.com");
        updateUserProfileDto.setTinkoffToken("updatedToken");

        userPrincipalPayload = new UserPrincipalPayload();
        userPrincipalPayload.setId("1");
        userPrincipalPayload.setEmail("test@test.com");
        userPrincipalPayload.setTinkoffToken("token");

        authentication = new UsernamePasswordAuthenticationToken(userPrincipalPayload, null);
    }

    @Test
    void login() {
        when(userService.login(any(UserDto.class))).thenReturn(new UserTokenDto("token"));
        UserTokenDto result = userController.login(userDto);
        assertEquals("token", result.getToken());
    }

    @Test
    void create() {
        doNothing().when(userService).create(any(UserDto.class));
        userController.create(userDto);
        verify(userService, times(1)).create(userDto);
    }

    @Test
    void getProfile() {
        User user = new User();
        user.setEmail("test@test.com");
        when(userService.getByEmail(anyString())).thenReturn(Optional.of(user));
        User result = userController.getProfile(authentication);
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    void update() {
        doNothing().when(userService).update(anyString(), any(UserUpdateProfileDto.class));
        userController.update(authentication, updateUserProfileDto);
        verify(userService, times(1)).update(userPrincipalPayload.getId(), updateUserProfileDto);
    }
}