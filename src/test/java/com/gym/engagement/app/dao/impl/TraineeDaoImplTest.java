package com.gym.engagement.app.dao.impl;

import com.gym.engagement.app.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TraineeDaoImplTest {

    private static final Long FIRST_TRAINEE_ID = 1L;
    private static final Long SECOND_TRAINEE_ID = 2L;

    private TraineeDaoImpl traineeDao;
    private Map<Long, Trainee> storage;

    @BeforeEach
    void setUp() {
        storage = new LinkedHashMap<>();
        traineeDao = new TraineeDaoImpl();
        traineeDao.setStorage(storage);
    }

    @Test
    void save_ShouldAddTraineeToStorage() {
        Trainee trainee = createTrainee(FIRST_TRAINEE_ID, "John", "Smith");

        traineeDao.save(FIRST_TRAINEE_ID, trainee);

        assertEquals(1, storage.size());
        assertSame(trainee, storage.get(FIRST_TRAINEE_ID));
    }

    @Test
    void findById_ShouldReturnTrainee_WhenTraineeExists() {
        Trainee trainee = createTrainee(FIRST_TRAINEE_ID, "John", "Smith");
        storage.put(FIRST_TRAINEE_ID, trainee);

        Optional<Trainee> result = traineeDao.findById(FIRST_TRAINEE_ID);

        assertTrue(result.isPresent());
        assertSame(trainee, result.get());
    }

    @Test
    void findById_ShouldReturnEmptyOptional_WhenTraineeDoesNotExist() {
        Optional<Trainee> result = traineeDao.findById(FIRST_TRAINEE_ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllTraineesWithoutChangingStorage() {
        Trainee firstTrainee = createTrainee(FIRST_TRAINEE_ID, "John", "Smith");
        Trainee secondTrainee = createTrainee(SECOND_TRAINEE_ID, "Anna", "Brown");

        storage.put(FIRST_TRAINEE_ID, firstTrainee);
        storage.put(SECOND_TRAINEE_ID, secondTrainee);

        List<Trainee> result = traineeDao.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(firstTrainee));
        assertTrue(result.contains(secondTrainee));

        result.clear();

        assertEquals(2, storage.size());
    }

    @Test
    void update_ShouldReplaceExistingTraineeInStorage() {
        Trainee existingTrainee = createTrainee(FIRST_TRAINEE_ID, "John", "Smith");
        Trainee updatedTrainee = createTrainee(FIRST_TRAINEE_ID, "John", "Johnson");

        storage.put(FIRST_TRAINEE_ID, existingTrainee);

        traineeDao.update(FIRST_TRAINEE_ID, updatedTrainee);

        assertEquals(1, storage.size());
        assertSame(updatedTrainee, storage.get(FIRST_TRAINEE_ID));
        assertEquals("Johnson", storage.get(FIRST_TRAINEE_ID).getLastName());
    }

    @Test
    void deleteById_ShouldRemoveTraineeFromStorage() {
        Trainee trainee = createTrainee(FIRST_TRAINEE_ID, "John", "Smith");
        storage.put(FIRST_TRAINEE_ID, trainee);

        traineeDao.deleteById(FIRST_TRAINEE_ID);

        assertTrue(storage.isEmpty());
        assertTrue(traineeDao.findById(FIRST_TRAINEE_ID).isEmpty());
    }

    private Trainee createTrainee(Long id, String firstName, String lastName) {
        return Trainee.builder()
                .userId(id)
                .firstName(firstName)
                .lastName(lastName)
                .active(true)
                .build();
    }
}