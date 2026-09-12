package com.gym.engagement.app.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InitialDataParserTest {

    private static final Long TRAINEE_ID = 1L;
    private static final Long TRAINER_ID = 2L;
    private static final Long TRAINING_ID = 1L;

    private static final String YOGA = "Yoga";
    private static final LocalDate TRAINEE_DATE_OF_BIRTH = LocalDate.of(1998, 5, 12);
    private static final LocalDate TRAINING_DATE = LocalDate.of(2026, 9, 5);

    private InitialDataParser parser;

    @BeforeEach
    void setUp() {
        parser = new InitialDataParser();
    }

    @Test
    void parse_ShouldCreateTrainee_WhenLineContainsTraineeData() {
        String line = "TRAINEE,1,Oleksii,Koval,Oleksii.Koval,temporaryPass1,true,1998-05-12,Kyiv";

        Object actual = parser.parse(line);

        Trainee trainee = assertInstanceOf(Trainee.class, actual);
        assertEquals(TRAINEE_ID, trainee.getUserId());
        assertEquals("Oleksii", trainee.getFirstName());
        assertEquals("Koval", trainee.getLastName());
        assertEquals("Oleksii.Koval", trainee.getUsername());
        assertEquals("temporaryPass1", trainee.getPassword());
        assertTrue(trainee.isActive());
        assertEquals(TRAINEE_DATE_OF_BIRTH, trainee.getDateOfBirth());
        assertEquals("Kyiv", trainee.getAddress());
    }

    @Test
    void parse_ShouldCreateTrainer_WhenLineContainsTrainerData() {
        String line = "TRAINER,2,Marta,Shevchenko,Marta.Shevchenko,temporaryPass2,true,Yoga";

        Object actual = parser.parse(line);
        Trainer trainer = assertInstanceOf(Trainer.class, actual);
        assertEquals(TRAINER_ID, trainer.getUserId());
        assertEquals("Marta", trainer.getFirstName());
        assertEquals("Shevchenko", trainer.getLastName());
        assertEquals("Marta.Shevchenko", trainer.getUsername());
        assertEquals("temporaryPass2", trainer.getPassword());
        assertTrue(trainer.isActive());
        assertEquals(YOGA, trainer.getSpecialization().getTrainingTypeName());
    }

    @Test
    void parse_ShouldCreateTraining_WhenLineContainsTrainingData() {
        String line = "TRAINING,1,1,2,Morning Yoga,Yoga,2026-09-05,60";

        Object actual = parser.parse(line);
        Training training = assertInstanceOf(Training.class, actual);
        assertEquals(TRAINING_ID, training.getId());
        assertEquals(TRAINEE_ID, training.getTraineeId());
        assertEquals(TRAINER_ID, training.getTrainerId());
        assertEquals("Morning Yoga", training.getTrainingName());
        assertEquals(YOGA, training.getTrainingType().getTrainingTypeName());
        assertEquals(TRAINING_DATE, training.getTrainingDate());
        assertEquals(60, training.getTrainingDuration());
    }

    @Test
    void parse_ShouldThrowException_WhenRecordTypeIsUnsupported() {
        String line = "ADMIN,1,Test,User";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> parser.parse(line));

        assertEquals("Unsupported initial data record type: ADMIN", exception.getMessage());
    }
}