package com.gym.engagement.app.service.common;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreValidatorTest {

    private CoreValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CoreValidator();
    }

    @Test
    void validateId_ShouldNotThrowException_WhenIdIsValid() {
        assertDoesNotThrow(() -> validator.validateId(1L, "Trainee"));
    }

    @Test
    void validateId_ShouldThrowException_WhenIdIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateId(null, "Trainee"));

        assertEquals("Trainee ID cannot be null", exception.getMessage());
    }

    @Test
    void validateTrainee_ShouldThrowException_WhenTraineeIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateTrainee(null));

        assertEquals("Trainee cannot be null", exception.getMessage());
    }

    @Test
    void validateTrainee_ShouldThrowException_WhenTraineeIdIsNull() {
        Trainee trainee = Trainee.builder().build();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateTrainee(trainee));

        assertEquals("Trainee ID cannot be null", exception.getMessage());
    }

    @Test
    void validateTrainee_ShouldNotThrowException_WhenTraineeIsValid() {
        Trainee trainee = Trainee.builder()
                .userId(1L)
                .build();

        assertDoesNotThrow(() -> validator.validateTrainee(trainee));
    }

    @Test
    void validateTrainer_ShouldThrowException_WhenTrainerIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateTrainer(null));

        assertEquals("Trainer cannot be null", exception.getMessage());
    }

    @Test
    void validateTrainer_ShouldNotThrowException_WhenTrainerIsValid() {
        Trainer trainer = Trainer.builder()
                .userId(1L)
                .build();

        assertDoesNotThrow(() -> validator.validateTrainer(trainer));
    }

    @Test
    void validateTraining_ShouldThrowException_WhenTrainingIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateTraining(null));

        assertEquals("Training cannot be null", exception.getMessage());
    }

    @Test
    void validateTraining_ShouldNotThrowException_WhenTrainingIsValid() {
        Training training = Training.builder()
                .id(1L)
                .build();

        assertDoesNotThrow(() -> validator.validateTraining(training));
    }

    @Test
    void validateUpdateId_ShouldNotThrowException_WhenIdsMatch() {
        assertDoesNotThrow(() -> validator.validateUpdateId(1L, 1L, "Trainee"));
    }

    @Test
    void validateUpdateId_ShouldThrowException_WhenIdsDoNotMatch() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateUpdateId(1L, 2L, "Trainee"));

        assertEquals("Trainee ID does not match update ID", exception.getMessage());
    }
}