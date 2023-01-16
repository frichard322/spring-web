package edu.bbte.idde.frim1910.spring.repository;

import edu.bbte.idde.frim1910.spring.model.BaseEntity;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface Dao<T extends BaseEntity> {
    T saveAndFlush(T entity);

    Collection<T> findAll();

    Optional<T> findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
