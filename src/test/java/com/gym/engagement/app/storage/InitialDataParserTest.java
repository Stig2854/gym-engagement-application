package com.gym.engagement.app.storage;

import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InitialDataParserTest {

    private InitialDataParser initialDataParser;

    @BeforeEach
    void setUp() {
        initialDataParser = new InitialDataParser();
    }

    @Test
    void parse_ShouldCreateTrainee_WhenLineContainsTraineeData() {
        String line = "TRAINEE,1,Oleksii,Koval,Oleksii.Koval,temporaryPass1,true,1998-05-12,Kyiv";

        Object parsedEntity = initialDataParser.parse(line);

        Trainee trainee = assertInstanceOf(Trainee.class, parsedEntity);

        assertAll(
                () -> assertEquals(1L, trainee.getUserId()),
                () -> assertEquals("Oleksii", trainee.getFirstName()),
                () -> assertEquals("Koval", trainee.getLastName()),
                () -> assertEquals("Oleksii.Koval", trainee.getUsername()),
                () -> assertEquals("temporaryPass1", trainee.getPassword()),
                () -> assertEquals(true, trainee.isActive()),
                () -> assertEquals(LocalDate.of(1998, 5, 12), trainee.getDateOfBirth()),
                () -> assertEquals("Kyiv", trainee.getAddress())
        );
    }

    @Test
    void parse_ShouldCreateTrainer_WhenLineContainsTrainerData() {
        String line = "TRAINER,2,Marta,Shevchenko,Marta.Shevchenko,temporaryPass2,true,Yoga";

        Object parsedEntity = initialDataParser.parse(line);

        Trainer trainer = assertInstanceOf(Trainer.class, parsedEntity);

        assertAll(
                () -> assertEquals(2L, trainer.getUserId()),
                () -> assertEquals("Marta", trainer.getFirstName()),
                () -> assertEquals("Shevchenko", trainer.getLastName()),
                () -> assertEquals("Marta.Shevchenko", trainer.getUsername()),
                () -> assertEquals("temporaryPass2", trainer.getPassword()),
                () -> assertEquals(true, trainer.isActive()),
                () -> assertEquals(
                        "Yoga",
                        trainer.getSpecialization().getTrainingTypeName())
        );
    }

    @Test
    void parse_ShouldCreateTraining_WhenLineContainsTrainingData() {
        String line = "TRAINING,1,1,2,Morning Yoga,Yoga,2026-09-05,60";

        Object parsedEntity = initialDataParser.parse(line);

        Training training = assertInstanceOf(Training.class, parsedEntity);

        assertAll(
                () -> assertEquals(1L, training.getId()),
                () -> assertEquals(1L, training.getTraineeId()),
                () -> assertEquals(2L, training.getTrainerId()),
                () -> assertEquals("Morning Yoga", training.getTrainingName()),
                () -> assertEquals(
                        "Yoga",
                        training.getTrainingType().getTrainingTypeName()),
                () -> assertEquals(LocalDate.of(2026, 9, 5), training.getTrainingDate()),
                () -> assertEquals(60, training.getTrainingDuration())
        );
    }

    @Test
    void parse_ShouldThrowException_WhenRecordTypeIsUnsupported() {
        String line = "ADMIN,1,Test,User";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> initialDataParser.parse(line));

        assertEquals(
                "Unsupported initial data record type: ADMIN",
                exception.getMessage());
    }
}