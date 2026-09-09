package com.gym.engagement.app.mapper;

import com.gym.engagement.app.dto.TraineeDto;
import com.gym.engagement.app.dto.TrainerDto;
import com.gym.engagement.app.dto.TrainingDto;
import com.gym.engagement.app.dto.TrainingTypeDto;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.model.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GymMapper {

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    Trainee toEntity(TraineeDto traineeDto);

    TraineeDto toDto(Trainee trainee);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    Trainer toEntity(TrainerDto trainerDto);

    TrainerDto toDto(Trainer trainer);

    Training toEntity(TrainingDto trainingDto);

    TrainingDto toDto(Training training);

    TrainingType toEntity(TrainingTypeDto trainingTypeDto);

    TrainingTypeDto toDto(TrainingType trainingType);
}