package edu.bbte.idde.frim1910.backend.dao.mem;

import edu.bbte.idde.frim1910.backend.dao.CarAdvertisementDao;
import edu.bbte.idde.frim1910.backend.dao.CarDao;
import edu.bbte.idde.frim1910.backend.dao.DaoFactory;
import edu.bbte.idde.frim1910.backend.dao.LogDao;

public class MemDaoFactory extends DaoFactory {
    private static CarAdvertisementDao carAdvertisementDao;

    @Override
    public synchronized CarDao getCarDao() {
        return null;
    }

    @Override
    public synchronized CarAdvertisementDao getCarAdvertisementDao() {
        if (carAdvertisementDao == null) {
            carAdvertisementDao = new CarAdvertisementMemDao();
        }
        return carAdvertisementDao;
    }

    @Override
    public LogDao getLogDao() {
        return null;
    }
}
