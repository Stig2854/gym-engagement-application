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
import com.gym.engagement.app.service.TrainingServiceImpl;
import com.gym.engagement.app.service.common.CoreValidator;
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
class TrainingServiceImplTest {

    private static final Long TRAINING_ID = 1L;

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private CoreValidator validator;

    private TrainingServiceImpl trainingService;

    @BeforeEach
    void setUp() {
        trainingService = new TrainingServiceImpl();
        trainingService.setTrainingDao(trainingDao);
        trainingService.setValidator(validator);
    }

    @Test
    void create_ShouldSaveAndReturnTraining_WhenTrainingIdIsUnique() {
        Training training = createTraining();

        when(trainingDao.findById(TRAINING_ID)).thenReturn(Optional.empty());

        Training result = trainingService.create(training);

        ArgumentCaptor<Training> trainingCaptor = ArgumentCaptor.forClass(Training.class);

        verify(validator).validateTraining(training);
        verify(trainingDao).findById(TRAINING_ID);
        verify(trainingDao).save(eq(TRAINING_ID), trainingCaptor.capture());

        assertSame(training, result);
        assertSame(training, trainingCaptor.getValue());
    }

    @Test
    void create_ShouldThrowException_WhenTrainingWithIdAlreadyExists() {
        Training training = createTraining();

        when(trainingDao.findById(TRAINING_ID)).thenReturn(Optional.of(training));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> trainingService.create(training));

        verify(validator).validateTraining(training);
        verify(trainingDao).findById(TRAINING_ID);
        verify(trainingDao, never()).save(eq(TRAINING_ID), any(Training.class));

        assertEquals("Training with ID 1 already exists", exception.getMessage());
    }

    @Test
    void findById_ShouldValidateIdAndReturnTrainingFromDao() {
        Training training = createTraining();

        when(trainingDao.findById(TRAINING_ID)).thenReturn(Optional.of(training));

        Optional<Training> result = trainingService.findById(TRAINING_ID);

        verify(validator).validateId(TRAINING_ID, "Training");
        verify(trainingDao).findById(TRAINING_ID);

        assertTrue(result.isPresent());
        assertSame(training, result.get());
    }

    @Test
    void findAll_ShouldReturnAllTrainingsFromDao() {
        Training firstTraining = createTraining();
        Training secondTraining = Training.builder()
                .id(2L)
                .traineeId(2L)
                .trainerId(3L)
                .trainingName("Strength training")
                .trainingType(TrainingType.builder()
                        .trainingTypeName("Strength")
                        .build())
                .trainingDate(LocalDate.of(2026, 9, 10))
                .trainingDuration(60)
                .build();

        List<Training> trainings = List.of(firstTraining, secondTraining);

        when(trainingDao.findAll()).thenReturn(trainings);

        List<Training> result = trainingService.findAll();

        verify(trainingDao).findAll();

        assertEquals(trainings, result);
    }

    private Training createTraining() {
        return Training.builder()
                .id(TRAINING_ID)
                .traineeId(10L)
                .trainerId(20L)
                .trainingName("Morning yoga")
                .trainingType(TrainingType.builder()
                        .trainingTypeName("Yoga")
                        .build())
                .trainingDate(LocalDate.of(2026, 9, 9))
                .trainingDuration(45)
                .build();
    }
}