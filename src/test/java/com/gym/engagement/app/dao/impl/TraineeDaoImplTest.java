package com.gym.engagement.app.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gym.engagement.app.model.Trainee;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TraineeDaoImplTest {

    private static final Long FIRST_TRAINEE_ID = 1L;
    private static final Long SECOND_TRAINEE_ID = 2L;
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Smith";
    private static final String UPDATED_LAST_NAME = "Johnson";

    private Map<Long, Trainee> storage;

    private TraineeDaoImpl dao;

    @BeforeEach
    void setUp() {
        storage = new LinkedHashMap<>();
        dao = new TraineeDaoImpl();
        dao.setStorage(storage);
    }

    @Test
    void save_ShouldAddTraineeToStorage() {
        Trainee trainee = createTrainee(FIRST_TRAINEE_ID, FIRST_NAME, LAST_NAME);

        dao.save(FIRST_TRAINEE_ID, trainee);

        assertEquals(1, storage.size());
        assertSame(trainee, storage.get(FIRST_TRAINEE_ID));
    }

    @Test
    void findById_ShouldReturnTrainee_WhenTraineeExists() {
        Trainee trainee = createTrainee(FIRST_TRAINEE_ID, FIRST_NAME, LAST_NAME);
        storage.put(FIRST_TRAINEE_ID, trainee);

        Optional<Trainee> actual = dao.findById(FIRST_TRAINEE_ID);

        assertTrue(actual.isPresent());
        assertSame(trainee, actual.get());
    }

    @Test
    void findById_ShouldReturnEmptyOptional_WhenTraineeDoesNotExist() {
        Optional<Trainee> actual = dao.findById(FIRST_TRAINEE_ID);

        assertTrue(actual.isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllTrainees() {
        Trainee firstTrainee = createTrainee(FIRST_TRAINEE_ID, FIRST_NAME, LAST_NAME);
        Trainee secondTrainee = createTrainee(SECOND_TRAINEE_ID, "Anna", "Brown");
        storage.put(FIRST_TRAINEE_ID, firstTrainee);
        storage.put(SECOND_TRAINEE_ID, secondTrainee);

        List<Trainee> actual = dao.findAll();

        assertEquals(2, actual.size());
        assertTrue(actual.contains(firstTrainee));
        assertTrue(actual.contains(secondTrainee));
    }

    @Test
    void update_ShouldReplaceExistingTraineeInStorage() {
        Trainee existingTrainee = createTrainee(FIRST_TRAINEE_ID, FIRST_NAME, LAST_NAME);
        Trainee updatedTrainee = createTrainee(FIRST_TRAINEE_ID, FIRST_NAME, UPDATED_LAST_NAME);
        storage.put(FIRST_TRAINEE_ID, existingTrainee);

        dao.update(FIRST_TRAINEE_ID, updatedTrainee);

        assertEquals(1, storage.size());
        assertSame(updatedTrainee, storage.get(FIRST_TRAINEE_ID));
        assertEquals(UPDATED_LAST_NAME, storage.get(FIRST_TRAINEE_ID).getLastName());
    }

    @Test
    void deleteById_ShouldRemoveTraineeFromStorage() {
        Trainee trainee = createTrainee(FIRST_TRAINEE_ID, FIRST_NAME, LAST_NAME);
        storage.put(FIRST_TRAINEE_ID, trainee);

        dao.deleteById(FIRST_TRAINEE_ID);

        assertTrue(storage.isEmpty());
        assertTrue(dao.findById(FIRST_TRAINEE_ID).isEmpty());
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