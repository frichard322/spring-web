package edu.bbte.idde.frim1910.backend.dao.mem;

import edu.bbte.idde.frim1910.backend.dao.CarAdvertisementDao;
import edu.bbte.idde.frim1910.backend.dao.DaoException;
import edu.bbte.idde.frim1910.backend.model.CarAdvertisement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;
import java.util.stream.Collectors;

public class CarAdvertisementMemDao implements CarAdvertisementDao {

    private final Collection<CarAdvertisement> advertisements;
    private static final Logger LOG = LoggerFactory.getLogger(CarAdvertisementMemDao.class);

    public CarAdvertisementMemDao() {
        this.advertisements = new LinkedList<>();
        LOG.info("CarAdvertisementMemDao has been instantiated!");
    }

    @Override
    public void create(CarAdvertisement carAdvertisement) {
        if (carAdvertisement.getTitle() == null
                || carAdvertisement.getDescription() == null
                || carAdvertisement.getCarUuid() == null
                || carAdvertisement.getDate() == null
                || carAdvertisement.getPrice() == null) {
            LOG.error("Null property found!");
            throw new DaoException("Null property found!");
        }
        advertisements.add(carAdvertisement);
        LOG.info("{} has been created!", carAdvertisement);
    }

    @Override
    public void delete(UUID uuid) {
        if (advertisements.removeIf(adv -> uuid.equals(adv.getUuid()))) {
            LOG.info("{} uuid has been removed!", uuid);
        } else {
            LOG.error("Not found!");
            throw new DaoException("Not found!");
        }
    }

    @Override
    public void update(CarAdvertisement carAdvertisement) {
        if (carAdvertisement.getTitle() == null
                || carAdvertisement.getDescription() == null
                || carAdvertisement.getCarUuid() == null
                || carAdvertisement.getDate() == null
                || carAdvertisement.getPrice() == null) {
            LOG.error("Null property found!");
            throw new DaoException("Null property found!");
        }
        if (advertisements.removeIf(adv -> carAdvertisement.getUuid().equals(adv.getUuid()))) {
            CarAdvertisement newCarAdvertisement = new CarAdvertisement(
                    carAdvertisement.getTitle(),
                    carAdvertisement.getDescription(),
                    carAdvertisement.getCarUuid(),
                    carAdvertisement.getDate(),
                    carAdvertisement.getPrice()
            );
            newCarAdvertisement.setUuid(carAdvertisement.getUuid());
            advertisements.add(newCarAdvertisement);
            LOG.info("{} has been updated!", newCarAdvertisement);
        } else {
            LOG.error("Not found!");
            throw new DaoException("Not found!");
        }
    }

    @Override
    public Collection<CarAdvertisement> findAll() {
        LOG.info("Found: {}", advertisements);
        return advertisements;
    }

    @Override
    public CarAdvertisement findByUuid(UUID uuid) {
        for (CarAdvertisement adv : advertisements) {
            if (adv.getUuid().equals(uuid)) {
                LOG.info("Found: {}", adv);
                return adv;
            }
        }
        LOG.error("Not found!");
        throw new DaoException("Not found!");
    }

    @Override
    public Collection<CarAdvertisement> findByPrice(Float min, Float max) {
        LOG.info("Found: {}", advertisements);
        return advertisements
            .stream()
            .filter(carAdvertisement -> carAdvertisement.getPrice() >= min && carAdvertisement.getPrice() <= max)
            .collect(Collectors.toList());
    }
}
