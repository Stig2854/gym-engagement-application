package com.gym.engagement.app.service;

import com.gym.engagement.app.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerService {

    Trainer create(Trainer trainer);

    Optional<Trainer> findById(Long id);

    List<Trainer> findAll();

    Trainer update(Long id, Trainer trainer);
}