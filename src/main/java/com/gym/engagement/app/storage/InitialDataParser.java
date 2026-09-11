package com.gym.engagement.app.storage;

import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.model.TrainingType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class InitialDataParser {

    public Object parse(String line) {
        String[] values = line.split(",", -1);

        return switch (values[0]) {
            case "TRAINEE" -> parseTrainee(values);
            case "TRAINER" -> parseTrainer(values);
            case "TRAINING" -> parseTraining(values);
            default -> throw new IllegalArgumentException("Unsupported initial data record type: %s".formatted(values[0]));
        };
    }

    private Trainee parseTrainee(String[] values) {
        return Trainee.builder()
                .userId(Long.valueOf(values[1]))
                .firstName(values[2])
                .lastName(values[3])
                .username(values[4])
                .password(values[5])
                .active(Boolean.parseBoolean(values[6]))
                .dateOfBirth(LocalDate.parse(values[7]))
                .address(values[8])
                .build();
    }

    private Trainer parseTrainer(String[] values) {
        TrainingType specialization = TrainingType.builder()
                .trainingTypeName(values[7])
                .build();

        return Trainer.builder()
                .userId(Long.valueOf(values[1]))
                .firstName(values[2])
                .lastName(values[3])
                .username(values[4])
                .password(values[5])
                .active(Boolean.parseBoolean(values[6]))
                .specialization(specialization)
                .build();
    }

    private Training parseTraining(String[] values) {
        TrainingType trainingType = TrainingType.builder()
                .trainingTypeName(values[5])
                .build();

        return Training.builder()
                .id(Long.valueOf(values[1]))
                .traineeId(Long.valueOf(values[2]))
                .trainerId(Long.valueOf(values[3]))
                .trainingName(values[4])
                .trainingType(trainingType)
                .trainingDate(LocalDate.parse(values[6]))
                .trainingDuration(Integer.parseInt(values[7]))
                .build();
    }
}
