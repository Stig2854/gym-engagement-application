package com.gym.engagement.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TrainerDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private TrainingTypeDto specialization;
    private boolean isActive;
}
