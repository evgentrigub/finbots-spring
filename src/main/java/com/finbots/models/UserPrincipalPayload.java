package com.finbots.models;

import lombok.Data;

@Data
public class UserPrincipalPayload {
    private String id;

    private String email;

    private String tinkoffToken;

}
