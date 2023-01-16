package edu.bbte.idde.frim1910.spring.repository;

import edu.bbte.idde.frim1910.spring.model.Car;

import java.sql.Timestamp;
import java.util.Collection;

public interface CarDao extends Dao<Car> {
    Collection<Car> findCarsByBrand(String brand);

    void updateCarsByDate(Timestamp lastRent, String brand, String model);
}
