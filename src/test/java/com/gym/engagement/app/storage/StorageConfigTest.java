package com.gym.engagement.app.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StorageConfigTest {

    private static final Long TRAINEE_ID = 1L;
    private static final Long TRAINER_ID = 1L;
    private static final Long TRAINING_ID = 1L;

    @Mock
    private InMemoryStorage inMemoryStorage;

    private StorageConfig storageConfig;

    @BeforeEach
    void setUp() {
        storageConfig = new StorageConfig();
    }

    @Test
    void traineeStorage_ShouldReturnTraineeMapFromInMemoryStorage() {
        Map<Long, Trainee> traineeMap = new HashMap<>();
        traineeMap.put(TRAINEE_ID, new Trainee());

        when(inMemoryStorage.getTrainees()).thenReturn(traineeMap);

        Map<Long, Trainee> actual = storageConfig.traineeStorage(inMemoryStorage);

        assertSame(traineeMap, actual);
        assertEquals(1, actual.size());
        verify(inMemoryStorage).getTrainees();
    }

    @Test
    void traineeStorage_ShouldReturnEmptyMap_WhenStorageIsEmpty() {
        Map<Long, Trainee> traineeMap = new HashMap<>();

        when(inMemoryStorage.getTrainees()).thenReturn(traineeMap);

        Map<Long, Trainee> actual = storageConfig.traineeStorage(inMemoryStorage);

        assertTrue(actual.isEmpty());
        verify(inMemoryStorage).getTrainees();
    }

    @Test
    void trainingStorage_ShouldReturnTrainingMapFromInMemoryStorage() {
        Map<Long, Training> trainingMap = new HashMap<>();
        trainingMap.put(TRAINING_ID, new Training());

        when(inMemoryStorage.getTrainings()).thenReturn(trainingMap);

        Map<Long, Training> actual = storageConfig.trainingStorage(inMemoryStorage);

        assertSame(trainingMap, actual);
        assertEquals(1, actual.size());
        verify(inMemoryStorage).getTrainings();
    }

    @Test
    void trainingStorage_ShouldReturnEmptyMap_WhenStorageIsEmpty() {
        Map<Long, Training> trainingMap = new HashMap<>();

        when(inMemoryStorage.getTrainings()).thenReturn(trainingMap);

        Map<Long, Training> actual = storageConfig.trainingStorage(inMemoryStorage);

        assertTrue(actual.isEmpty());
        verify(inMemoryStorage).getTrainings();
    }

    @Test
    void trainerStorage_ShouldReturnTrainerMapFromInMemoryStorage() {
        Map<Long, Trainer> trainerMap = new HashMap<>();
        trainerMap.put(TRAINER_ID, new Trainer());

        when(inMemoryStorage.getTrainers()).thenReturn(trainerMap);

        Map<Long, Trainer> actual = storageConfig.trainerStorage(inMemoryStorage);

        assertSame(trainerMap, actual);
        assertEquals(1, actual.size());
        verify(inMemoryStorage).getTrainers();
    }

    @Test
    void trainerStorage_ShouldReturnEmptyMap_WhenStorageIsEmpty() {
        Map<Long, Trainer> trainerMap = new HashMap<>();

        when(inMemoryStorage.getTrainers()).thenReturn(trainerMap);

        Map<Long, Trainer> actual = storageConfig.trainerStorage(inMemoryStorage);

        assertTrue(actual.isEmpty());
        verify(inMemoryStorage).getTrainers();
    }
}