package com.gym.engagement.app.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    private GymMapper gymMapper;

    @BeforeEach
    void setUp() {
        gymMapper = Mappers.getMapper(GymMapper.class);
    }

    @Test
    void toEntity_ShouldMapTraineeDtoWithoutCredentials() {
        TraineeDto traineeDto = createTraineeDto();

        Trainee trainee = gymMapper.toEntity(traineeDto);

        assertAll(
                () -> assertEquals(1L, trainee.getUserId()),
                () -> assertEquals("John", trainee.getFirstName()),
                () -> assertEquals("Smith", trainee.getLastName()),
                () -> assertEquals(true, trainee.isActive()),
                () -> assertEquals(LocalDate.of(1995, 5, 10), trainee.getDateOfBirth()),
                () -> assertEquals("Kyiv, Ukraine", trainee.getAddress()),
                () -> assertNull(trainee.getUsername()),
                () -> assertNull(trainee.getPassword())
        );
    }

    @Test
    void toDto_ShouldMapTraineeWithoutCredentials() {
        Trainee trainee = Trainee.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Smith")
                .username("John.Smith")
                .password("Abc123Xyz9")
                .active(true)
                .dateOfBirth(LocalDate.of(1995, 5, 10))
                .address("Kyiv, Ukraine")
                .build();

        TraineeDto traineeDto = gymMapper.toDto(trainee);

        assertAll(
                () -> assertEquals(1L, traineeDto.getUserId()),
                () -> assertEquals("John", traineeDto.getFirstName()),
                () -> assertEquals("Smith", traineeDto.getLastName()),
                () -> assertEquals(true, traineeDto.isActive()),
                () -> assertEquals(LocalDate.of(1995, 5, 10), traineeDto.getDateOfBirth()),
                () -> assertEquals("Kyiv, Ukraine", traineeDto.getAddress())
        );
    }

    @Test
    void toEntity_ShouldMapTrainerDtoWithoutCredentials() {
        TrainerDto trainerDto = createTrainerDto();

        Trainer trainer = gymMapper.toEntity(trainerDto);

        assertAll(
                () -> assertEquals(2L, trainer.getUserId()),
                () -> assertEquals("Anna", trainer.getFirstName()),
                () -> assertEquals("Brown", trainer.getLastName()),
                () -> assertEquals(true, trainer.isActive()),
                () -> assertEquals(
                        "Yoga",
                        trainer.getSpecialization().getTrainingTypeName()),
                () -> assertNull(trainer.getUsername()),
                () -> assertNull(trainer.getPassword())
        );
    }

    @Test
    void toDto_ShouldMapTrainerWithoutCredentials() {
        Trainer trainer = Trainer.builder()
                .userId(2L)
                .firstName("Anna")
                .lastName("Brown")
                .username("Anna.Brown")
                .password("Abc123Xyz9")
                .active(true)
                .specialization(TrainingType.builder()
                        .trainingTypeName("Yoga")
                        .build())
                .build();

        TrainerDto trainerDto = gymMapper.toDto(trainer);

        assertAll(
                () -> assertEquals(2L, trainerDto.getUserId()),
                () -> assertEquals("Anna", trainerDto.getFirstName()),
                () -> assertEquals("Brown", trainerDto.getLastName()),
                () -> assertEquals(true, trainerDto.isActive()),
                () -> assertEquals(
                        "Yoga",
                        trainerDto.getSpecialization().getTrainingTypeName())
        );
    }

    @Test
    void toEntity_ShouldMapTrainingDto() {
        TrainingDto trainingDto = createTrainingDto();

        Training training = gymMapper.toEntity(trainingDto);

        assertAll(
                () -> assertEquals(3L, training.getId()),
                () -> assertEquals(1L, training.getTraineeId()),
                () -> assertEquals(2L, training.getTrainerId()),
                () -> assertEquals("Morning strength training", training.getTrainingName()),
                () -> assertEquals(
                        "Strength",
                        training.getTrainingType().getTrainingTypeName()),
                () -> assertEquals(LocalDate.of(2026, 9, 10), training.getTrainingDate()),
                () -> assertEquals(60, training.getTrainingDuration())
        );
    }

    @Test
    void toDto_ShouldMapTraining() {
        Training training = Training.builder()
                .id(3L)
                .traineeId(1L)
                .trainerId(2L)
                .trainingName("Morning strength training")
                .trainingType(TrainingType.builder()
                        .trainingTypeName("Strength")
                        .build())
                .trainingDate(LocalDate.of(2026, 9, 10))
                .trainingDuration(60)
                .build();

        TrainingDto trainingDto = gymMapper.toDto(training);

        assertAll(
                () -> assertEquals(3L, trainingDto.getId()),
                () -> assertEquals(1L, trainingDto.getTraineeId()),
                () -> assertEquals(2L, trainingDto.getTrainerId()),
                () -> assertEquals(
                        "Morning strength training",
                        trainingDto.getTrainingName()),
                () -> assertEquals(
                        "Strength",
                        trainingDto.getTrainingType().getTrainingTypeName()),
                () -> assertEquals(LocalDate.of(2026, 9, 10), trainingDto.getTrainingDate()),
                () -> assertEquals(60, trainingDto.getTrainingDuration())
        );
    }

    private TraineeDto createTraineeDto() {
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setUserId(1L);
        traineeDto.setFirstName("John");
        traineeDto.setLastName("Smith");
        traineeDto.setActive(true);
        traineeDto.setDateOfBirth(LocalDate.of(1995, 5, 10));
        traineeDto.setAddress("Kyiv, Ukraine");

        return traineeDto;
    }

    private TrainerDto createTrainerDto() {
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setUserId(2L);
        trainerDto.setFirstName("Anna");
        trainerDto.setLastName("Brown");
        trainerDto.setActive(true);
        trainerDto.setSpecialization(createTrainingTypeDto("Yoga"));

        return trainerDto;
    }

    private TrainingDto createTrainingDto() {
        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setId(3L);
        trainingDto.setTraineeId(1L);
        trainingDto.setTrainerId(2L);
        trainingDto.setTrainingName("Morning strength training");
        trainingDto.setTrainingType(createTrainingTypeDto("Strength"));
        trainingDto.setTrainingDate(LocalDate.of(2026, 9, 10));
        trainingDto.setTrainingDuration(60);

        return trainingDto;
    }

    private TrainingTypeDto createTrainingTypeDto(String trainingTypeName) {
        TrainingTypeDto trainingTypeDto = new TrainingTypeDto();
        trainingTypeDto.setTrainingTypeName(trainingTypeName);

        return trainingTypeDto;
    }
}