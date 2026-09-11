package com.gym.engagement.app.service.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
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
import com.gym.engagement.app.service.TrainerServiceImpl;
import com.gym.engagement.app.service.common.CoreValidator;
import com.gym.engagement.app.service.common.ProfileCredentialGenerator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    private static final Long TRAINER_ID = 1L;
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Smith";
    private static final String USERNAME = "John.Smith";
    private static final String PASSWORD = "Abc123Xyz9";

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private CoreValidator validator;

    @Mock
    private ProfileCredentialGenerator credentialGenerator;

    private TrainerServiceImpl trainerService;

    @BeforeEach
    void setUp() {
        trainerService = new TrainerServiceImpl();
        trainerService.setTrainerDao(trainerDao);
        trainerService.setValidator(validator);
        trainerService.setCredentialGenerator(credentialGenerator);
    }

    @Test
    void create_ShouldGenerateCredentialsSaveAndReturnTrainer() {
        Trainer trainer = createTrainer();

        when(trainerDao.findById(TRAINER_ID)).thenReturn(Optional.empty());
        when(credentialGenerator.generateUsername(FIRST_NAME, LAST_NAME)).thenReturn(USERNAME);
        when(credentialGenerator.generatePassword()).thenReturn(PASSWORD);

        Trainer result = trainerService.create(trainer);

        ArgumentCaptor<Trainer> trainerCaptor = ArgumentCaptor.forClass(Trainer.class);

        verify(validator).validateTrainer(trainer);
        verify(trainerDao).findById(TRAINER_ID);
        verify(credentialGenerator).generateUsername(FIRST_NAME, LAST_NAME);
        verify(credentialGenerator).generatePassword();
        verify(trainerDao).save(eq(TRAINER_ID), trainerCaptor.capture());

        Trainer savedTrainer = trainerCaptor.getValue();

        assertAll(
                () -> assertEquals(TRAINER_ID, result.getUserId()),
                () -> assertEquals(FIRST_NAME, result.getFirstName()),
                () -> assertEquals(LAST_NAME, result.getLastName()),
                () -> assertEquals(USERNAME, result.getUsername()),
                () -> assertEquals(PASSWORD, result.getPassword()),
                () -> assertTrue(result.isActive()),
                () -> assertSame(trainer.getSpecialization(), result.getSpecialization()),
                () -> assertEquals(result.getUserId(), savedTrainer.getUserId()),
                () -> assertEquals(result.getUsername(), savedTrainer.getUsername()),
                () -> assertEquals(result.getPassword(), savedTrainer.getPassword())
        );
    }

    @Test
    void create_ShouldThrowException_WhenTrainerWithIdAlreadyExists() {
        Trainer trainer = createTrainer();

        when(trainerDao.findById(TRAINER_ID)).thenReturn(Optional.of(trainer));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> trainerService.create(trainer));

        verify(validator).validateTrainer(trainer);
        verify(trainerDao).findById(TRAINER_ID);
        verifyNoInteractions(credentialGenerator);
        verify(trainerDao, never()).save(eq(TRAINER_ID), any(Trainer.class));

        assertEquals("Trainer with ID 1 already exists", exception.getMessage());
    }

    @Test
    void findById_ShouldValidateIdAndReturnTrainerFromDao() {
        Trainer trainer = createTrainer();

        when(trainerDao.findById(TRAINER_ID)).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = trainerService.findById(TRAINER_ID);

        verify(validator).validateId(TRAINER_ID, "Trainer");
        verify(trainerDao).findById(TRAINER_ID);

        assertTrue(result.isPresent());
        assertSame(trainer, result.get());
    }

    @Test
    void findAll_ShouldReturnAllTrainersFromDao() {
        List<Trainer> trainers = List.of(
                createTrainer(),
                Trainer.builder()
                        .userId(2L)
                        .firstName("Anna")
                        .lastName("Brown")
                        .build());

        when(trainerDao.findAll()).thenReturn(trainers);

        List<Trainer> result = trainerService.findAll();

        verify(trainerDao).findAll();

        assertEquals(trainers, result);
    }

    @Test
    void update_ShouldPreserveExistingCredentialsAndUpdateTrainerData() {
        TrainingType oldSpecialization = TrainingType.builder()
                .trainingTypeName("Yoga")
                .build();

        Trainer existingTrainer = Trainer.builder()
                .userId(TRAINER_ID)
                .firstName("Old")
                .lastName("Trainer")
                .username(USERNAME)
                .password(PASSWORD)
                .active(false)
                .specialization(oldSpecialization)
                .build();

        TrainingType newSpecialization = TrainingType.builder()
                .trainingTypeName("Fitness")
                .build();

        Trainer trainerToUpdate = Trainer.builder()
                .userId(TRAINER_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .active(true)
                .specialization(newSpecialization)
                .build();

        when(trainerDao.findById(TRAINER_ID)).thenReturn(Optional.of(existingTrainer));

        Trainer result = trainerService.update(TRAINER_ID, trainerToUpdate);

        ArgumentCaptor<Trainer> trainerCaptor = ArgumentCaptor.forClass(Trainer.class);

        verify(validator).validateTrainer(trainerToUpdate);
        verify(validator).validateUpdateId(TRAINER_ID, TRAINER_ID, "Trainer");
        verify(trainerDao).findById(TRAINER_ID);
        verify(trainerDao).update(eq(TRAINER_ID), trainerCaptor.capture());

        Trainer updatedTrainer = trainerCaptor.getValue();

        assertAll(
                () -> assertEquals(USERNAME, result.getUsername()),
                () -> assertEquals(PASSWORD, result.getPassword()),
                () -> assertEquals(FIRST_NAME, result.getFirstName()),
                () -> assertEquals(LAST_NAME, result.getLastName()),
                () -> assertTrue(result.isActive()),
                () -> assertSame(newSpecialization, result.getSpecialization()),
                () -> assertEquals(USERNAME, updatedTrainer.getUsername()),
                () -> assertEquals(PASSWORD, updatedTrainer.getPassword()),
                () -> assertSame(newSpecialization, updatedTrainer.getSpecialization())
        );
    }

    @Test
    void update_ShouldThrowException_WhenTrainerDoesNotExist() {
        Trainer trainer = createTrainer();

        when(trainerDao.findById(TRAINER_ID)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> trainerService.update(TRAINER_ID, trainer));

        verify(validator).validateTrainer(trainer);
        verify(validator).validateUpdateId(TRAINER_ID, TRAINER_ID, "Trainer");
        verify(trainerDao).findById(TRAINER_ID);
        verify(trainerDao, never()).update(eq(TRAINER_ID), any(Trainer.class));

        assertEquals("Trainer not found with ID: 1", exception.getMessage());
    }

    private Trainer createTrainer() {
        TrainingType specialization = TrainingType.builder()
                .trainingTypeName("Yoga")
                .build();

        return Trainer.builder()
                .userId(TRAINER_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .active(true)
                .specialization(specialization)
                .build();
    }
}