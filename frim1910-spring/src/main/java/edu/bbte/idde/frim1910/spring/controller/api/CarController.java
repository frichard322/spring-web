package edu.bbte.idde.frim1910.spring.controller.api;

import edu.bbte.idde.frim1910.spring.controller.api.exception.NotFoundException;
import edu.bbte.idde.frim1910.spring.dto.incoming.CarCreationDto;
import edu.bbte.idde.frim1910.spring.dto.incoming.CarRentUpdateDto;
import edu.bbte.idde.frim1910.spring.dto.incoming.CarUpdateDto;
import edu.bbte.idde.frim1910.spring.dto.outgoing.CarDto;
import edu.bbte.idde.frim1910.spring.mapper.CarMapper;
import edu.bbte.idde.frim1910.spring.model.Car;
import edu.bbte.idde.frim1910.spring.service.CarService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/cars")
@Log4j2
public class CarController {
    @Autowired
    private CarService service;

    @Autowired
    private CarMapper mapper;

    @GetMapping
    public Collection<CarDto> findAllCars(@RequestParam(value = "brand", required = false) String brand) {
        Collection<Car> cars;
        if (brand == null || "".equals(brand)) {
            cars = service.findAllCars();
        } else {
            cars = service.findCarsByBrand(brand);
        }
        return mapper.modelsToDtos(cars);
    }

    @GetMapping("/{carId}")
    public CarDto findCarById(@PathVariable("carId") UUID carId) {
        Car car = service.findCarById(carId)
                .orElseThrow(() -> new NotFoundException(Car.class, carId));
        return mapper.modelToDto(car);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CarDto createCar(@Valid @RequestBody CarCreationDto carDto) {
        Car car = mapper.creationDtoToModel(carDto);
        car = service.saveCar(car);
        return mapper.modelToDto(car);
    }

    @PatchMapping("/{carId}")
    public CarDto updateCar(@PathVariable("carId") UUID carId, @Valid @RequestBody CarUpdateDto carUpdateDto) {
        Car car = service.findCarById(carId)
                .orElseThrow(() -> new NotFoundException(Car.class, carId));
        car = mapper.updateModelByDto(carUpdateDto, car);
        car = service.saveCar(car);
        return mapper.modelToDto(car);
    }

    @PatchMapping("/")
    public void updateCarsByDate(
            @RequestParam(value = "rentedAfter", required = false) Long rentedAfter,
            @Valid @RequestBody CarRentUpdateDto carUpdateDto
    ) {
        service.updateCarsByDate(
                Timestamp.from(Instant.ofEpochSecond(rentedAfter)), carUpdateDto.getBrand(), carUpdateDto.getModel());
    }

    @DeleteMapping("/{carId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable("carId") UUID carId) {
        if (!service.existsCarById(carId)) {
            throw new NotFoundException(Car.class, carId);
        }
        service.deleteCar(carId);
    }
}

