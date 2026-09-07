package com.gym.engagement.app.service;

import com.gym.engagement.app.dao.impl.TrainingDao;
import com.gym.engagement.app.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingService {
    private TrainingDao trainingDao;

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    public Training create(Training training) {
        if (training == null || training.getId() == null) {
            throw new IllegalArgumentException("Training or training ID cannot be null");
        }
        if (trainingDao.findById(training.getId()).isPresent()) {
            throw new IllegalStateException("Training with ID " + training.getId() + " already exists");
        }
        trainingDao.save(training.getId(), training);
        return training;
    }

    public Optional<Training> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Training ID cannot be null");
        }
        return trainingDao.findById(id);
    }

    public List<Training> findAll() {
        return trainingDao.findAll();
    }

    public Training update(Long id, Training training) {
        if (id == null) {
            throw new IllegalArgumentException("Training ID cannot be null");
        }
        if (training == null || training.getId() == null) {
            throw new IllegalArgumentException("Training or training ID cannot be null");
        }
        if (!id.equals(training.getId())) {
            throw new IllegalArgumentException("Training ID does not match update ID");
        }
        if (trainingDao.findById(id).isEmpty()) {
            throw new IllegalStateException("Training not found with ID: " + id);
        }
        trainingDao.update(id, training);
        return training;
    }
}