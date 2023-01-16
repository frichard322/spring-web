package edu.bbte.idde.frim1910.spring.service;

import edu.bbte.idde.frim1910.spring.model.Car;
import edu.bbte.idde.frim1910.spring.repository.CarDao;
import edu.bbte.idde.frim1910.spring.service.exception.ServiceException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class CarService {
    @Autowired
    private CarDao carRepository;

    public Collection<Car> findAllCars() {
        try {
            return carRepository.findAll();
        } catch (DataAccessException e) {
            log.error("findAllCars failed");
            throw new ServiceException("findAllCars failed");
        }
    }

    public Collection<Car> findCarsByBrand(String brand) {
        try {
            return carRepository.findCarsByBrand(brand);
        } catch (DataAccessException e) {
            log.error("findCarsByBrand failed");
            throw new ServiceException("findCarsByBrand failed");
        }
    }

    public Optional<Car> findCarById(UUID id) {
        try {
            return carRepository.findById(id);
        } catch (IllegalArgumentException e) {
            log.error("findCarById failed");
            throw new ServiceException("findCarById failed");
        }
    }

    public Car saveCar(Car car) {
        try {
            return carRepository.saveAndFlush(car);
        } catch (DataIntegrityViolationException e) {
            log.error("saveCar failed");
            throw new ServiceException("saveCar failed");
        }
    }

    public boolean existsCarById(UUID id) {
        try {
            return carRepository.existsById(id);
        } catch (IllegalArgumentException e) {
            log.error("existsCarById failed");
            throw new ServiceException("existsCarById failed");
        }
    }

    public void deleteCar(UUID id) {
        try {
            carRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            log.error("deleteCar failed");
            throw new ServiceException("deleteCar failed");
        }
    }

    public void updateCarsByDate(Timestamp rentedAfter, String brand, String model) {
        try {
            carRepository.updateCarsByDate(rentedAfter, brand, model);
        } catch (IllegalArgumentException e) {
            log.error("deleteCar failed");
            throw new ServiceException("deleteCar failed");
        }
    }
}
