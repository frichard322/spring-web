package edu.bbte.idde.frim1910.backend.dao;

import edu.bbte.idde.frim1910.backend.model.Car;

import java.util.Collection;

public interface CarDao extends Dao<Car> {
    Collection<Car> findByBrand(String brand);
}
