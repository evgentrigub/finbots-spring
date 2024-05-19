package com.finbots.controllers;

import com.finbots.models.user.*;
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
        UserInfoDto expectedUser = new UserInfoDto("test@example.com", "Test User", "ROLE_USER");
        when(userService.getIUserInfoByEmail("test@example.com")).thenReturn(expectedUser);

        // Act
        UserInfoDto actualUser = userController.getProfile(userDetails);

        // Assert
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getRole(), actualUser.getRole());
        assertEquals(expectedUser.getTinkoffToken(), actualUser.getTinkoffToken());
        verify(userService).getIUserInfoByEmail("test@example.com");
    }

    @Test
    void testUpdateProfile() {
        // Arrange
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        UserUpdateProfileDto updateUserProfile = new UserUpdateProfileDto();
        updateUserProfile.setEmail("updated@example.com");
        updateUserProfile.setTinkoffToken("updatedToken");
        doNothing().when(userService).update(userDetails, updateUserProfile);

        UserInfoDto updatedUser = new UserInfoDto("updated@example.com", "updatedToken", "ROLE_USER");
        when(userService.getIUserInfoByEmail("test@example.com")).thenReturn(updatedUser); // Mock getByEmail() to return updatedUser when called with "test@example.com"

        // Act
        userController.update(userDetails, updateUserProfile);
        UserInfoDto actualUser = userController.getProfile(userDetails);

        // Assert
        assertEquals(updatedUser.getEmail(), actualUser.getEmail());
        assertEquals(updatedUser.getRole(), actualUser.getRole());
        assertEquals(updatedUser.getTinkoffToken(), actualUser.getTinkoffToken());
        verify(userService).update(userDetails, updateUserProfile);
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