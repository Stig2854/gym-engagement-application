package com.gym.engagement.app.service.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.gym.engagement.app.dao.TraineeDao;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.service.common.CoreValidator;
import com.gym.engagement.app.service.common.ProfileCredentialGenerator;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    private static final Long TRAINEE_ID = 1L;
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Smith";
    private static final String USERNAME = "John.Smith";
    private static final String PASSWORD = "Abc123Xyz9";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1995, 5, 10);
    private static final String ADDRESS = "Kyiv, Ukraine";

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private CoreValidator validator;

    @Mock
    private ProfileCredentialGenerator credentialGenerator;

    private TraineeServiceImpl traineeService;

    @BeforeEach
    void setUp() {
        traineeService = new TraineeServiceImpl();
        traineeService.setTraineeDao(traineeDao);
        traineeService.setValidator(validator);
        traineeService.setCredentialGenerator(credentialGenerator);
    }

    @Test
    void create_ShouldGenerateCredentialsSaveAndReturnTrainee() {
        Trainee trainee = createTrainee();

        when(traineeDao.findById(TRAINEE_ID)).thenReturn(Optional.empty());
        when(credentialGenerator.generateUsername(FIRST_NAME, LAST_NAME)).thenReturn(USERNAME);
        when(credentialGenerator.generatePassword()).thenReturn(PASSWORD);

        Trainee result = traineeService.create(trainee);

        ArgumentCaptor<Trainee> traineeCaptor = ArgumentCaptor.forClass(Trainee.class);
        verify(validator).validateTrainee(trainee);
        verify(traineeDao).findById(TRAINEE_ID);
        verify(credentialGenerator).generateUsername(FIRST_NAME, LAST_NAME);
        verify(credentialGenerator).generatePassword();
        verify(traineeDao).save(eq(TRAINEE_ID), traineeCaptor.capture());

        Trainee savedTrainee = traineeCaptor.getValue();

        assertAll(
                () -> assertEquals(TRAINEE_ID, result.getUserId()),
                () -> assertEquals(FIRST_NAME, result.getFirstName()),
                () -> assertEquals(LAST_NAME, result.getLastName()),
                () -> assertEquals(USERNAME, result.getUsername()),
                () -> assertEquals(PASSWORD, result.getPassword()),
                () -> assertEquals(DATE_OF_BIRTH, result.getDateOfBirth()),
                () -> assertEquals(ADDRESS, result.getAddress()),
                () -> assertTrue(result.isActive()),
                () -> assertEquals(result.getUserId(), savedTrainee.getUserId()),
                () -> assertEquals(result.getUsername(), savedTrainee.getUsername()),
                () -> assertEquals(result.getPassword(), savedTrainee.getPassword())
        );
    }

    @Test
    void create_ShouldThrowException_WhenTraineeWithIdAlreadyExists() {
        Trainee trainee = createTrainee();

        when(traineeDao.findById(TRAINEE_ID)).thenReturn(Optional.of(trainee));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> traineeService.create(trainee));

        verify(validator).validateTrainee(trainee);
        verify(traineeDao).findById(TRAINEE_ID);
        verifyNoInteractions(credentialGenerator);
        verify(traineeDao, never()).save(eq(TRAINEE_ID), eq(trainee));

        assertEquals("Trainee with ID 1 already exists", exception.getMessage());
    }

    @Test
    void findById_ShouldValidateIdAndReturnTraineeFromDao() {
        Trainee trainee = createTrainee();
        when(traineeDao.findById(TRAINEE_ID)).thenReturn(Optional.of(trainee));

        Optional<Trainee> result = traineeService.findById(TRAINEE_ID);

        verify(validator).validateId(TRAINEE_ID, "Trainee");
        verify(traineeDao).findById(TRAINEE_ID);

        assertTrue(result.isPresent());
        assertSame(trainee, result.get());
    }

    @Test
    void findAll_ShouldReturnAllTraineesFromDao() {
        List<Trainee> trainees = List.of(
                createTrainee(),
                Trainee.builder()
                        .userId(2L)
                        .firstName("Anna")
                        .lastName("Brown")
                        .build());

        when(traineeDao.findAll()).thenReturn(trainees);

        List<Trainee> result = traineeService.findAll();

        verify(traineeDao).findAll();
        assertEquals(trainees, result);
    }

    @Test
    void update_ShouldPreserveExistingCredentialsAndUpdateTraineeData() {
        Trainee existingTrainee = Trainee.builder()
                .userId(TRAINEE_ID)
                .firstName("Old")
                .lastName("Name")
                .username(USERNAME)
                .password(PASSWORD)
                .active(false)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Old address")
                .build();

        Trainee traineeToUpdate = createTrainee();

        when(traineeDao.findById(TRAINEE_ID)).thenReturn(Optional.of(existingTrainee));

        Trainee result = traineeService.update(TRAINEE_ID, traineeToUpdate);

        ArgumentCaptor<Trainee> traineeCaptor = ArgumentCaptor.forClass(Trainee.class);
        verify(validator).validateTrainee(traineeToUpdate);
        verify(validator).validateUpdateId(TRAINEE_ID, TRAINEE_ID, "Trainee");
        verify(traineeDao).findById(TRAINEE_ID);
        verify(traineeDao).update(eq(TRAINEE_ID), traineeCaptor.capture());

        Trainee updatedTrainee = traineeCaptor.getValue();

        assertAll(
                () -> assertEquals(USERNAME, result.getUsername()),
                () -> assertEquals(PASSWORD, result.getPassword()),
                () -> assertEquals(FIRST_NAME, result.getFirstName()),
                () -> assertEquals(LAST_NAME, result.getLastName()),
                () -> assertEquals(DATE_OF_BIRTH, result.getDateOfBirth()),
                () -> assertEquals(ADDRESS, result.getAddress()),
                () -> assertEquals(USERNAME, updatedTrainee.getUsername()),
                () -> assertEquals(PASSWORD, updatedTrainee.getPassword())
        );
    }

    @Test
    void update_ShouldThrowException_WhenTraineeDoesNotExist() {
        Trainee trainee = createTrainee();
        when(traineeDao.findById(TRAINEE_ID)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> traineeService.update(TRAINEE_ID, trainee));

        verify(validator).validateTrainee(trainee);
        verify(validator).validateUpdateId(TRAINEE_ID, TRAINEE_ID, "Trainee");
        verify(traineeDao).findById(TRAINEE_ID);
        verify(traineeDao, never()).update(eq(TRAINEE_ID), eq(trainee));

        assertEquals("Trainee not found with ID: 1", exception.getMessage());
    }

    @Test
    void deleteById_ShouldDeleteTrainee_WhenTraineeExists() {
        Trainee trainee = createTrainee();
        when(traineeDao.findById(TRAINEE_ID)).thenReturn(Optional.of(trainee));

        traineeService.deleteById(TRAINEE_ID);

        verify(validator).validateId(TRAINEE_ID, "Trainee");
        verify(traineeDao).findById(TRAINEE_ID);
        verify(traineeDao).deleteById(TRAINEE_ID);
    }

    @Test
    void deleteById_ShouldThrowException_WhenTraineeDoesNotExist() {
        when(traineeDao.findById(TRAINEE_ID)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> traineeService.deleteById(TRAINEE_ID));

        verify(validator).validateId(TRAINEE_ID, "Trainee");
        verify(traineeDao).findById(TRAINEE_ID);
        verify(traineeDao, never()).deleteById(TRAINEE_ID);

        assertEquals("Trainee not found with ID: 1", exception.getMessage());
    }

    private Trainee createTrainee() {
        return Trainee.builder()
                .userId(TRAINEE_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .active(true)
                .dateOfBirth(DATE_OF_BIRTH)
                .address(ADDRESS)
                .build();
    }
}