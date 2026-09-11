package com.gym.engagement.app.service.common;

import com.gym.engagement.app.dao.TraineeDao;
import com.gym.engagement.app.dao.TrainerDao;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileCredentialGeneratorTest {

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Smith";

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    private ProfileCredentialGenerator credentialGenerator;

    @BeforeEach
    void setUp() {
        credentialGenerator = new ProfileCredentialGenerator();
        credentialGenerator.setTraineeDao(traineeDao);
        credentialGenerator.setTrainerDao(trainerDao);
    }

    @Test
    void generateUsername_ShouldReturnBaseUsername_WhenNoProfilesWithSameNameExist() {
        when(traineeDao.findAll()).thenReturn(List.of());
        when(trainerDao.findAll()).thenReturn(List.of());

        String username = credentialGenerator.generateUsername(FIRST_NAME, LAST_NAME);

        assertEquals("John.Smith", username);
    }

    @Test
    void generateUsername_ShouldAddSuffix_WhenTraineeWithSameNameAlreadyExists() {
        Trainee existingTrainee = createTrainee(
                1L,
                FIRST_NAME,
                LAST_NAME,
                "John.Smith");

        when(traineeDao.findAll()).thenReturn(List.of(existingTrainee));
        when(trainerDao.findAll()).thenReturn(List.of());

        String username = credentialGenerator.generateUsername(FIRST_NAME, LAST_NAME);

        assertEquals("John.Smith1", username);
    }

    @Test
    void generateUsername_ShouldCountTraineesAndTrainers_WhenProfilesWithSameNameExist() {
        Trainee existingTrainee = createTrainee(
                1L,
                FIRST_NAME,
                LAST_NAME,
                "John.Smith");

        Trainer existingTrainer = createTrainer(
                2L,
                FIRST_NAME,
                LAST_NAME,
                "John.Smith1");

        when(traineeDao.findAll()).thenReturn(List.of(existingTrainee));
        when(trainerDao.findAll()).thenReturn(List.of(existingTrainer));

        String username = credentialGenerator.generateUsername(FIRST_NAME, LAST_NAME);

        assertEquals("John.Smith2", username);
    }

    @Test
    void generateUsername_ShouldFindNextAvailableSuffix_WhenUsernameIsAlreadyTakenByAnotherProfile() {
        Trainee traineeWithDifferentName = createTrainee(
                1L,
                "Alex",
                "Brown",
                "John.Smith");

        Trainer trainerWithDifferentName = createTrainer(
                2L,
                "Emma",
                "Wilson",
                "John.Smith1");

        when(traineeDao.findAll()).thenReturn(List.of(traineeWithDifferentName));
        when(trainerDao.findAll()).thenReturn(List.of(trainerWithDifferentName));

        String username = credentialGenerator.generateUsername(FIRST_NAME, LAST_NAME);

        assertEquals("John.Smith2", username);
    }

    @Test
    void generateUsername_ShouldThrowException_WhenFirstNameIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> credentialGenerator.generateUsername(null, LAST_NAME));

        assertEquals("First name cannot be null or blank", exception.getMessage());
    }

    @Test
    void generateUsername_ShouldThrowException_WhenLastNameIsBlank() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> credentialGenerator.generateUsername(FIRST_NAME, "  "));

        assertEquals("Last name cannot be null or blank", exception.getMessage());
    }

    @Test
    void generatePassword_ShouldReturnPasswordWithTenAllowedCharacters() {
        String password = credentialGenerator.generatePassword();

        assertEquals(10, password.length());
        assertTrue(password.matches("[A-Za-z0-9]{10}"));
    }

    private Trainee createTrainee(
            Long userId,
            String firstName,
            String lastName,
            String username) {

        return Trainee.builder()
                .userId(userId)
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .build();
    }

    private Trainer createTrainer(
            Long userId,
            String firstName,
            String lastName,
            String username) {

        return Trainer.builder()
                .userId(userId)
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .build();
    }
}