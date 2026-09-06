package com.gym.engagement.app.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractDao<T> implements Dao<T> {

    private Map<Long, T> storage;

    protected void setStorage(Map<Long, T> storage) {
        this.storage = storage;
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(Long id, T entity) {
        storage.put(id, entity);
    }

    @Override
    public void update(Long id, T entity) {
        storage.put(id, entity);
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}