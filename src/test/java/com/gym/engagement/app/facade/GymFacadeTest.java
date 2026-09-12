package com.gym.engagement.app.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private GymFacade facade;

    @BeforeEach
    void setUp() {
        facade = new GymFacade(traineeService, trainerService, trainingService, gymMapper);
    }

    @Test
    void createTrainee_ShouldMapDtoCreateEntityAndMapEntityToDto() {
        TraineeDto expectedTraineeDto = mock(TraineeDto.class);

        when(gymMapper.toEntity(traineeDto)).thenReturn(trainee);
        when(traineeService.create(trainee)).thenReturn(trainee);
        when(gymMapper.toDto(trainee)).thenReturn(expectedTraineeDto);

        TraineeDto actual = facade.createTrainee(traineeDto);
        assertSame(expectedTraineeDto, actual);
        verify(gymMapper).toEntity(traineeDto);
        verify(traineeService).create(trainee);
        verify(gymMapper).toDto(trainee);
    }

    @Test
    void updateTrainee_ShouldMapDtoUpdateEntityAndMapEntityToDto() {
        TraineeDto expectedTraineeDto = mock(TraineeDto.class);
        when(gymMapper.toEntity(traineeDto)).thenReturn(trainee);
        when(traineeService.update(ID, trainee)).thenReturn(trainee);
        when(gymMapper.toDto(trainee)).thenReturn(expectedTraineeDto);

        TraineeDto actual = facade.updateTrainee(ID, traineeDto);
        assertSame(expectedTraineeDto, actual);
        verify(gymMapper).toEntity(traineeDto);
        verify(traineeService).update(ID, trainee);
        verify(gymMapper).toDto(trainee);
    }

    @Test
    void deleteTraineeById_ShouldDelegateDeletionToService() {
        facade.deleteTraineeById(ID);

        verify(traineeService).deleteById(ID);
    }

    @Test
    void findTraineeById_ShouldMapAndReturnTraineeDto_WhenTraineeExists() {
        TraineeDto expectedTraineeDto = mock(TraineeDto.class);
        when(traineeService.findById(ID)).thenReturn(Optional.of(trainee));
        when(gymMapper.toDto(trainee)).thenReturn(expectedTraineeDto);

        Optional<TraineeDto> actual = facade.findTraineeById(ID);

        assertTrue(actual.isPresent());
        assertSame(expectedTraineeDto, actual.get());
        verify(traineeService).findById(ID);
        verify(gymMapper).toDto(trainee);
    }

    @Test
    void findTraineeById_ShouldReturnEmptyOptional_WhenTraineeDoesNotExist() {
        when(traineeService.findById(NOT_FOUND_ID)).thenReturn(Optional.empty());

        Optional<TraineeDto> actual = facade.findTraineeById(NOT_FOUND_ID);

        assertTrue(actual.isEmpty());
        verify(traineeService).findById(NOT_FOUND_ID);
        verifyNoInteractions(gymMapper);
    }

    @Test
    void findAllTrainees_ShouldMapAllEntitiesToDtos() {
        Trainee secondTrainee = mock(Trainee.class);
        TraineeDto expectedFirstTraineeDto = mock(TraineeDto.class);
        TraineeDto expectedSecondTraineeDto = mock(TraineeDto.class);
        List<TraineeDto> expected = List.of(expectedFirstTraineeDto, expectedSecondTraineeDto);

        when(traineeService.findAll()).thenReturn(List.of(trainee, secondTrainee));
        when(gymMapper.toDto(trainee)).thenReturn(expectedFirstTraineeDto);
        when(gymMapper.toDto(secondTrainee)).thenReturn(expectedSecondTraineeDto);

        List<TraineeDto> actual = facade.findAllTrainees();

        assertEquals(expected, actual);
        verify(traineeService).findAll();
        verify(gymMapper).toDto(trainee);
        verify(gymMapper).toDto(secondTrainee);
    }

    @Test
    void createTrainer_ShouldMapDtoCreateEntityAndMapEntityToDto() {
        TrainerDto expectedTrainerDto = mock(TrainerDto.class);

        when(gymMapper.toEntity(trainerDto)).thenReturn(trainer);
        when(trainerService.create(trainer)).thenReturn(trainer);
        when(gymMapper.toDto(trainer)).thenReturn(expectedTrainerDto);

        TrainerDto actual = facade.createTrainer(trainerDto);
        assertSame(expectedTrainerDto, actual);
        verify(gymMapper).toEntity(trainerDto);
        verify(trainerService).create(trainer);
        verify(gymMapper).toDto(trainer);
    }

    @Test
    void updateTrainer_ShouldMapDtoUpdateEntityAndMapEntityToDto() {
        TrainerDto expectedTrainerDto = mock(TrainerDto.class);

        when(gymMapper.toEntity(trainerDto)).thenReturn(trainer);
        when(trainerService.update(ID, trainer)).thenReturn(trainer);
        when(gymMapper.toDto(trainer)).thenReturn(expectedTrainerDto);

        TrainerDto actual = facade.updateTrainer(ID, trainerDto);

        assertSame(expectedTrainerDto, actual);
        verify(gymMapper).toEntity(trainerDto);
        verify(trainerService).update(ID, trainer);
        verify(gymMapper).toDto(trainer);
    }

    @Test
    void findTrainerById_ShouldMapAndReturnTrainerDto_WhenTrainerExists() {
        TrainerDto expectedTrainerDto = mock(TrainerDto.class);

        when(trainerService.findById(ID)).thenReturn(Optional.of(trainer));
        when(gymMapper.toDto(trainer)).thenReturn(expectedTrainerDto);

        Optional<TrainerDto> actual = facade.findTrainerById(ID);

        assertTrue(actual.isPresent());
        assertSame(expectedTrainerDto, actual.get());
        verify(trainerService).findById(ID);
        verify(gymMapper).toDto(trainer);
    }

    @Test
    void findTrainerById_ShouldReturnEmptyOptional_WhenTrainerDoesNotExist() {
        when(trainerService.findById(NOT_FOUND_ID)).thenReturn(Optional.empty());

        Optional<TrainerDto> actual = facade.findTrainerById(NOT_FOUND_ID);

        assertTrue(actual.isEmpty());
        verify(trainerService).findById(NOT_FOUND_ID);
        verifyNoInteractions(gymMapper);
    }

    @Test
    void findAllTrainers_ShouldMapAllEntitiesToDtos() {
        Trainer secondTrainer = mock(Trainer.class);
        TrainerDto expectedFirstTrainerDto = mock(TrainerDto.class);
        TrainerDto expectedSecondTrainerDto = mock(TrainerDto.class);
        List<TrainerDto> expected = List.of(expectedFirstTrainerDto, expectedSecondTrainerDto);

        when(trainerService.findAll()).thenReturn(List.of(trainer, secondTrainer));
        when(gymMapper.toDto(trainer)).thenReturn(expectedFirstTrainerDto);
        when(gymMapper.toDto(secondTrainer)).thenReturn(expectedSecondTrainerDto);

        List<TrainerDto> actual = facade.findAllTrainers();

        assertEquals(expected, actual);
        verify(trainerService).findAll();
        verify(gymMapper).toDto(trainer);
        verify(gymMapper).toDto(secondTrainer);
    }

    @Test
    void createTraining_ShouldMapDtoCreateEntityAndMapEntityToDto() {
        TrainingDto expectedTrainingDto = mock(TrainingDto.class);

        when(gymMapper.toEntity(trainingDto)).thenReturn(training);
        when(trainingService.create(training)).thenReturn(training);
        when(gymMapper.toDto(training)).thenReturn(expectedTrainingDto);

        TrainingDto actual = facade.createTraining(trainingDto);
        assertSame(expectedTrainingDto, actual);
        verify(gymMapper).toEntity(trainingDto);
        verify(trainingService).create(training);
        verify(gymMapper).toDto(training);
    }

    @Test
    void findTrainingById_ShouldMapAndReturnTrainingDto_WhenTrainingExists() {
        TrainingDto expectedTrainingDto = mock(TrainingDto.class);

        when(trainingService.findById(ID)).thenReturn(Optional.of(training));
        when(gymMapper.toDto(training)).thenReturn(expectedTrainingDto);

        Optional<TrainingDto> actual = facade.findTrainingById(ID);

        assertTrue(actual.isPresent());
        assertSame(expectedTrainingDto, actual.get());
        verify(trainingService).findById(ID);
        verify(gymMapper).toDto(training);
    }

    @Test
    void findTrainingById_ShouldReturnEmptyOptional_WhenTrainingDoesNotExist() {
        when(trainingService.findById(NOT_FOUND_ID)).thenReturn(Optional.empty());

        Optional<TrainingDto> actual = facade.findTrainingById(NOT_FOUND_ID);

        assertTrue(actual.isEmpty());
        verify(trainingService).findById(NOT_FOUND_ID);
        verifyNoInteractions(gymMapper);
    }

    @Test
    void findAllTrainings_ShouldMapAllEntitiesToDtos() {
        Training secondTraining = mock(Training.class);
        TrainingDto expectedFirstTrainingDto = mock(TrainingDto.class);
        TrainingDto expectedSecondTrainingDto = mock(TrainingDto.class);
        List<TrainingDto> expected = List.of(expectedFirstTrainingDto, expectedSecondTrainingDto);

        when(trainingService.findAll()).thenReturn(List.of(training, secondTraining));
        when(gymMapper.toDto(training)).thenReturn(expectedFirstTrainingDto);
        when(gymMapper.toDto(secondTraining)).thenReturn(expectedSecondTrainingDto);

        List<TrainingDto> actual = facade.findAllTrainings();

        assertEquals(expected, actual);
        verify(trainingService).findAll();
        verify(gymMapper).toDto(training);
        verify(gymMapper).toDto(secondTraining);
    }
}