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

    private static final Long VALID_ID = 1L;
    private static final Long DIFFERENT_ID = 2L;

    private static final String TRAINEE = "Trainee";

    private CoreValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CoreValidator();
    }

    @Test
    void validateId_ShouldNotThrowException_WhenIdIsValid() {
        assertDoesNotThrow(() -> validator.validateId(VALID_ID, TRAINEE));
    }

    @Test
    void validateId_ShouldThrowException_WhenIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validateId(null, TRAINEE));

        assertEquals("Trainee ID cannot be null", exception.getMessage());
    }

    @Test
    void validateTrainee_ShouldThrowException_WhenTraineeIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validateTrainee(null));

        assertEquals("Trainee cannot be null", exception.getMessage());
    }

    @Test
    void validateTrainee_ShouldThrowException_WhenTraineeIdIsNull() {
        Trainee trainee = Trainee.builder().build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validateTrainee(trainee));

        assertEquals("Trainee ID cannot be null", exception.getMessage());
    }

    @Test
    void validateTrainee_ShouldNotThrowException_WhenTraineeIsValid() {
        Trainee trainee = Trainee.builder().userId(VALID_ID).build();

        assertDoesNotThrow(() -> validator.validateTrainee(trainee));
    }

    @Test
    void validateTrainer_ShouldThrowException_WhenTrainerIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validateTrainer(null));

        assertEquals("Trainer cannot be null", exception.getMessage());
    }

    @Test
    void validateTrainer_ShouldNotThrowException_WhenTrainerIsValid() {
        Trainer trainer = Trainer.builder().userId(VALID_ID).build();

        assertDoesNotThrow(() -> validator.validateTrainer(trainer));
    }

    @Test
    void validateTraining_ShouldThrowException_WhenTrainingIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validateTraining(null));

        assertEquals("Training cannot be null", exception.getMessage());
    }

    @Test
    void validateTraining_ShouldNotThrowException_WhenTrainingIsValid() {
        Training training = Training.builder().id(VALID_ID).build();

        assertDoesNotThrow(() -> validator.validateTraining(training));
    }

    @Test
    void validateUpdateId_ShouldNotThrowException_WhenIdsMatch() {
        assertDoesNotThrow(() -> validator.validateUpdateId(VALID_ID, VALID_ID, TRAINEE));
    }

    @Test
    void validateUpdateId_ShouldThrowException_WhenIdsDoNotMatch() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validateUpdateId(VALID_ID, DIFFERENT_ID, TRAINEE));

        assertEquals("Trainee ID does not match update ID", exception.getMessage());
    }
}