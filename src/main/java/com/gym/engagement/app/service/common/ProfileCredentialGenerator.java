package com.gym.engagement.app.service.common;

import com.gym.engagement.app.dao.TraineeDao;
import com.gym.engagement.app.dao.TrainerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class ProfileCredentialGenerator {

    private static final int PASSWORD_LENGTH = 10;
    private static final String PASSWORD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private final SecureRandom random = new SecureRandom();

    private TraineeDao traineeDao;
    private TrainerDao trainerDao;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public String generateUsername(String firstName, String lastName) {
        validateNamePart(firstName, "First name");
        validateNamePart(lastName, "Last name");

        String baseUsername = firstName + "." + lastName;
        int suffix = countProfilesWithSameName(firstName, lastName);
        String username = suffix == 0 ? baseUsername : baseUsername + suffix;

        while (isUsernameTaken(username)) {
            suffix++;
            username = baseUsername + suffix;
        }

        return username;
    }

    private int countProfilesWithSameName(String firstName, String lastName) {
        long traineeCount = traineeDao.findAll().stream()
                .filter(trainee -> firstName.equals(trainee.getFirstName())
                        && lastName.equals(trainee.getLastName()))
                .count();

        long trainerCount = trainerDao.findAll().stream()
                .filter(trainer -> firstName.equals(trainer.getFirstName())
                        && lastName.equals(trainer.getLastName()))
                .count();

        return Math.toIntExact(traineeCount + trainerCount);
    }

    public String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int index = 0; index < PASSWORD_LENGTH; index++) {
            int randomIndex = random.nextInt(PASSWORD_CHARS.length());
            password.append(PASSWORD_CHARS.charAt(randomIndex));
        }

        return password.toString();
    }

    private boolean isUsernameTaken(String username) {
        boolean traineeUsernameTaken = traineeDao.findAll().stream()
                .anyMatch(trainee -> username.equals(trainee.getUsername()));

        boolean trainerUsernameTaken = trainerDao.findAll().stream()
                .anyMatch(trainer -> username.equals(trainer.getUsername()));

        return traineeUsernameTaken || trainerUsernameTaken;
    }

    private void validateNamePart(String namePart, String fieldName) {
        if (namePart == null || namePart.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }
}