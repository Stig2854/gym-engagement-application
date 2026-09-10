package com.gym.engagement.app.service.impl;

import com.gym.engagement.app.dao.TrainingDao;
import com.gym.engagement.app.model.Training;
import com.gym.engagement.app.service.TrainingService;
import com.gym.engagement.app.service.common.CoreValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private TrainingDao trainingDao;
    private CoreValidator validator;

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    @Autowired
    public void setValidator(CoreValidator validator) {
        this.validator = validator;
    }

    @Override
    public Training create(Training training) {
        validator.validateTraining(training);

        if (trainingDao.findById(training.getId()).isPresent()) {
            LOGGER.warn("Attempt to create training with existing ID: {}", training.getId());

            throw new IllegalStateException("Training with ID " + training.getId() + " already exists");
        }

        trainingDao.save(training.getId(), training);

        LOGGER.info("Created training with ID: {}", training.getId());

        return training;
    }

    @Override
    public Optional<Training> findById(Long id) {
        validator.validateId(id, "Training");

        return trainingDao.findById(id);
    }

    @Override
    public List<Training> findAll() {
        return trainingDao.findAll();
    }
}