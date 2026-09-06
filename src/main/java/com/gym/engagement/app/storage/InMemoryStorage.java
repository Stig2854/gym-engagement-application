package com.gym.engagement.app.storage;

import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class InMemoryStorage {
    private final Map<Long, Trainee> trainees = new HashMap<>();
    private final Map<Long, Training> trainings = new HashMap<>();
    private final Map<Long, Trainer> trainers = new HashMap<>();
}
