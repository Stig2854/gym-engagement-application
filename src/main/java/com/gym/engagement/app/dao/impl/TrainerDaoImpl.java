package com.gym.engagement.app.dao.impl;

import com.gym.engagement.app.dao.AbstractDao;
import com.gym.engagement.app.dao.TrainerDao;
import com.gym.engagement.app.model.Trainer;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDaoImpl extends AbstractDao<Trainer> implements TrainerDao {

    @Autowired
    public void setStorage(@Qualifier("trainerStorage") Map<Long, Trainer> storage) {
        super.setStorage(storage);
    }
}