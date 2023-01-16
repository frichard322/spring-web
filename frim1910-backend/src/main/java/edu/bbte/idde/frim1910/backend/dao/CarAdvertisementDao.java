package edu.bbte.idde.frim1910.backend.dao;

import edu.bbte.idde.frim1910.backend.model.CarAdvertisement;

import java.util.Collection;

public interface CarAdvertisementDao extends Dao<CarAdvertisement> {
    Collection<CarAdvertisement> findByPrice(Float min, Float max);
}
