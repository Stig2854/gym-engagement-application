package com.gym.engagement.app.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.TrainingType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrainerDaoImplTest {

    private static final Long TRAINER_ID = 1L;
    private static final Long SECOND_TRAINER_ID = 2L;

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Smith";
    private static final String USERNAME = "John.Smith";
    private static final String YOGA = "Yoga";

    private Map<Long, Trainer> storage;
    private TrainerDaoImpl dao;

    @BeforeEach
    void setUp() {
        storage = new LinkedHashMap<>();
        dao = new TrainerDaoImpl();
        dao.setStorage(storage);
    }

    @Test
    void save_ShouldAddTrainerToStorage() {
        Trainer trainer = createTrainer();

        dao.save(TRAINER_ID, trainer);

        assertEquals(1, storage.size());
        assertSame(trainer, storage.get(TRAINER_ID));
    }

    @Test
    void findById_ShouldReturnTrainer_WhenTrainerExists() {
        Trainer trainer = createTrainer();
        storage.put(TRAINER_ID, trainer);

        Optional<Trainer> actual = dao.findById(TRAINER_ID);

        assertTrue(actual.isPresent());
        assertSame(trainer, actual.get());
    }

    @Test
    void findById_ShouldReturnEmptyOptional_WhenTrainerDoesNotExist() {
        Optional<Trainer> actual = dao.findById(TRAINER_ID);

        assertTrue(actual.isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllTrainersFromStorage() {
        Trainer firstTrainer = createTrainer();
        Trainer secondTrainer = createSecondTrainer();

        storage.put(TRAINER_ID, firstTrainer);
        storage.put(SECOND_TRAINER_ID, secondTrainer);

        List<Trainer> actual = dao.findAll();

        assertEquals(List.of(firstTrainer, secondTrainer), actual);
    }

    @Test
    void update_ShouldReplaceTrainerInStorage() {
        Trainer existingTrainer = createTrainer();
        Trainer updatedTrainer = createUpdatedTrainer();
        storage.put(TRAINER_ID, existingTrainer);

        dao.update(TRAINER_ID, updatedTrainer);

        assertEquals(1, storage.size());
        assertSame(updatedTrainer, storage.get(TRAINER_ID));
    }

    @Test
    void deleteById_ShouldRemoveTrainerFromStorage() {
        Trainer trainer = createTrainer();
        storage.put(TRAINER_ID, trainer);

        dao.deleteById(TRAINER_ID);

        assertTrue(storage.isEmpty());
    }

    private Trainer createTrainer() {
        return Trainer.builder()
                .userId(TRAINER_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .username(USERNAME)
                .active(true)
                .specialization(createTrainingType(YOGA))
                .build();
    }

    private Trainer createSecondTrainer() {
        return Trainer.builder()
                .userId(SECOND_TRAINER_ID)
                .firstName("Anna")
                .lastName("Brown")
                .active(true)
                .specialization(createTrainingType("Fitness"))
                .build();
    }

    private Trainer createUpdatedTrainer() {
        return Trainer.builder()
                .userId(TRAINER_ID)
                .firstName("Updated")
                .lastName("Trainer")
                .active(false)
                .specialization(createTrainingType("Strength"))
                .build();
    }

    private TrainingType createTrainingType(String trainingTypeName) {
        return TrainingType.builder()
                .trainingTypeName(trainingTypeName)
                .build();
    }
}