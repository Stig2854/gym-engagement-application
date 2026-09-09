package com.gym.engagement.app.service.impl;

import com.gym.engagement.app.dao.TraineeDao;
import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.service.TraineeService;
import com.gym.engagement.app.service.common.CoreValidator;
import com.gym.engagement.app.service.common.ProfileCredentialGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeServiceImpl implements TraineeService {

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
            throw new IllegalStateException("Trainee with ID " + trainee.getUserId() + " already exists");
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
                .orElseThrow(() -> new IllegalStateException("Trainee not found with ID: " + id));

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

        return updatedTrainee;
    }

    @Override
    public void deleteById(Long id) {
        validator.validateId(id, "Trainee");

        traineeDao.findById(id)
                .orElseThrow(() -> new IllegalStateException("Trainee not found with ID: " + id));

        traineeDao.deleteById(id);
    }
}