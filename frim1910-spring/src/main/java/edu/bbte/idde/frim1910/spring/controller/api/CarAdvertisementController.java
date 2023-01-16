package edu.bbte.idde.frim1910.spring.controller.api;

import edu.bbte.idde.frim1910.spring.controller.api.exception.NotFoundException;
import edu.bbte.idde.frim1910.spring.dto.incoming.CarAdvertisementCreationDto;
import edu.bbte.idde.frim1910.spring.dto.outgoing.CarAdvertisementDetailedDto;
import edu.bbte.idde.frim1910.spring.dto.outgoing.CarAdvertisementReducedDto;
import edu.bbte.idde.frim1910.spring.mapper.CarAdvertisementMapper;
import edu.bbte.idde.frim1910.spring.model.Car;
import edu.bbte.idde.frim1910.spring.model.CarAdvertisement;
import edu.bbte.idde.frim1910.spring.service.CarService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("api/cars/{carId}/advertisements")
@Log4j2
public class CarAdvertisementController {
    @Autowired
    private CarService carService;

    @Autowired
    private CarAdvertisementMapper mapper;

    @GetMapping
    public Collection<CarAdvertisementReducedDto> findAllCarAdvertisements(@PathVariable("carId") UUID carId) {
        Car car = carService.findCarById(carId)
                .orElseThrow(() -> new NotFoundException(Car.class, carId));
        return mapper.modelsToReducedDtos(car.getCarAdvertisements());
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CarAdvertisementDetailedDto createCarAdvertisement(
            @PathVariable("carId") UUID carId,
            @Valid @RequestBody CarAdvertisementCreationDto carAdvertisementCreationDto
    ) {
        Car car = carService.findCarById(carId)
                .orElseThrow(() -> new NotFoundException(Car.class, carId));
        CarAdvertisement carAdvertisement = mapper.dtoToModel(carAdvertisementCreationDto, car);
        car.getCarAdvertisements().add(carAdvertisement);
        carService.saveCar(car);
        return mapper.modelToDetailedDto(car.getCarAdvertisements().get(car.getCarAdvertisements().size() - 1));
    }

    @DeleteMapping("/{carAdvertisementId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCarAdvertisement(
            @PathVariable("carId") UUID carId,
            @PathVariable("carAdvertisementId") UUID carAdvertisementId
    ) {
        Car car = carService.findCarById(carId)
                .orElseThrow(() -> new NotFoundException(Car.class, carId));
        CarAdvertisement carAdvertisement = car.getCarAdvertisements()
                .stream()
                .filter(carAdv -> carAdv.getId().equals(carAdvertisementId))
                .findFirst().orElseThrow(() -> new NotFoundException(CarAdvertisement.class, carAdvertisementId));
        car.getCarAdvertisements().remove(carAdvertisement);
        carService.saveCar(car);
    }
}
