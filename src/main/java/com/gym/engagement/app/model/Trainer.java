package com.gym.engagement.app.model;

public class Trainer extends User {
    private TrainingType  Specialization;

    public Trainer() {
    }

    public Trainer(Long id, String firstName, String lastName, String username,
                   String password, boolean isActive, TrainingType specialization) {
        super(id, firstName, lastName, username, password, isActive);
        this.Specialization = specialization;
    }

    public TrainingType getSpecialization() {
        return Specialization;
    }

    public void setSpecialization(TrainingType specialization) {
        Specialization = specialization;
    }
}
