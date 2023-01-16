package edu.bbte.idde.frim1910.desktop;

import edu.bbte.idde.frim1910.backend.config.ConfigBeanFactory;
import edu.bbte.idde.frim1910.backend.dao.CarAdvertisementDao;
import edu.bbte.idde.frim1910.backend.dao.CarDao;
import edu.bbte.idde.frim1910.backend.dao.DaoFactory;
import edu.bbte.idde.frim1910.backend.model.Car;
import edu.bbte.idde.frim1910.backend.model.CarAdvertisement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.time.Instant;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        CarDao carDao = DaoFactory.getInstance().getCarDao();

        Car car1 = new Car(
                "BMW",
                "5S",
                "sedan",
                1981,
                5.0f
        );
        Car car2 = new Car(
                "Audi",
                "TT",
                "hatchback",
                2016,
                2.0f
        );
        Car car3 = new Car(
                "Chevrolet",
                "Camaro SS",
                "coupe",
                2018,
                2.0f
        );

        if (!"mem".equals(ConfigBeanFactory.getProfile())) {
            carDao.create(car1);
            carDao.create(car2);
            carDao.create(car3);
        }

        CarAdvertisementDao carAdvertisementDao = DaoFactory.getInstance().getCarAdvertisementDao();

        CarAdvertisement carAdv1 = new CarAdvertisement(
                "Elado Autooo",
                "Kik a szeme",
                car1.getUuid(),
                new Date(Instant.now().toEpochMilli()),
                18336.5F
        );
        CarAdvertisement carAdv2 = new CarAdvertisement(
                "Elado Dudu",
                "Nagy body",
                car2.getUuid(),
                new Date(Instant.now().toEpochMilli()),
                8675.0F
        );
        CarAdvertisement carAdv3 = new CarAdvertisement(
                "Elado Kocsi",
                "Popec motor",
                car3.getUuid(),
                new Date(Instant.now().toEpochMilli()),
                218647.2F
        );

        carAdvertisementDao.create(carAdv1);
        carAdvertisementDao.create(carAdv2);
        carAdvertisementDao.create(carAdv3);

        LOG.info(carAdvertisementDao.findAll().toString());

        carAdvertisementDao.delete(carAdv1.getUuid());

        LOG.info(carAdvertisementDao.findAll().toString());

        carAdv2.setTitle(carAdv2.getTitle().concat(" - Updated"));
        carAdvertisementDao.update(carAdv2);

        LOG.info(carAdvertisementDao.findByUuid(carAdv2.getUuid()).toString());
        LOG.info(carAdvertisementDao.findByPrice(1000.0f, 10000.0f).toString());
    }
}
