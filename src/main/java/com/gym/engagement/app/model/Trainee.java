package com.gym.engagement.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Trainee extends User {

    private Long userId;
    private LocalDate dateOfBirth;
    private String address;
}