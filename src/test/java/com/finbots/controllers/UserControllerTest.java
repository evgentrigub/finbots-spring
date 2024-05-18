package com.finbots.controllers;

import com.finbots.models.*;
import com.finbots.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        // Arrange
        UserDto userDto = new UserDto("test@example.com", "password");
        UserTokenDto expectedToken = new UserTokenDto("token");
        when(userService.login(userDto)).thenReturn(expectedToken);

        // Act
        UserTokenDto actualToken = userController.login(userDto);

        // Assert
        assertEquals(expectedToken, actualToken);
        verify(userService).login(userDto);
    }

    @Test
    void testSignup() {
        // Arrange
        UserDto userDto = new UserDto("test@example.com", "password");
        doNothing().when(userService).create(userDto);

        // Act
        userController.create(userDto);

        // Assert
        verify(userService).create(userDto);
    }

    @Test
    void testGetProfile() {
        // Arrange
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        User expectedUser = new User("test@example.com", "Test User");
        when(userService.getByEmail("test@example.com")).thenReturn(expectedUser);

        // Act
        User actualUser = userController.getProfile(userDetails);

        // Assert
        assertEquals(expectedUser, actualUser);
        verify(userService).getByEmail("test@example.com");
    }

    @Test
    void testUpdateProfile() {
        // Arrange
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        UserUpdateProfileDto updateUserProfile = new UserUpdateProfileDto("New Name");
        doNothing().when(userService).update("test@example.com", updateUserProfile);

        // Act
        userController.update(userDetails, updateUserProfile);

        // Assert
        verify(userService).update("test@example.com", updateUserProfile);
    }

    @Test
    void testChangePassword() throws Exception {
        // Arrange
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        UserUpdatePasswordRequestDto updatePasswordRequestDto = new UserUpdatePasswordRequestDto("oldPassword", "newPassword");
        doNothing().when(userService).changePassword(updatePasswordRequestDto, "test@example.com");

        // Act
        userController.changePassword(userDetails, updatePasswordRequestDto);

        // Assert
        verify(userService).changePassword(updatePasswordRequestDto, "test@example.com");
    }
}