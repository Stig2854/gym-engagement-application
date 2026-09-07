package com.gym.engagement.app.service;

import com.gym.engagement.app.model.Training;

import java.util.List;
import java.util.Optional;

public interface TrainingService {

    Training create(Training training);

    Optional<Training> findById(Long id);

    List<Training> findAll();
}