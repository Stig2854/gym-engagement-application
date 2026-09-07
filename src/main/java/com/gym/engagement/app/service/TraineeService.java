package com.gym.engagement.app.service;

import com.gym.engagement.app.dao.impl.TraineeDao;
import com.gym.engagement.app.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeService {
    private TraineeDao traineeDao;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    public Trainee create(Trainee trainee) {
        if (trainee == null || trainee.getUserId() == null) {
            throw new IllegalArgumentException("Trainee or trainee ID cannot be null");
        }
        if (traineeDao.findById(trainee.getUserId()).isPresent()) {
            throw new IllegalStateException("Trainee with ID " + trainee.getUserId() + " already exists");
        }
        traineeDao.save(trainee.getUserId(), trainee);
        return trainee;
    }

    public Optional<Trainee> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Trainee ID cannot be null");
        }
        return traineeDao.findById(id);
    }

    public List<Trainee> findAll() {
        return traineeDao.findAll();
    }

}