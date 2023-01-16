package edu.bbte.idde.frim1910.spring.repository.jpa;

import edu.bbte.idde.frim1910.spring.model.CarAdvertisement;
import edu.bbte.idde.frim1910.spring.repository.CarAdvertisementDao;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
@Profile("jpa")
public interface CarAdvertisementRepository extends CarAdvertisementDao, JpaRepository<CarAdvertisement, UUID> {
    @Override
    @Query(
            value = "SELECT title, description, creation_date, modification_date, price, car_id, id "
                    + "FROM caradvertisement WHERE price >= :min AND price <= :max",
            nativeQuery = true
    )
    Collection<CarAdvertisement> findCarAdvertisementsByPrice(@Param("min") Float min, @Param("max") Float max);
}
