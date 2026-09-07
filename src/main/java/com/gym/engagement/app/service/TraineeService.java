package com.gym.engagement.app.service;

import com.gym.engagement.app.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeService {

    Trainee create(Trainee trainee);

    Optional<Trainee> findById(Long id);

    List<Trainee> findAll();

    Trainee update(Long id, Trainee trainee);

    void deleteById(Long id);
}