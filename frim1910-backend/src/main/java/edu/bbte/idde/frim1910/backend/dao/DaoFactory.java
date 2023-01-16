package edu.bbte.idde.frim1910.backend.dao;

import edu.bbte.idde.frim1910.backend.config.ConfigBean;
import edu.bbte.idde.frim1910.backend.config.ConfigBeanFactory;
import edu.bbte.idde.frim1910.backend.dao.jdbc.JdbcDaoFactory;
import edu.bbte.idde.frim1910.backend.dao.mem.MemDaoFactory;

public abstract class DaoFactory {
    private static DaoFactory INSTANCE;

    public static synchronized DaoFactory getInstance() {
        if (INSTANCE == null) {
            ConfigBean configBean = ConfigBeanFactory.getConfigBean();
            if ("jdbc".equals(configBean.getDaoFactoryImplementation())) {
                INSTANCE = new JdbcDaoFactory();
            } else {
                INSTANCE = new MemDaoFactory();
            }
        }
        return INSTANCE;
    }

    public abstract CarDao getCarDao();

    public abstract CarAdvertisementDao getCarAdvertisementDao();

    public abstract LogDao getLogDao();
}

