package com.gym.engagement.app.service;

import com.gym.engagement.app.dao.impl.TrainerDao;
import com.gym.engagement.app.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {
    private TrainerDao trainerDao;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public Trainer create(Trainer trainer) {
        if (trainer == null || trainer.getUserId() == null) {
            throw new IllegalArgumentException("Trainer or trainer ID cannot be null");
        }
        if (trainerDao.findById(trainer.getUserId()).isPresent()) {
            throw new IllegalStateException("Trainer with ID " + trainer.getUserId() + " already exists");
        }
        trainerDao.save(trainer.getUserId(), trainer);
        return trainer;
    }

    public Optional<Trainer> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }
        return trainerDao.findById(id);
    }

    public List<Trainer> findAll() {
        return trainerDao.findAll();
    }

    public Trainer update(Long id, Trainer trainer) {
        if (id == null) {
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }
        if (trainer == null || trainer.getUserId() == null) {
            throw new IllegalArgumentException("Trainer or trainer ID cannot be null");
        }
        if (!id.equals(trainer.getUserId())) {
            throw new IllegalArgumentException("Trainer ID does not match update ID");
        }
        if (trainerDao.findById(id).isEmpty()) {
            throw new IllegalStateException("Trainer not found with ID: " + id);
        }
        trainerDao.update(id, trainer);
        return trainer;
    }
}