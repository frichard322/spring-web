package edu.bbte.idde.frim1910.spring.repository.jpa;

import edu.bbte.idde.frim1910.spring.model.Car;
import edu.bbte.idde.frim1910.spring.repository.CarDao;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.UUID;

@Repository
@Profile("jpa")
public interface CarRepository extends CarDao, JpaRepository<Car, UUID> {
    @Override
    @Query(
            value = "SELECT brand, model, type, year, engine, lastRent, id FROM car WHERE brand = :brand",
            nativeQuery = true
    )
    Collection<Car> findCarsByBrand(@Param("brand")String brand);

    @Override
    @Query(
            value = "UPDATE Car SET brand = :brand, model = :model WHERE last_rent > :lastRent",
            nativeQuery = true
    )
    void updateCarsByDate(
            @Param("lastRent") Timestamp lastRent,
            @Param("brand") String brand,
            @Param("model") String model
    );
}
