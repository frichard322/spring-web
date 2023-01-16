package edu.bbte.idde.frim1910.backend.dao.jdbc;

import edu.bbte.idde.frim1910.backend.dao.CarAdvertisementDao;
import edu.bbte.idde.frim1910.backend.dao.CarDao;
import edu.bbte.idde.frim1910.backend.dao.DaoFactory;
import edu.bbte.idde.frim1910.backend.dao.LogDao;

public class JdbcDaoFactory extends DaoFactory {
    private static CarDao carDao;
    private static CarAdvertisementDao carAdvertisementDao;
    private static LogDao logDao;

    @Override
    public synchronized CarDao getCarDao() {
        if (carDao == null) {
            carDao = new CarJdbcDao();
        }
        return carDao;
    }

    @Override
    public synchronized CarAdvertisementDao getCarAdvertisementDao() {
        if (carAdvertisementDao == null) {
            carAdvertisementDao = new CarAdvertisementJdbcDao();
        }
        return carAdvertisementDao;
    }

    @Override
    public synchronized LogDao getLogDao() {
        if (logDao == null) {
            logDao = new LogJdbcDao();
        }
        return logDao;
    }
}

