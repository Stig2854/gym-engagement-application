package com.gym.engagement.app.storage;

import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class StorageConfig {
    @Bean("traineeStorage")
    public Map<Long, Trainee> traineeStorage(InMemoryStorage inMemoryStorage) {
        return inMemoryStorage.getTrainees();
    }

    @Bean("trainingStorage")
    public Map<Long, Training> trainingStorage(InMemoryStorage inMemoryStorage) {
        return inMemoryStorage.getTrainings();
    }

    @Bean("trainerStorage")
    public Map<Long, Trainer> trainerStorage(InMemoryStorage inMemoryStorage) {
        return inMemoryStorage.getTrainers();
    }
}
