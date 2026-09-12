package com.gym.engagement.app.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.model.TrainingType;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrainingDaoImplTest {

    private static final Long TRAINING_ID = 1L;
    private static final Long SECOND_TRAINING_ID = 2L;
    private static final Long TRAINEE_ID = 10L;
    private static final Long TRAINER_ID = 20L;

    private static final String YOGA = "Yoga";
    private static final String MORNING_YOGA = "Morning Yoga";
    private static final LocalDate TRAINING_DATE = LocalDate.of(2026, 9, 5);

    private Map<Long, Training> storage;
    private TrainingDaoImpl dao;

    @BeforeEach
    void setUp() {
        storage = new LinkedHashMap<>();
        dao = new TrainingDaoImpl();
        dao.setStorage(storage);
    }

    @Test
    void save_ShouldAddTrainingToStorage() {
        Training training = createTraining();

        dao.save(TRAINING_ID, training);

        assertEquals(1, storage.size());
        assertSame(training, storage.get(TRAINING_ID));
    }

    @Test
    void findById_ShouldReturnTraining_WhenTrainingExists() {
        Training training = createTraining();
        storage.put(TRAINING_ID, training);

        Optional<Training> actual = dao.findById(TRAINING_ID);

        assertTrue(actual.isPresent());
        assertSame(training, actual.get());
    }

    @Test
    void findById_ShouldReturnEmptyOptional_WhenTrainingDoesNotExist() {
        Optional<Training> actual = dao.findById(TRAINING_ID);

        assertTrue(actual.isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllTrainingsFromStorage() {
        Training firstTraining = createTraining();
        Training secondTraining = createSecondTraining();

        storage.put(TRAINING_ID, firstTraining);
        storage.put(SECOND_TRAINING_ID, secondTraining);

        List<Training> actual = dao.findAll();

        assertEquals(List.of(firstTraining, secondTraining), actual);
    }

    @Test
    void update_ShouldReplaceTrainingInStorage() {
        Training existingTraining = createTraining();
        Training updatedTraining = createUpdatedTraining();
        storage.put(TRAINING_ID, existingTraining);

        dao.update(TRAINING_ID, updatedTraining);

        assertEquals(1, storage.size());
        assertSame(updatedTraining, storage.get(TRAINING_ID));
    }

    @Test
    void deleteById_ShouldRemoveTrainingFromStorage() {
        Training training = createTraining();
        storage.put(TRAINING_ID, training);

        dao.deleteById(TRAINING_ID);

        assertTrue(storage.isEmpty());
    }

    private Training createTraining() {
        return Training.builder()
                .id(TRAINING_ID)
                .traineeId(TRAINEE_ID)
                .trainerId(TRAINER_ID)
                .trainingName(MORNING_YOGA)
                .trainingType(createTrainingType(YOGA))
                .trainingDate(TRAINING_DATE)
                .trainingDuration(60)
                .build();
    }

    private Training createSecondTraining() {
        return Training.builder()
                .id(SECOND_TRAINING_ID)
                .traineeId(TRAINEE_ID)
                .trainerId(TRAINER_ID)
                .trainingName("Strength Training")
                .trainingType(createTrainingType("Strength"))
                .trainingDate(LocalDate.of(2026, 9, 6))
                .trainingDuration(45)
                .build();
    }

    private Training createUpdatedTraining() {
        return Training.builder()
                .id(TRAINING_ID)
                .traineeId(TRAINEE_ID)
                .trainerId(TRAINER_ID)
                .trainingName("Updated Training")
                .trainingType(createTrainingType("Fitness"))
                .trainingDate(LocalDate.of(2026, 9, 7))
                .trainingDuration(90)
                .build();
    }

    private TrainingType createTrainingType(String trainingTypeName) {
        return TrainingType.builder()
                .trainingTypeName(trainingTypeName)
                .build();
    }
}