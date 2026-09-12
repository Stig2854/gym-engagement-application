package com.gym.engagement.app.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.gym.engagement.app.dao.TraineeDao;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.service.common.CoreValidator;
import com.gym.engagement.app.service.common.ProfileCredentialGenerator;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    private static final Long TRAINEE_ID = 1L;
    private static final Long SECOND_TRAINEE_ID = 2L;

    private static final String TRAINEE = "Trainee";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Smith";
    private static final String USERNAME = "John.Smith";
    private static final String PASSWORD = "Abc123Xyz9";
    private static final String HASHED_PASSWORD = "$2a$10$hashedPasswordForTest";
    private static final String ADDRESS = "Kyiv, Ukraine";
    private static final String TRAINEE_NOT_FOUND_MESSAGE = "Trainee not found with ID: 1";
    private static final String TRAINEE_ALREADY_EXISTS_MESSAGE = "Trainee with ID 1 already exists";

    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1995, 5, 10);

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private CoreValidator validator;

    @Mock
    private ProfileCredentialGenerator credentialGenerator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<Trainee> traineeCaptor;

    private TraineeServiceImpl service;

    private Logger logger;
    private ListAppender<ILoggingEvent> logAppender;

    @BeforeEach
    void setUp() {
        service = new TraineeServiceImpl();
        service.setTraineeDao(traineeDao);
        service.setValidator(validator);
        service.setCredentialGenerator(credentialGenerator);
        service.setPasswordEncoder(passwordEncoder);

        logger = (Logger) LoggerFactory.getLogger(TraineeServiceImpl.class);
        logAppender = new ListAppender<>();
        logAppender.start();
        logger.addAppender(logAppender);
    }

    @AfterEach
    void tearDown() {
        logger.detachAppender(logAppender);
        logAppender.stop();
    }

    @Test
    void create_ShouldLogInfo_WhenCreatingTrainee() {
        Trainee trainee = createTrainee();

        when(traineeDao.findById(TRAINEE_ID)).thenReturn(Optional.empty());
        when(credentialGenerator.generateUsername(FIRST_NAME, LAST_NAME)).thenReturn(USERNAME);
        when(credentialGenerator.generatePassword()).thenReturn(PASSWORD);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(HASHED_PASSWORD);

        service.create(trainee);

        assertEquals(1, logAppender.list.size());
        Iterator<ILoggingEvent> iterator = logAppender.list.iterator();
        ILoggingEvent event = iterator.next();
        assertEquals(Level.INFO, event.getLevel());
        assertEquals("Created trainee with ID: " + TRAINEE_ID, event.getFormattedMessage());
        assertFalse(event.getFormattedMessage().contains(PASSWORD));
        assertFalse(event.getFormattedMessage().contains(HASHED_PASSWORD));
    }

    @Test
    void create_ShouldGenerateCredentialsSaveAndReturnTrainee() {
        Trainee trainee = createTrainee();

        when(traineeDao.findById(TRAINEE_ID)).thenReturn(Optional.empty());
        when(credentialGenerator.generateUsername(FIRST_NAME, LAST_NAME)).thenReturn(USERNAME);
        when(credentialGenerator.generatePassword()).thenReturn(PASSWORD);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(HASHED_PASSWORD);

        Trainee actual = service.create(trainee);

        verify(validator).validateTrainee(trainee);
        verify(traineeDao).findById(TRAINEE_ID);
        verify(credentialGenerator).generateUsername(FIRST_NAME, LAST_NAME);
        verify(credentialGenerator).generatePassword();
        verify(passwordEncoder).encode(PASSWORD);
        verify(traineeDao).save(eq(TRAINEE_ID), traineeCaptor.capture());

        Trainee savedTrainee = traineeCaptor.getValue();

        assertEquals(TRAINEE_ID, actual.getUserId());
        assertEquals(FIRST_NAME, actual.getFirstName());
        assertEquals(LAST_NAME, actual.getLastName());
        assertEquals(USERNAME, actual.getUsername());
        assertEquals(PASSWORD, actual.getPassword());
        assertEquals(DATE_OF_BIRTH, actual.getDateOfBirth());
        assertEquals(ADDRESS, actual.getAddress());
        assertTrue(actual.isActive());
        assertEquals(TRAINEE_ID, savedTrainee.getUserId());
        assertEquals(USERNAME, savedTrainee.getUsername());
        assertEquals(HASHED_PASSWORD, savedTrainee.getPassword());
    }

    @Test
    void create_ShouldThrowException_WhenTraineeWithIdAlreadyExists() {
        Trainee trainee = createTrainee();

        when(traineeDao.findById(TRAINEE_ID)).thenReturn(Optional.of(trainee));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> service.create(trainee));

        assertEquals(TRAINEE_ALREADY_EXISTS_MESSAGE, exception.getMessage());
        verify(validator).validateTrainee(trainee);
        verify(traineeDao).findById(TRAINEE_ID);
        verifyNoInteractions(credentialGenerator, passwordEncoder);
        verify(traineeDao, never()).save(eq(TRAINEE_ID), any());
    }

    @Test
    void findById_ShouldValidateIdAndReturnTraineeFromDao() {
        Trainee trainee = createTrainee();

        when(traineeDao.findById(TRAINEE_ID)).thenReturn(Optional.of(trainee));

        Optional<Trainee> actual = service.findById(TRAINEE_ID);

        assertTrue(actual.isPresent());
        assertSame(trainee, actual.get());
        verify(validator).validateId(TRAINEE_ID, TRAINEE);
        verify(traineeDao).findById(TRAINEE_ID);
    }

    @Test
    void findAll_ShouldReturnAllTraineesFromDao() {
        List<Trainee> expected = List.of(createTrainee(), createSecondTrainee());

        when(traineeDao.findAll()).thenReturn(expected);

        List<Trainee> actual = service.findAll();

        assertEquals(expected, actual);
        verify(traineeDao).findAll();
    }

    @Test
    void update_ShouldPreserveExistingCredentialsAndUpdateTraineeData() {
        Trainee existingTrainee = createExistingTrainee();
        Trainee traineeToUpdate = createTrainee();

        when(traineeDao.findById(TRAINEE_ID)).thenReturn(Optional.of(existingTrainee));

        Trainee actual = service.update(TRAINEE_ID, traineeToUpdate);
        verify(validator).validateTrainee(traineeToUpdate);
        verify(validator).validateUpdateId(TRAINEE_ID, TRAINEE_ID, TRAINEE);
        verify(traineeDao).findById(TRAINEE_ID);
        verify(traineeDao).update(eq(TRAINEE_ID), traineeCaptor.capture());
        verifyNoInteractions(passwordEncoder);

        Trainee updatedTrainee = traineeCaptor.getValue();
        assertEquals(USERNAME, actual.getUsername());
        assertEquals(HASHED_PASSWORD, actual.getPassword());
        assertEquals(FIRST_NAME, actual.getFirstName());
        assertEquals(LAST_NAME, actual.getLastName());
        assertEquals(DATE_OF_BIRTH, actual.getDateOfBirth());
        assertEquals(ADDRESS, actual.getAddress());
        assertEquals(USERNAME, updatedTrainee.getUsername());
        assertEquals(HASHED_PASSWORD, updatedTrainee.getPassword());
    }

    @Test
    void update_ShouldThrowException_WhenTraineeDoesNotExist() {
        Trainee trainee = createTrainee();

        when(traineeDao.findById(TRAINEE_ID)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> service.update(TRAINEE_ID, trainee));

        assertEquals(TRAINEE_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(validator).validateTrainee(trainee);
        verify(validator).validateUpdateId(TRAINEE_ID, TRAINEE_ID, TRAINEE);
        verify(traineeDao).findById(TRAINEE_ID);
        verify(traineeDao, never()).update(eq(TRAINEE_ID), any());
    }

    @Test
    void deleteById_ShouldDeleteTrainee_WhenTraineeExists() {
        Trainee trainee = createTrainee();

        when(traineeDao.findById(TRAINEE_ID)).thenReturn(Optional.of(trainee));

        service.deleteById(TRAINEE_ID);

        verify(validator).validateId(TRAINEE_ID, TRAINEE);
        verify(traineeDao).findById(TRAINEE_ID);
        verify(traineeDao).deleteById(TRAINEE_ID);
    }

    @Test
    void deleteById_ShouldThrowException_WhenTraineeDoesNotExist() {
        when(traineeDao.findById(TRAINEE_ID)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> service.deleteById(TRAINEE_ID));

        assertEquals(TRAINEE_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(validator).validateId(TRAINEE_ID, TRAINEE);
        verify(traineeDao).findById(TRAINEE_ID);
        verify(traineeDao, never()).deleteById(TRAINEE_ID);
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

    private Trainee createSecondTrainee() {
        return Trainee.builder()
                .userId(SECOND_TRAINEE_ID)
                .firstName("Anna")
                .lastName("Brown")
                .build();
    }

    private Trainee createExistingTrainee() {
        return Trainee.builder()
                .userId(TRAINEE_ID)
                .firstName("Old")
                .lastName("Name")
                .username(USERNAME)
                .password(HASHED_PASSWORD)
                .active(false)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Old address")
                .build();
    }
}