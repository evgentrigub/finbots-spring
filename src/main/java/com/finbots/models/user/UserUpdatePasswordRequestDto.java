package com.finbots.models.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdatePasswordRequestDto {
    @NotEmpty
    private String oldPass;

    @NotEmpty
    private String newPass;
}
