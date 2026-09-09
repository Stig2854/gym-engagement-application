package com.gym.engagement.app.service.impl;

import com.gym.engagement.app.dao.TrainerDao;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.service.TrainerService;
import com.gym.engagement.app.service.common.CoreValidator;
import com.gym.engagement.app.service.common.ProfileCredentialGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {

    private TrainerDao trainerDao;
    private CoreValidator validator;
    private ProfileCredentialGenerator credentialGenerator;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    public void setValidator(CoreValidator validator) {
        this.validator = validator;
    }

    @Autowired
    public void setCredentialGenerator(ProfileCredentialGenerator credentialGenerator) {
        this.credentialGenerator = credentialGenerator;
    }

    @Override
    public Trainer create(Trainer trainer) {
        validator.validateTrainer(trainer);

        if (trainerDao.findById(trainer.getUserId()).isPresent()) {
            throw new IllegalStateException("Trainer with ID " + trainer.getUserId() + " already exists");
        }

        String username = credentialGenerator.generateUsername(trainer.getFirstName(), trainer.getLastName());
        String password = credentialGenerator.generatePassword();

        Trainer trainerWithCredentials = Trainer.builder()
                .userId(trainer.getUserId())
                .firstName(trainer.getFirstName())
                .lastName(trainer.getLastName())
                .username(username)
                .password(password)
                .active(trainer.isActive())
                .specialization(trainer.getSpecialization())
                .build();

        trainerDao.save(trainerWithCredentials.getUserId(), trainerWithCredentials);

        return trainerWithCredentials;
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        validator.validateId(id, "Trainer");

        return trainerDao.findById(id);
    }

    @Override
    public List<Trainer> findAll() {
        return trainerDao.findAll();
    }

    @Override
    public Trainer update(Long id, Trainer trainer) {
        validator.validateTrainer(trainer);
        validator.validateUpdateId(id, trainer.getUserId(), "Trainer");

        Trainer existingTrainer = trainerDao.findById(id)
                .orElseThrow(() -> new IllegalStateException("Trainer not found with ID: " + id));

        Trainer updatedTrainer = Trainer.builder()
                .userId(trainer.getUserId())
                .firstName(trainer.getFirstName())
                .lastName(trainer.getLastName())
                .username(existingTrainer.getUsername())
                .password(existingTrainer.getPassword())
                .active(trainer.isActive())
                .specialization(trainer.getSpecialization())
                .build();

        trainerDao.update(id, updatedTrainer);

        return updatedTrainer;
    }
}