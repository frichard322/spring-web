package edu.bbte.idde.frim1910.backend.dao.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import edu.bbte.idde.frim1910.backend.config.ConfigBean;
import edu.bbte.idde.frim1910.backend.config.ConfigBeanFactory;
import edu.bbte.idde.frim1910.backend.dao.DaoException;
import edu.bbte.idde.frim1910.backend.model.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractJdbcDao<T extends BaseEntity> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJdbcDao.class);
    protected HikariDataSource dataSource;

    public AbstractJdbcDao() {
        try {
            ConfigBean configBean = ConfigBeanFactory.getConfigBean();
            Class.forName(configBean.getJdbcClassName());
            dataSource = new HikariDataSource();
            dataSource.setDriverClassName(configBean.getJdbcClassName());
            dataSource.setJdbcUrl(configBean.getJdbcURL());
            dataSource.setUsername(configBean.getUsername());
            dataSource.setPassword(configBean.getPassword());
            dataSource.setMaximumPoolSize(configBean.getPoolSize());
        } catch (RepositoryException e) {
            LOGGER.error("Database connection problem!", e);
            throw new DaoException("Database connection problem!", e);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Table could not be created!", e);
            throw new DaoException("Table could not be created!", e);
        }
    }
}
