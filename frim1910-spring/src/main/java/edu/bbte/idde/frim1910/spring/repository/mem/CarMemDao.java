package edu.bbte.idde.frim1910.spring.repository.mem;

import edu.bbte.idde.frim1910.spring.model.Car;
import edu.bbte.idde.frim1910.spring.repository.CarAdvertisementDao;
import edu.bbte.idde.frim1910.spring.repository.CarDao;
import edu.bbte.idde.frim1910.spring.repository.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Profile("mem")
public class CarMemDao implements CarDao {

    @Autowired
    CarAdvertisementDao carAdvertisementDao;

    private final Collection<Car> cars = new LinkedList<>();

    @Override
    public Collection<Car> findAll() {
        return cars;
    }

    @Override
    public Collection<Car> findCarsByBrand(String brand) {
        return cars
            .stream()
            .filter(car -> Objects.equals(car.getBrand(), brand))
            .collect(Collectors.toList());
    }

    @Override
    public void updateCarsByDate(Timestamp lastRent, String brand, String model) {

    }

    @Override
    public Optional<Car> findById(UUID id) {
        return cars
            .stream()
            .filter(car -> Objects.equals(car.getId(), id))
            .findFirst();
    }

    @Override
    public boolean existsById(UUID id) {
        return cars
            .stream()
            .anyMatch(car -> Objects.equals(car.getId(), id));
    }

    @Override
    public Car saveAndFlush(Car entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        cars.add(entity);
        return entity;
    }

    @Override
    public void deleteById(UUID id) {
        if (!cars.removeIf(car -> Objects.equals(car.getId(), id))) {
            throw new DaoException("Not found!");
        }
    }
}
