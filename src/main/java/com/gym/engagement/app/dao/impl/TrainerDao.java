package com.gym.engagement.app.dao.impl;

import com.gym.engagement.app.dao.AbstractDao;
import com.gym.engagement.app.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TrainerDao extends AbstractDao<Trainer> {

    @Autowired
    public void setStorage(@Qualifier("trainerStorage") Map<Long, Trainer> storage) {
        super.setStorage(storage);
    }
}