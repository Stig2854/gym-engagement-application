package com.gym.engagement.app.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> findById(Long id);

    List<T> findAll();

    void save(Long id, T entity);

    void update(Long id, T entity);

    void deleteById(Long id);
}