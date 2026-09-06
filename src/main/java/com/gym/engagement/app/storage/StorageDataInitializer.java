package com.gym.engagement.app.storage;

import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.model.TrainingType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Component
public class StorageDataInitializer implements BeanPostProcessor {

    private Resource initialDataFile;

    @Value("${storage.initial-data.path}")
    public void setInitialDataFile(Resource initialDataFile) {
        this.initialDataFile = initialDataFile;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof InMemoryStorage) {
            initializeStorage((InMemoryStorage) bean);
        }

        return bean;
    }

    private void initializeStorage(InMemoryStorage storage) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        initialDataFile.getInputStream(),
                        StandardCharsets.UTF_8
                )
        )) {
            reader.lines()
                    .filter(line -> !line.isBlank())
                    .forEach(line -> initializeEntity(storage, line));
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Failed to initialize in-memory storage",
                    exception
            );
        }
    }

    private void initializeEntity(InMemoryStorage storage, String line) {
        String[] values = line.split(",", -1);

        switch (values[0]) {
            case "TRAINEE" -> initializeTrainee(storage, values);
            case "TRAINER" -> initializeTrainer(storage, values);
            case "TRAINING" -> initializeTraining(storage, values);
            default -> throw new IllegalArgumentException(
                    "Unsupported initial data record type: " + values[0]
            );
        }
    }

    private void initializeTrainee(InMemoryStorage storage, String[] values) {
        Long userId = Long.valueOf(values[1]);

        Trainee trainee = Trainee.builder()
                .userId(userId)
                .firstName(values[2])
                .lastName(values[3])
                .username(values[4])
                .password(values[5])
                .active(Boolean.parseBoolean(values[6]))
                .dateOfBirth(LocalDate.parse(values[7]))
                .address(values[8])
                .build();

        storage.getTrainees().put(userId, trainee);
    }

    private void initializeTrainer(InMemoryStorage storage, String[] values) {
        Long userId = Long.valueOf(values[1]);

        TrainingType specialization = TrainingType.builder()
                .trainingTypeName(values[7])
                .build();

        Trainer trainer = Trainer.builder()
                .userId(userId)
                .firstName(values[2])
                .lastName(values[3])
                .username(values[4])
                .password(values[5])
                .active(Boolean.parseBoolean(values[6]))
                .specialization(specialization)
                .build();

        storage.getTrainers().put(userId, trainer);
    }

    private void initializeTraining(InMemoryStorage storage, String[] values) {
        Long id = Long.valueOf(values[1]);

        TrainingType trainingType = TrainingType.builder()
                .trainingTypeName(values[5])
                .build();

        Training training = Training.builder()
                .id(id)
                .traineeId(Long.valueOf(values[2]))
                .trainerId(Long.valueOf(values[3]))
                .trainingName(values[4])
                .trainingType(trainingType)
                .trainingDate(LocalDate.parse(values[6]))
                .trainingDuration(Integer.parseInt(values[7]))
                .build();

        storage.getTrainings().put(id, training);
    }
}