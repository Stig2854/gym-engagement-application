package com.gym.engagement.app.service.impl;

import com.gym.engagement.app.dao.TraineeDao;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.service.TraineeService;
import com.gym.engagement.app.service.common.CoreValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {

    private TraineeDao traineeDao;
    private CoreValidator validator;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setValidator(CoreValidator validator) {
        this.validator = validator;
    }

    @Override
    public Trainee create(Trainee trainee) {
        validator.validateTrainee(trainee);

        if (traineeDao.findById(trainee.getUserId()).isPresent()) {
            throw new IllegalStateException("Trainee with ID " + trainee.getUserId() + " already exists");
        }

        traineeDao.save(trainee.getUserId(), trainee);

        return trainee;
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        validator.validateId(id, "Trainee");

        return traineeDao.findById(id);
    }

    @Override
    public List<Trainee> findAll() {
        return traineeDao.findAll();
    }

    @Override
    public Trainee update(Long id, Trainee trainee) {
        validator.validateTrainee(trainee);
        validator.validateUpdateId(id, trainee.getUserId(), "Trainee");

        traineeDao.findById(id)
                .orElseThrow(() -> new IllegalStateException("Trainee not found with ID: " + id));

        traineeDao.update(id, trainee);

        return trainee;
    }

    @Override
    public void deleteById(Long id) {
        validator.validateId(id, "Trainee");

        traineeDao.findById(id)
                .orElseThrow(() -> new IllegalStateException("Trainee not found with ID: " + id));

        traineeDao.deleteById(id);
    }
}