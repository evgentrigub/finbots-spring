package com.finbots.controllers;

import com.finbots.models.*;
import com.finbots.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user") // TODO: Change to "/users" to follow REST conventions
public class UserController {

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
    // @PreAuthorize or @Secured can be used to secure the endpoint
    public User getProfile(Authentication authentication) {
        UserPrincipalPayload userJwtPayload = (UserPrincipalPayload) authentication.getPrincipal();
        var userOptional = userService.getByEmail(userJwtPayload.getEmail());
        return userOptional.get();
    }

    @PutMapping
    // @PreAuthorize or @Secured can be used to secure the endpoint
    public void update(Authentication authentication, @RequestBody UserUpdateProfileDto updateUserProfile) {
        UserPrincipalPayload userJwtPayload = (UserPrincipalPayload) authentication.getPrincipal();
        userService.update(userJwtPayload.getId(), updateUserProfile);
    }
}
