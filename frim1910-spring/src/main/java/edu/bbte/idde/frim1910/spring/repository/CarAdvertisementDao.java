package edu.bbte.idde.frim1910.spring.repository;

import edu.bbte.idde.frim1910.spring.model.CarAdvertisement;

import java.util.Collection;

public interface CarAdvertisementDao extends Dao<CarAdvertisement> {
    Collection<CarAdvertisement> findCarAdvertisementsByPrice(Float min, Float max);
}
