package com.gym.engagement.app.service.common;

import com.gym.engagement.app.model.Trainee;
import com.gym.engagement.app.model.Trainer;
import com.gym.engagement.app.model.Training;
import org.springframework.stereotype.Component;

@Component
public class CoreValidator {

    public void validateId(Long id, String entityName) {
        if (id == null) {
            throw new IllegalArgumentException(entityName + " ID cannot be null");
        }
    }

    public void validateTrainee(Trainee trainee) {
        if (trainee == null) {
            throw new IllegalArgumentException("Trainee cannot be null");
        }

        validateId(trainee.getUserId(), "Trainee");
    }

    public void validateTrainer(Trainer trainer) {
        if (trainer == null) {
            throw new IllegalArgumentException("Trainer cannot be null");
        }

        validateId(trainer.getUserId(), "Trainer");
    }

    public void validateTraining(Training training) {
        if (training == null) {
            throw new IllegalArgumentException("Training cannot be null");
        }

        validateId(training.getId(), "Training");
    }

    public void validateUpdateId(Long id, Long entityId, String entityName) {
        validateId(id, entityName);
        validateId(entityId, entityName);

        if (!id.equals(entityId)) {
            throw new IllegalArgumentException(entityName + " ID does not match update ID");
        }
    }


}