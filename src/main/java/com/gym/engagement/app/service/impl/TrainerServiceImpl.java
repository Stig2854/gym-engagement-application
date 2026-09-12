package com.gym.engagement.app.service.impl;

import com.gym.engagement.app.dao.TrainerDao;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.service.TrainerService;
import com.gym.engagement.app.service.common.CoreValidator;
import com.gym.engagement.app.service.common.ProfileCredentialGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private TrainerDao trainerDao;
    private CoreValidator validator;
    private ProfileCredentialGenerator credentialGenerator;
    private PasswordEncoder passwordEncoder;

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

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Trainer create(Trainer trainer) {
        validator.validateTrainer(trainer);

        if (trainerDao.findById(trainer.getUserId()).isPresent()) {
            LOGGER.warn("Attempt to create trainer with existing ID: {}", trainer.getUserId());

            throw new IllegalStateException("Trainer with ID %d already exists".formatted(trainer.getUserId()));
        }

        String username = credentialGenerator.generateUsername(trainer.getFirstName(), trainer.getLastName());
        String rawPassword = credentialGenerator.generatePassword();

        Trainer trainerWithCredentials = Trainer.builder()
                .userId(trainer.getUserId())
                .firstName(trainer.getFirstName())
                .lastName(trainer.getLastName())
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .active(trainer.isActive())
                .specialization(trainer.getSpecialization())
                .build();

        trainerDao.save(trainerWithCredentials.getUserId(), trainerWithCredentials);
        LOGGER.info("Created trainer with ID: {}", trainerWithCredentials.getUserId());

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
                .orElseThrow(() -> {LOGGER.warn("Attempt to update non-existing trainer with ID: {}", id);

                    return new IllegalStateException("Trainer not found with ID: %d".formatted(id));
                });

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
        LOGGER.info("Updated trainer with ID: {}", updatedTrainer.getUserId());

        return updatedTrainer;
    }
}