package edu.bbte.idde.frim1910.spring.repository.mem;

import edu.bbte.idde.frim1910.spring.model.CarAdvertisement;
import edu.bbte.idde.frim1910.spring.repository.CarAdvertisementDao;
import edu.bbte.idde.frim1910.spring.repository.exception.DaoException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Profile("mem")
public class CarAdvertisementMemDao implements CarAdvertisementDao {

    private final Collection<CarAdvertisement> advertisements = new LinkedList<>();

    @Override
    public Collection<CarAdvertisement> findAll() {
        return advertisements;
    }

    @Override
    public Collection<CarAdvertisement> findCarAdvertisementsByPrice(Float min, Float max) {
        return advertisements
            .stream()
            .filter(carAdvertisement -> carAdvertisement.getPrice() >= min && carAdvertisement.getPrice() <= max)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<CarAdvertisement> findById(UUID id) {
        return advertisements
            .stream()
            .filter(carAdvertisement -> Objects.equals(carAdvertisement.getId(), id))
            .findFirst();
    }

    @Override
    public boolean existsById(UUID id) {
        return advertisements
            .stream()
            .anyMatch(carAdvertisement -> Objects.equals(carAdvertisement.getId(), id));
    }

    @Override
    public CarAdvertisement saveAndFlush(CarAdvertisement entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        advertisements.add(entity);
        return entity;
    }

    @Override
    public void deleteById(UUID id) {
        if (!advertisements.removeIf(carAdvertisement -> Objects.equals(carAdvertisement.getId(), id))) {
            throw new DaoException("Not found!");
        }
    }
}
