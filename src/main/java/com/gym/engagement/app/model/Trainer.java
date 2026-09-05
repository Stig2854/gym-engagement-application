package com.gym.engagement.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class Trainer extends User {

    private Long userId;
    private TrainingType specialization;
}