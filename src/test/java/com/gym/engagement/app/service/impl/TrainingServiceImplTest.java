package com.gym.engagement.app.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gym.engagement.app.dao.TrainingDao;
import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.model.TrainingType;
import com.gym.engagement.app.service.common.CoreValidator;

import java.time.LocalDate;
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
class TrainingServiceImplTest {

    private static final Long TRAINING_ID = 1L;
    private static final Long SECOND_TRAINING_ID = 2L;

    private static final String TRAINING = "Training";
    private static final String YOGA = "Yoga";
    private static final String STRENGTH = "Strength";
    private static final String MORNING_YOGA = "Morning yoga";
    private static final String STRENGTH_TRAINING = "Strength training";

    private static final LocalDate FIRST_TRAINING_DATE = LocalDate.of(2026, 9, 9);
    private static final LocalDate SECOND_TRAINING_DATE = LocalDate.of(2026, 9, 10);

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private CoreValidator validator;

    @Captor
    private ArgumentCaptor<Training> trainingCaptor;

    private TrainingServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TrainingServiceImpl();
        service.setTrainingDao(trainingDao);
        service.setValidator(validator);
    }

    @Test
    void create_ShouldSaveAndReturnTraining_WhenTrainingIdIsUnique() {
        Training training = createTraining();

        when(trainingDao.findById(TRAINING_ID)).thenReturn(Optional.empty());

        Training actual = service.create(training);

        verify(validator).validateTraining(training);
        verify(trainingDao).findById(TRAINING_ID);
        verify(trainingDao).save(eq(TRAINING_ID), trainingCaptor.capture());

        assertSame(training, actual);
        assertSame(training, trainingCaptor.getValue());
    }

    @Test
    void create_ShouldThrowException_WhenTrainingWithIdAlreadyExists() {
        Training training = createTraining();

        when(trainingDao.findById(TRAINING_ID)).thenReturn(Optional.of(training));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> service.create(training));

        assertEquals("Training with ID 1 already exists", exception.getMessage());
        verify(validator).validateTraining(training);
        verify(trainingDao).findById(TRAINING_ID);
        verify(trainingDao, never()).save(eq(TRAINING_ID), any());
    }

    @Test
    void findById_ShouldValidateIdAndReturnTrainingFromDao() {
        Training training = createTraining();

        when(trainingDao.findById(TRAINING_ID)).thenReturn(Optional.of(training));

        Optional<Training> actual = service.findById(TRAINING_ID);

        assertTrue(actual.isPresent());
        assertSame(training, actual.get());
        verify(validator).validateId(TRAINING_ID, TRAINING);
        verify(trainingDao).findById(TRAINING_ID);
    }

    @Test
    void findAll_ShouldReturnAllTrainingsFromDao() {
        List<Training> expected = List.of(createTraining(), createSecondTraining());

        when(trainingDao.findAll()).thenReturn(expected);

        List<Training> actual = service.findAll();

        assertEquals(expected, actual);
        verify(trainingDao).findAll();
    }

    private Training createTraining() {
        TrainingType trainingType = createTrainingType(YOGA);

        return Training.builder()
                .id(TRAINING_ID)
                .traineeId(10L)
                .trainerId(20L)
                .trainingName(MORNING_YOGA)
                .trainingType(trainingType)
                .trainingDate(FIRST_TRAINING_DATE)
                .trainingDuration(45)
                .build();
    }

    private Training createSecondTraining() {
        TrainingType trainingType = createTrainingType(STRENGTH);

        return Training.builder()
                .id(SECOND_TRAINING_ID)
                .traineeId(2L)
                .trainerId(3L)
                .trainingName(STRENGTH_TRAINING)
                .trainingType(trainingType)
                .trainingDate(SECOND_TRAINING_DATE)
                .trainingDuration(60)
                .build();
    }

    private TrainingType createTrainingType(String trainingTypeName) {
        return TrainingType.builder().trainingTypeName(trainingTypeName).build();
    }
}