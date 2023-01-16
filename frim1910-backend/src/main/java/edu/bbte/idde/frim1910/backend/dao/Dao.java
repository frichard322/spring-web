package edu.bbte.idde.frim1910.backend.dao;

import edu.bbte.idde.frim1910.backend.model.BaseEntity;

import java.util.Collection;
import java.util.UUID;

public interface Dao<T extends BaseEntity> {
    void create(T entity);

    Collection<T> findAll();

    T findByUuid(UUID uuid);

    void delete(UUID uuid);

    void update(T entity);
}
