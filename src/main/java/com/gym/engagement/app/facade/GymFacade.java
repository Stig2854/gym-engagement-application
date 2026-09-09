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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GymFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final GymMapper gymMapper;

    public TraineeDto createTrainee(TraineeDto traineeDto) {
        Trainee trainee = gymMapper.toEntity(traineeDto);
        Trainee createdTrainee = traineeService.create(trainee);

        return gymMapper.toDto(createdTrainee);
    }

    public TraineeDto updateTrainee(Long id, TraineeDto traineeDto) {
        Trainee trainee = gymMapper.toEntity(traineeDto);
        Trainee updatedTrainee = traineeService.update(id, trainee);

        return gymMapper.toDto(updatedTrainee);
    }

    public void deleteTraineeById(Long id) {
        traineeService.deleteById(id);
    }

    public Optional<TraineeDto> findTraineeById(Long id) {
        return traineeService.findById(id).map(gymMapper::toDto);
    }

    public List<TraineeDto> findAllTrainees() {
        return traineeService.findAll().stream().map(gymMapper::toDto).toList();
    }

    public TrainerDto createTrainer(TrainerDto trainerDto) {
        Trainer trainer = gymMapper.toEntity(trainerDto);
        Trainer createdTrainer = trainerService.create(trainer);

        return gymMapper.toDto(createdTrainer);
    }

    public TrainerDto updateTrainer(Long id, TrainerDto trainerDto) {
        Trainer trainer = gymMapper.toEntity(trainerDto);
        Trainer updatedTrainer = trainerService.update(id, trainer);

        return gymMapper.toDto(updatedTrainer);
    }

    public Optional<TrainerDto> findTrainerById(Long id) {
        return trainerService.findById(id).map(gymMapper::toDto);
    }

    public List<TrainerDto> findAllTrainers() {
        return trainerService.findAll().stream().map(gymMapper::toDto).toList();
    }

    public TrainingDto createTraining(TrainingDto trainingDto) {
        Training training = gymMapper.toEntity(trainingDto);
        Training createdTraining = trainingService.create(training);

        return gymMapper.toDto(createdTraining);
    }

    public Optional<TrainingDto> findTrainingById(Long id) {
        return trainingService.findById(id).map(gymMapper::toDto);
    }

    public List<TrainingDto> findAllTrainings() {
        return trainingService.findAll().stream().map(gymMapper::toDto).toList();
    }
}
