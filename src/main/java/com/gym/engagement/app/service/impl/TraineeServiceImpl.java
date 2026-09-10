package com.gym.engagement.app.service.impl;

import com.gym.engagement.app.dao.TraineeDao;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.service.TraineeService;
import com.gym.engagement.app.service.common.CoreValidator;
import com.gym.engagement.app.service.common.ProfileCredentialGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeServiceImpl.class);

    private TraineeDao traineeDao;
    private CoreValidator validator;
    private ProfileCredentialGenerator credentialGenerator;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setCredentialGenerator(ProfileCredentialGenerator credentialGenerator) {
        this.credentialGenerator = credentialGenerator;
    }

    @Autowired
    public void setValidator(CoreValidator validator) {
        this.validator = validator;
    }

    @Override
    public Trainee create(Trainee trainee) {
        validator.validateTrainee(trainee);
        if (traineeDao.findById(trainee.getUserId()).isPresent()) {
            LOGGER.warn("Attempt to create trainee with existing ID: {}", trainee.getUserId());

            throw new IllegalStateException("Trainee with ID %d already exists".formatted(trainee.getUserId()));
        }

        String username = credentialGenerator.generateUsername(trainee.getFirstName(), trainee.getLastName());
        String password = credentialGenerator.generatePassword();

        Trainee traineeWithCredentials = Trainee.builder()
                .userId(trainee.getUserId())
                .firstName(trainee.getFirstName())
                .lastName(trainee.getLastName())
                .username(username)
                .password(password)
                .active(trainee.isActive())
                .dateOfBirth(trainee.getDateOfBirth())
                .address(trainee.getAddress())
                .build();

        traineeDao.save(traineeWithCredentials.getUserId(), traineeWithCredentials);
        LOGGER.info("Created trainee with ID: {}", traineeWithCredentials.getUserId());

        return traineeWithCredentials;
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

        Trainee existingTrainee = traineeDao.findById(id)
                .orElseThrow(() -> {
                    LOGGER.warn("Attempt to update non-existing trainee with ID: {}", id);

                    return new IllegalStateException("Trainee not found with ID: %d".formatted(id));
                });

        Trainee updatedTrainee = Trainee.builder()
                .userId(trainee.getUserId())
                .firstName(trainee.getFirstName())
                .lastName(trainee.getLastName())
                .username(existingTrainee.getUsername())
                .password(existingTrainee.getPassword())
                .active(trainee.isActive())
                .dateOfBirth(trainee.getDateOfBirth())
                .address(trainee.getAddress())
                .build();

        traineeDao.update(id, updatedTrainee);
        LOGGER.info("Updated trainee with ID: {}", updatedTrainee.getUserId());

        return updatedTrainee;
    }

    @Override
    public void deleteById(Long id) {
        validator.validateId(id, "Trainee");

        traineeDao.findById(id)
                .orElseThrow(() -> {
                    LOGGER.warn("Attempt to delete non-existing trainee with ID: {}", id);

                    return new IllegalStateException("Trainee not found with ID: %d".formatted(id));
                });

        traineeDao.deleteById(id);
        LOGGER.info("Deleted trainee with ID: {}", id);
    }
}