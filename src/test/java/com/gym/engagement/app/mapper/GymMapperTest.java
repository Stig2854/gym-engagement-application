package com.gym.engagement.app.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gym.engagement.app.dto.TraineeDto;
import com.gym.engagement.app.dto.TrainerDto;
import com.gym.engagement.app.dto.TrainingDto;
import com.gym.engagement.app.dto.TrainingTypeDto;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.model.TrainingType;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class GymMapperTest {

    private static final Long TRAINEE_ID = 1L;
    private static final Long TRAINER_ID = 2L;
    private static final Long TRAINING_ID = 3L;

    private static final String TRAINEE_FIRST_NAME = "John";
    private static final String TRAINEE_LAST_NAME = "Smith";
    private static final String TRAINEE_USERNAME = "John.Smith";

    private static final String TRAINER_FIRST_NAME = "Anna";
    private static final String TRAINER_LAST_NAME = "Brown";
    private static final String TRAINER_USERNAME = "Anna.Brown";

    private static final String PASSWORD = "Abc123Xyz9";
    private static final String ADDRESS = "Kyiv, Ukraine";
    private static final String YOGA_TRAINING_TYPE = "Yoga";
    private static final String STRENGTH_TRAINING_TYPE = "Strength";
    private static final String TRAINING_NAME = "Morning strength training";

    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1995, 5, 10);
    private static final LocalDate TRAINING_DATE = LocalDate.of(2026, 9, 10);

    private GymMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(GymMapper.class);
    }

    @Test
    void toEntity_ShouldMapTraineeDtoWithoutCredentials() {
        TraineeDto traineeDto = createTraineeDto();

        Trainee actual = mapper.toEntity(traineeDto);

        assertEquals(TRAINEE_ID, actual.getUserId());
        assertEquals(TRAINEE_FIRST_NAME, actual.getFirstName());
        assertEquals(TRAINEE_LAST_NAME, actual.getLastName());
        assertTrue(actual.isActive());
        assertEquals(DATE_OF_BIRTH, actual.getDateOfBirth());
        assertEquals(ADDRESS, actual.getAddress());
        assertNull(actual.getUsername());
        assertNull(actual.getPassword());
    }

    @Test
    void toDto_ShouldMapTrainee() {
        Trainee trainee = createTrainee();

        TraineeDto actual = mapper.toDto(trainee);

        assertEquals(TRAINEE_ID, actual.getUserId());
        assertEquals(TRAINEE_FIRST_NAME, actual.getFirstName());
        assertEquals(TRAINEE_LAST_NAME, actual.getLastName());
        assertTrue(actual.isActive());
        assertEquals(DATE_OF_BIRTH, actual.getDateOfBirth());
        assertEquals(ADDRESS, actual.getAddress());
    }

    @Test
    void toEntity_ShouldMapTrainerDtoWithoutCredentials() {
        TrainerDto trainerDto = createTrainerDto();

        Trainer actual = mapper.toEntity(trainerDto);

        assertEquals(TRAINER_ID, actual.getUserId());
        assertEquals(TRAINER_FIRST_NAME, actual.getFirstName());
        assertEquals(TRAINER_LAST_NAME, actual.getLastName());
        assertTrue(actual.isActive());
        assertEquals(YOGA_TRAINING_TYPE, actual.getSpecialization().getTrainingTypeName());
        assertNull(actual.getUsername());
        assertNull(actual.getPassword());
    }

    @Test
    void toDto_ShouldMapTrainer() {
        Trainer trainer = createTrainer();

        TrainerDto actual = mapper.toDto(trainer);

        assertEquals(TRAINER_ID, actual.getUserId());
        assertEquals(TRAINER_FIRST_NAME, actual.getFirstName());
        assertEquals(TRAINER_LAST_NAME, actual.getLastName());
        assertTrue(actual.isActive());
        assertEquals(YOGA_TRAINING_TYPE, actual.getSpecialization().getTrainingTypeName());
    }

    @Test
    void toEntity_ShouldMapTrainingDto() {
        TrainingDto trainingDto = createTrainingDto();

        Training actual = mapper.toEntity(trainingDto);

        assertEquals(TRAINING_ID, actual.getId());
        assertEquals(TRAINEE_ID, actual.getTraineeId());
        assertEquals(TRAINER_ID, actual.getTrainerId());
        assertEquals(TRAINING_NAME, actual.getTrainingName());
        assertEquals(STRENGTH_TRAINING_TYPE, actual.getTrainingType().getTrainingTypeName());
        assertEquals(TRAINING_DATE, actual.getTrainingDate());
        assertEquals(60, actual.getTrainingDuration());
    }

    @Test
    void toDto_ShouldMapTraining() {
        Training training = createTraining();

        TrainingDto actual = mapper.toDto(training);

        assertEquals(TRAINING_ID, actual.getId());
        assertEquals(TRAINEE_ID, actual.getTraineeId());
        assertEquals(TRAINER_ID, actual.getTrainerId());
        assertEquals(TRAINING_NAME, actual.getTrainingName());
        assertEquals(STRENGTH_TRAINING_TYPE, actual.getTrainingType().getTrainingTypeName());
        assertEquals(TRAINING_DATE, actual.getTrainingDate());
        assertEquals(60, actual.getTrainingDuration());
    }

    private TraineeDto createTraineeDto() {
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setUserId(TRAINEE_ID);
        traineeDto.setFirstName(TRAINEE_FIRST_NAME);
        traineeDto.setLastName(TRAINEE_LAST_NAME);
        traineeDto.setActive(true);
        traineeDto.setDateOfBirth(DATE_OF_BIRTH);
        traineeDto.setAddress(ADDRESS);

        return traineeDto;
    }

    private Trainee createTrainee() {
        return Trainee.builder()
                .userId(TRAINEE_ID)
                .firstName(TRAINEE_FIRST_NAME)
                .lastName(TRAINEE_LAST_NAME)
                .username(TRAINEE_USERNAME)
                .password(PASSWORD)
                .active(true)
                .dateOfBirth(DATE_OF_BIRTH)
                .address(ADDRESS)
                .build();
    }

    private TrainerDto createTrainerDto() {
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setUserId(TRAINER_ID);
        trainerDto.setFirstName(TRAINER_FIRST_NAME);
        trainerDto.setLastName(TRAINER_LAST_NAME);
        trainerDto.setActive(true);
        trainerDto.setSpecialization(createTrainingTypeDto(YOGA_TRAINING_TYPE));

        return trainerDto;
    }

    private Trainer createTrainer() {
        TrainingType specialization = createTrainingType(YOGA_TRAINING_TYPE);

        return Trainer.builder()
                .userId(TRAINER_ID)
                .firstName(TRAINER_FIRST_NAME)
                .lastName(TRAINER_LAST_NAME)
                .username(TRAINER_USERNAME)
                .password(PASSWORD)
                .active(true)
                .specialization(specialization)
                .build();
    }

    private TrainingDto createTrainingDto() {
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setId(TRAINING_ID);
        trainingDto.setTraineeId(TRAINEE_ID);
        trainingDto.setTrainerId(TRAINER_ID);
        trainingDto.setTrainingName(TRAINING_NAME);
        trainingDto.setTrainingType(createTrainingTypeDto(STRENGTH_TRAINING_TYPE));
        trainingDto.setTrainingDate(TRAINING_DATE);
        trainingDto.setTrainingDuration(60);

        return trainingDto;
    }

    private Training createTraining() {
        TrainingType trainingType = createTrainingType(STRENGTH_TRAINING_TYPE);

        return Training.builder()
                .id(TRAINING_ID)
                .traineeId(TRAINEE_ID)
                .trainerId(TRAINER_ID)
                .trainingName(TRAINING_NAME)
                .trainingType(trainingType)
                .trainingDate(TRAINING_DATE)
                .trainingDuration(60)
                .build();
    }

    private TrainingTypeDto createTrainingTypeDto(String trainingTypeName) {
        TrainingTypeDto trainingTypeDto = new TrainingTypeDto();
        trainingTypeDto.setTrainingTypeName(trainingTypeName);

        return trainingTypeDto;
    }

    private TrainingType createTrainingType(String trainingTypeName) {
        return TrainingType.builder().trainingTypeName(trainingTypeName).build();
    }
}