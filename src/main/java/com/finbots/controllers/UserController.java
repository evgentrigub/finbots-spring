package com.finbots.controllers;

import com.finbots.models.bot.BotInfoDto;
import com.finbots.models.user.*;
import com.finbots.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController { // TODO добавить validated к методам

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public UserTokenDto login(@RequestBody UserDto userDto) {
        return userService.login(userDto);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody UserDto userDto) {
        userService.create(userDto);
    }

    @GetMapping
    public UserInfoDto getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getIUserInfoByEmail(userDetails.getUsername());
    }

    @PutMapping
    public void update(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UserUpdateProfileDto updateUserProfile) {
        userService.update(userDetails, updateUserProfile);
    }

    @PostMapping("/change-password")
    public void changePassword(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UserUpdatePasswordRequestDto updatePasswordRequestDto) {
        userService.changePassword(updatePasswordRequestDto, userDetails.getUsername());
    }

    @DeleteMapping
    public void delete(@AuthenticationPrincipal UserDetails userDetails) {
        userService.delete(userDetails.getUsername());
    }

    @GetMapping("/bots")
    public List<BotInfoDto> getBots(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getBotsByUser(userDetails.getUsername());
    }

}
