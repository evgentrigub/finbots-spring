package com.finbots.models;


import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateProfileDto {
    @Email
    private String email;

    private String tinkoffToken;
}
