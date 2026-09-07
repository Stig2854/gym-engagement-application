package com.gym.engagement.app.dao.impl;

import com.gym.engagement.app.dao.AbstractDao;
import com.gym.engagement.app.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TrainingDaoImpl extends AbstractDao<Training> {

    @Autowired
    public void setStorage(@Qualifier("trainingStorage") Map<Long, Training> storage) {
        super.setStorage(storage);
    }
}