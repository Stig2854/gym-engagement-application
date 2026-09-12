package com.gym.engagement.app.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class User {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean active;
}