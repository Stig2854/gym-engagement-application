package com.gym.engagement.app.dao.impl;

import com.gym.engagement.app.dao.AbstractDao;
import com.gym.engagement.app.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TraineeDao extends AbstractDao<Trainee> {

    @Autowired
    public void setStorage(@Qualifier("traineeStorage") Map<Long, Trainee> storage) {
        super.setStorage(storage);
    }
}