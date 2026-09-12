package com.gym.engagement.app.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.gym.engagement.app.dao.TrainerDao;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.TrainingType;
import com.gym.engagement.app.service.common.CoreValidator;
import com.gym.engagement.app.service.common.ProfileCredentialGenerator;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    private static final Long TRAINER_ID = 1L;
    private static final Long SECOND_TRAINER_ID = 2L;

    private static final String TRAINER = "Trainer";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Smith";
    private static final String USERNAME = "John.Smith";
    private static final String PASSWORD = "Abc123Xyz9";
    private static final String YOGA = "Yoga";
    private static final String FITNESS = "Fitness";
    private static final String TRAINER_ALREADY_EXISTS_MESSAGE = "Trainer with ID 1 already exists";
    private static final String TRAINER_NOT_FOUND_MESSAGE = "Trainer not found with ID: 1";

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private CoreValidator validator;

    @Mock
    private ProfileCredentialGenerator credentialGenerator;

    @Captor
    private ArgumentCaptor<Trainer> trainerCaptor;

    private TrainerServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TrainerServiceImpl();
        service.setTrainerDao(trainerDao);
        service.setValidator(validator);
        service.setCredentialGenerator(credentialGenerator);
    }

    @Test
    void create_ShouldGenerateCredentialsSaveAndReturnTrainer() {
        Trainer trainer = createTrainer();

        when(trainerDao.findById(TRAINER_ID)).thenReturn(Optional.empty());
        when(credentialGenerator.generateUsername(FIRST_NAME, LAST_NAME)).thenReturn(USERNAME);
        when(credentialGenerator.generatePassword()).thenReturn(PASSWORD);

        Trainer actual = service.create(trainer);
        verify(validator).validateTrainer(trainer);
        verify(trainerDao).findById(TRAINER_ID);
        verify(credentialGenerator).generateUsername(FIRST_NAME, LAST_NAME);
        verify(credentialGenerator).generatePassword();
        verify(trainerDao).save(eq(TRAINER_ID), trainerCaptor.capture());

        Trainer savedTrainer = trainerCaptor.getValue();
        assertEquals(TRAINER_ID, actual.getUserId());
        assertEquals(FIRST_NAME, actual.getFirstName());
        assertEquals(LAST_NAME, actual.getLastName());
        assertEquals(USERNAME, actual.getUsername());
        assertEquals(PASSWORD, actual.getPassword());
        assertTrue(actual.isActive());
        assertSame(trainer.getSpecialization(), actual.getSpecialization());

        assertEquals(actual.getUserId(), savedTrainer.getUserId());
        assertEquals(actual.getUsername(), savedTrainer.getUsername());
        assertEquals(actual.getPassword(), savedTrainer.getPassword());
    }

    @Test
    void create_ShouldThrowException_WhenTrainerWithIdAlreadyExists() {
        Trainer trainer = createTrainer();

        when(trainerDao.findById(TRAINER_ID)).thenReturn(Optional.of(trainer));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> service.create(trainer));

        assertEquals(TRAINER_ALREADY_EXISTS_MESSAGE, exception.getMessage());
        verify(validator).validateTrainer(trainer);
        verify(trainerDao).findById(TRAINER_ID);
        verifyNoInteractions(credentialGenerator);
        verify(trainerDao, never()).save(eq(TRAINER_ID), any());
    }

    @Test
    void findById_ShouldValidateIdAndReturnTrainerFromDao() {
        Trainer trainer = createTrainer();

        when(trainerDao.findById(TRAINER_ID)).thenReturn(Optional.of(trainer));

        Optional<Trainer> actual = service.findById(TRAINER_ID);

        assertTrue(actual.isPresent());
        assertSame(trainer, actual.get());
        verify(validator).validateId(TRAINER_ID, TRAINER);
        verify(trainerDao).findById(TRAINER_ID);
    }

    @Test
    void findAll_ShouldReturnAllTrainersFromDao() {
        List<Trainer> expected = List.of(createTrainer(), createSecondTrainer());

        when(trainerDao.findAll()).thenReturn(expected);

        List<Trainer> actual = service.findAll();

        assertEquals(expected, actual);
        verify(trainerDao).findAll();
    }

    @Test
    void update_ShouldPreserveExistingCredentialsAndUpdateTrainerData() {
        Trainer existingTrainer = createExistingTrainer();
        TrainingType newSpecialization = createTrainingType(FITNESS);
        Trainer trainerToUpdate = createTrainerToUpdate(newSpecialization);

        when(trainerDao.findById(TRAINER_ID)).thenReturn(Optional.of(existingTrainer));

        Trainer actual = service.update(TRAINER_ID, trainerToUpdate);
        verify(validator).validateTrainer(trainerToUpdate);
        verify(validator).validateUpdateId(TRAINER_ID, TRAINER_ID, TRAINER);
        verify(trainerDao).findById(TRAINER_ID);
        verify(trainerDao).update(eq(TRAINER_ID), trainerCaptor.capture());

        Trainer updatedTrainer = trainerCaptor.getValue();
        assertEquals(USERNAME, actual.getUsername());
        assertEquals(PASSWORD, actual.getPassword());
        assertEquals(FIRST_NAME, actual.getFirstName());
        assertEquals(LAST_NAME, actual.getLastName());
        assertTrue(actual.isActive());
        assertSame(newSpecialization, actual.getSpecialization());
        assertEquals(USERNAME, updatedTrainer.getUsername());
        assertEquals(PASSWORD, updatedTrainer.getPassword());
        assertSame(newSpecialization, updatedTrainer.getSpecialization());
    }

    @Test
    void update_ShouldThrowException_WhenTrainerDoesNotExist() {
        Trainer trainer = createTrainer();

        when(trainerDao.findById(TRAINER_ID)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> service.update(TRAINER_ID, trainer));

        assertEquals(TRAINER_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(validator).validateTrainer(trainer);
        verify(validator).validateUpdateId(TRAINER_ID, TRAINER_ID, TRAINER);
        verify(trainerDao).findById(TRAINER_ID);
        verify(trainerDao, never()).update(eq(TRAINER_ID), any());
    }

    private Trainer createTrainer() {
        return createTrainerToUpdate(createTrainingType(YOGA));
    }

    private Trainer createTrainerToUpdate(TrainingType specialization) {
        return Trainer.builder()
                .userId(TRAINER_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .active(true)
                .specialization(specialization)
                .build();
    }

    private Trainer createExistingTrainer() {
        return Trainer.builder()
                .userId(TRAINER_ID)
                .firstName("Old")
                .lastName(TRAINER)
                .username(USERNAME)
                .password(PASSWORD)
                .active(false)
                .specialization(createTrainingType(YOGA))
                .build();
    }

    private Trainer createSecondTrainer() {
        return Trainer.builder()
                .userId(SECOND_TRAINER_ID)
                .firstName("Anna")
                .lastName("Brown")
                .build();
    }

    private TrainingType createTrainingType(String trainingTypeName) {
        return TrainingType.builder()
                .trainingTypeName(trainingTypeName)
                .build();
    }
}