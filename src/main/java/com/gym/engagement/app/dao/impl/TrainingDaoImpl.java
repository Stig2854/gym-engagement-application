package com.gym.engagement.app.dao.impl;

import com.gym.engagement.app.dao.AbstractDao;
import com.gym.engagement.app.dao.TrainingDao;
import com.gym.engagement.app.model.Training;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDaoImpl extends AbstractDao<Training> implements TrainingDao {

    @Autowired
    public void setStorage(@Qualifier("trainingStorage") Map<Long, Training> storage) {
        super.setStorage(storage);
    }
}