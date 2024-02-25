package com.finbots.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserUpdatePasswordRequestDto {
    @NotEmpty
    private String oldPass;

    @NotEmpty
    private String newPass;
}
