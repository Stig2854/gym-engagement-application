package com.gym.engagement.app.facade;

import com.gym.engagement.app.dto.TraineeDto;
import com.gym.engagement.app.dto.TrainerDto;
import com.gym.engagement.app.dto.TrainingDto;
import com.gym.engagement.app.mapper.GymMapper;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.service.TraineeService;
import com.gym.engagement.app.service.TrainerService;
import com.gym.engagement.app.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GymFacadeTest {

    private static final Long ID = 1L;
    private static final Long NOT_FOUND_ID = 2L;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @Mock
    private GymMapper gymMapper;

    @Mock
    private TraineeDto traineeDto;

    @Mock
    private TrainerDto trainerDto;

    @Mock
    private TrainingDto trainingDto;

    @Mock
    private Trainee trainee;

    @Mock
    private Trainer trainer;

    @Mock
    private Training training;

    private GymFacade gymFacade;

    @BeforeEach
    void setUp() {
        gymFacade = new GymFacade(
                traineeService,
                trainerService,
                trainingService,
                gymMapper);
    }

    @Test
    void createTrainee_ShouldMapDtoCreateEntityAndMapResultToDto() {
        TraineeDto createdTraineeDto = mockCreatedTraineeDto();

        when(gymMapper.toEntity(traineeDto)).thenReturn(trainee);
        when(traineeService.create(trainee)).thenReturn(trainee);
        when(gymMapper.toDto(trainee)).thenReturn(createdTraineeDto);

        TraineeDto result = gymFacade.createTrainee(traineeDto);

        verify(gymMapper).toEntity(traineeDto);
        verify(traineeService).create(trainee);
        verify(gymMapper).toDto(trainee);

        assertSame(createdTraineeDto, result);
    }

    @Test
    void updateTrainee_ShouldMapDtoUpdateEntityAndMapResultToDto() {
        TraineeDto updatedTraineeDto = mockCreatedTraineeDto();

        when(gymMapper.toEntity(traineeDto)).thenReturn(trainee);
        when(traineeService.update(ID, trainee)).thenReturn(trainee);
        when(gymMapper.toDto(trainee)).thenReturn(updatedTraineeDto);

        TraineeDto result = gymFacade.updateTrainee(ID, traineeDto);

        verify(gymMapper).toEntity(traineeDto);
        verify(traineeService).update(ID, trainee);
        verify(gymMapper).toDto(trainee);

        assertSame(updatedTraineeDto, result);
    }

    @Test
    void deleteTraineeById_ShouldDelegateDeletionToService() {
        gymFacade.deleteTraineeById(ID);

        verify(traineeService).deleteById(ID);
    }

    @Test
    void findTraineeById_ShouldMapExistingTraineeAndReturnEmptyWhenNotFound() {
        TraineeDto foundTraineeDto = mockCreatedTraineeDto();

        when(traineeService.findById(ID)).thenReturn(Optional.of(trainee));
        when(traineeService.findById(NOT_FOUND_ID)).thenReturn(Optional.empty());
        when(gymMapper.toDto(trainee)).thenReturn(foundTraineeDto);

        Optional<TraineeDto> foundResult = gymFacade.findTraineeById(ID);
        Optional<TraineeDto> emptyResult = gymFacade.findTraineeById(NOT_FOUND_ID);

        verify(traineeService).findById(ID);
        verify(traineeService).findById(NOT_FOUND_ID);
        verify(gymMapper).toDto(trainee);

        assertTrue(foundResult.isPresent());
        assertSame(foundTraineeDto, foundResult.get());
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    void findAllTrainees_ShouldMapAllEntitiesToDtos() {
        Trainee secondTrainee = org.mockito.Mockito.mock(Trainee.class);
        TraineeDto secondTraineeDto = mockCreatedTraineeDto();

        when(traineeService.findAll()).thenReturn(List.of(trainee, secondTrainee));
        when(gymMapper.toDto(trainee)).thenReturn(traineeDto);
        when(gymMapper.toDto(secondTrainee)).thenReturn(secondTraineeDto);

        List<TraineeDto> result = gymFacade.findAllTrainees();

        verify(traineeService).findAll();
        verify(gymMapper).toDto(trainee);
        verify(gymMapper).toDto(secondTrainee);

        assertEquals(List.of(traineeDto, secondTraineeDto), result);
    }

    @Test
    void createTrainer_ShouldMapDtoCreateEntityAndMapResultToDto() {
        TrainerDto createdTrainerDto = mockCreatedTrainerDto();

        when(gymMapper.toEntity(trainerDto)).thenReturn(trainer);
        when(trainerService.create(trainer)).thenReturn(trainer);
        when(gymMapper.toDto(trainer)).thenReturn(createdTrainerDto);

        TrainerDto result = gymFacade.createTrainer(trainerDto);

        verify(gymMapper).toEntity(trainerDto);
        verify(trainerService).create(trainer);
        verify(gymMapper).toDto(trainer);

        assertSame(createdTrainerDto, result);
    }

    @Test
    void updateTrainer_ShouldMapDtoUpdateEntityAndMapResultToDto() {
        TrainerDto updatedTrainerDto = mockCreatedTrainerDto();

        when(gymMapper.toEntity(trainerDto)).thenReturn(trainer);
        when(trainerService.update(ID, trainer)).thenReturn(trainer);
        when(gymMapper.toDto(trainer)).thenReturn(updatedTrainerDto);

        TrainerDto result = gymFacade.updateTrainer(ID, trainerDto);

        verify(gymMapper).toEntity(trainerDto);
        verify(trainerService).update(ID, trainer);
        verify(gymMapper).toDto(trainer);

        assertSame(updatedTrainerDto, result);
    }

    @Test
    void findTrainerById_ShouldMapExistingTrainerAndReturnEmptyWhenNotFound() {
        TrainerDto foundTrainerDto = mockCreatedTrainerDto();

        when(trainerService.findById(ID)).thenReturn(Optional.of(trainer));
        when(trainerService.findById(NOT_FOUND_ID)).thenReturn(Optional.empty());
        when(gymMapper.toDto(trainer)).thenReturn(foundTrainerDto);

        Optional<TrainerDto> foundResult = gymFacade.findTrainerById(ID);
        Optional<TrainerDto> emptyResult = gymFacade.findTrainerById(NOT_FOUND_ID);

        verify(trainerService).findById(ID);
        verify(trainerService).findById(NOT_FOUND_ID);
        verify(gymMapper).toDto(trainer);

        assertTrue(foundResult.isPresent());
        assertSame(foundTrainerDto, foundResult.get());
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    void findAllTrainers_ShouldMapAllEntitiesToDtos() {
        Trainer secondTrainer = org.mockito.Mockito.mock(Trainer.class);
        TrainerDto secondTrainerDto = mockCreatedTrainerDto();

        when(trainerService.findAll()).thenReturn(List.of(trainer, secondTrainer));
        when(gymMapper.toDto(trainer)).thenReturn(trainerDto);
        when(gymMapper.toDto(secondTrainer)).thenReturn(secondTrainerDto);

        List<TrainerDto> result = gymFacade.findAllTrainers();

        verify(trainerService).findAll();
        verify(gymMapper).toDto(trainer);
        verify(gymMapper).toDto(secondTrainer);

        assertEquals(List.of(trainerDto, secondTrainerDto), result);
    }

    @Test
    void createTraining_ShouldMapDtoCreateEntityAndMapResultToDto() {
        TrainingDto createdTrainingDto = mockCreatedTrainingDto();

        when(gymMapper.toEntity(trainingDto)).thenReturn(training);
        when(trainingService.create(training)).thenReturn(training);
        when(gymMapper.toDto(training)).thenReturn(createdTrainingDto);

        TrainingDto result = gymFacade.createTraining(trainingDto);

        verify(gymMapper).toEntity(trainingDto);
        verify(trainingService).create(training);
        verify(gymMapper).toDto(training);

        assertSame(createdTrainingDto, result);
    }

    @Test
    void findTrainingById_ShouldMapExistingTrainingAndReturnEmptyWhenNotFound() {
        TrainingDto foundTrainingDto = mockCreatedTrainingDto();

        when(trainingService.findById(ID)).thenReturn(Optional.of(training));
        when(trainingService.findById(NOT_FOUND_ID)).thenReturn(Optional.empty());
        when(gymMapper.toDto(training)).thenReturn(foundTrainingDto);

        Optional<TrainingDto> foundResult = gymFacade.findTrainingById(ID);
        Optional<TrainingDto> emptyResult = gymFacade.findTrainingById(NOT_FOUND_ID);

        verify(trainingService).findById(ID);
        verify(trainingService).findById(NOT_FOUND_ID);
        verify(gymMapper).toDto(training);

        assertTrue(foundResult.isPresent());
        assertSame(foundTrainingDto, foundResult.get());
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    void findAllTrainings_ShouldMapAllEntitiesToDtos() {
        Training secondTraining = org.mockito.Mockito.mock(Training.class);
        TrainingDto secondTrainingDto = mockCreatedTrainingDto();

        when(trainingService.findAll()).thenReturn(List.of(training, secondTraining));
        when(gymMapper.toDto(training)).thenReturn(trainingDto);
        when(gymMapper.toDto(secondTraining)).thenReturn(secondTrainingDto);

        List<TrainingDto> result = gymFacade.findAllTrainings();

        verify(trainingService).findAll();
        verify(gymMapper).toDto(training);
        verify(gymMapper).toDto(secondTraining);

        assertEquals(List.of(trainingDto, secondTrainingDto), result);
    }

    private TraineeDto mockCreatedTraineeDto() {
        return org.mockito.Mockito.mock(TraineeDto.class);
    }

    private TrainerDto mockCreatedTrainerDto() {
        return org.mockito.Mockito.mock(TrainerDto.class);
    }

    private TrainingDto mockCreatedTrainingDto() {
        return org.mockito.Mockito.mock(TrainingDto.class);
    }
}