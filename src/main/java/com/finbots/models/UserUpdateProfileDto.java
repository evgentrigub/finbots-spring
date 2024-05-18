package com.finbots.models;


import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdateProfileDto {
    @Email
    private String email;

    private String tinkoffToken;

    public UserUpdateProfileDto(String email) {
        this.email = email;
    }
}
