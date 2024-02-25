package com.finbots.models;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDto {
    @Email
    @NotNull
    private String email;

    @NotEmpty
    private String password;
}