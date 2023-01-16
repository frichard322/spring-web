package edu.bbte.idde.frim1910.backend.dao.jdbc;

import edu.bbte.idde.frim1910.backend.dao.CarAdvertisementDao;
import edu.bbte.idde.frim1910.backend.dao.DaoException;
import edu.bbte.idde.frim1910.backend.model.CarAdvertisement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

public class CarAdvertisementJdbcDao extends AbstractJdbcDao<CarAdvertisement> implements CarAdvertisementDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarAdvertisementJdbcDao.class);

    public CarAdvertisementJdbcDao() {
        super();
        try (
                Connection connection = dataSource.getConnection();
                Statement createTable = connection.createStatement();
                Statement dropTable = connection.createStatement();
        ) {
            dropTable.executeUpdate("DROP TABLE IF EXISTS CarAdvertisement");
            createTable.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS CarAdvertisement ("
                    + "title VARCHAR, "
                    + "description VARCHAR, "
                    + "carUuid UUID, "
                    + "date_ DATE, "
                    + "price REAL, "
                    + "uuid UUID PRIMARY KEY, "
                    + "CONSTRAINT fk_car FOREIGN KEY(carUuid) references Car(uuid)"
                    + ");"
            );
        } catch (SQLException e) {
            LOGGER.error("Table creation has failed!", e);
            throw new DaoException("Table creation has failed!", e);
        }
        LOGGER.info("Table 'CarAdvertisement' has been successfully created!");
    }

    private void setCarAdvertisementAttributes(ResultSet selectResult, CarAdvertisement carAdvertisement)
            throws SQLException {
        carAdvertisement.setTitle(selectResult.getString(1));
        carAdvertisement.setDescription(selectResult.getString(2));
        carAdvertisement.setCarUuid((UUID)selectResult.getObject(3));
        carAdvertisement.setDate(selectResult.getDate(4));
        carAdvertisement.setPrice(selectResult.getFloat(5));
        carAdvertisement.setUuid((UUID)selectResult.getObject(6));
    }

    private void setPreparedStatementAttributes(CarAdvertisement carAdvertisement, PreparedStatement preparedStatement)
            throws SQLException {
        preparedStatement.setString(1, carAdvertisement.getTitle());
        preparedStatement.setString(2, carAdvertisement.getDescription());
        preparedStatement.setObject(3, carAdvertisement.getCarUuid());
        preparedStatement.setDate(4, carAdvertisement.getDate());
        preparedStatement.setFloat(5, carAdvertisement.getPrice());
        preparedStatement.setObject(6, carAdvertisement.getUuid());
    }

    @Override
    public void create(CarAdvertisement entity) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO CarAdvertisement (title, description, carUuid, date_, price, uuid) "
                        + "VALUES (?, ?, ?, ?, ?, ?)"
                )
        ) {
            setPreparedStatementAttributes(entity, preparedStatement);
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error("CarAdvertisement creation failed!", e);
            throw new DaoException("CarAdvertisement creation failed!", e);
        }
        LOGGER.info("Created: {}", entity);
    }

    @Override
    public Collection<CarAdvertisement> findAll() {
        Collection<CarAdvertisement> carAdvertisements = new LinkedList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement(
                            "SELECT title, description, carUuid, date_, price, uuid FROM CarAdvertisement"
                        )
        ) {
            ResultSet selectResult = preparedStatement.executeQuery();
            CarAdvertisement carAdvertisement = new CarAdvertisement();
            while (selectResult.next()) {
                setCarAdvertisementAttributes(selectResult, carAdvertisement);
                carAdvertisements.add(carAdvertisement);
            }
        } catch (SQLException e) {
            LOGGER.error("CarAdvertisement find-all failed!", e);
            throw new DaoException("CarAdvertisement find-all failed!", e);
        }
        LOGGER.info("Found: {}", carAdvertisements);
        return carAdvertisements;
    }

    @Override
    public CarAdvertisement findByUuid(UUID uuid) {
        CarAdvertisement carAdvertisement = new CarAdvertisement();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement(
                                "SELECT title, description, carUuid, date_, price, uuid  "
                                + "FROM CarAdvertisement WHERE uuid = ?"
                        )
        ) {
            preparedStatement.setObject(1, uuid);
            ResultSet selectResult = preparedStatement.executeQuery();
            if (selectResult.next()) {
                setCarAdvertisementAttributes(selectResult, carAdvertisement);
            } else {
                throw new DaoException("Not found!");
            }
        } catch (SQLException e) {
            LOGGER.error("CarAdvertisement find-by-uuid failed!", e);
            throw new DaoException("CarAdvertisement find-by-uuid failed!", e);
        }
        LOGGER.info("Found: {}", carAdvertisement);
        return carAdvertisement;
    }

    @Override
    public void delete(UUID uuid) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement("DELETE FROM CarAdvertisement WHERE uuid = ?")
        ) {
            preparedStatement.setObject(1, uuid);
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error("CarAdvertisement deletion failed!", e);
            throw new DaoException("CarAdvertisement deletion failed!", e);
        }
        LOGGER.info("Deleted: {}", uuid);
    }

    @Override
    public void update(CarAdvertisement entity) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE CarAdvertisement "
                        + "SET title = ?, "
                        + "description = ?, "
                        + "carUuid = ?, "
                        + "date_ = ?, "
                        + "price = ? "
                        + "WHERE uuid = ?")
        ) {
            setPreparedStatementAttributes(entity, preparedStatement);
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error("CarAdvertisement update failed!", e);
            throw new DaoException("CarAdvertisement update failed!", e);
        }
        LOGGER.info("Updated: {}", entity.getUuid());
    }

    @Override
    public Collection<CarAdvertisement> findByPrice(Float min, Float max) {
        Collection<CarAdvertisement> carAdvertisements = new LinkedList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement(
                                "SELECT title, description, carUuid, date_, price, uuid "
                                + "FROM CarAdvertisement WHERE price >= ? AND price <= ?"
                        )
        ) {
            preparedStatement.setFloat(1, min);
            preparedStatement.setFloat(2, max);
            ResultSet selectResult = preparedStatement.executeQuery();
            CarAdvertisement carAdvertisement = new CarAdvertisement();
            while (selectResult.next()) {
                setCarAdvertisementAttributes(selectResult, carAdvertisement);
                carAdvertisements.add(carAdvertisement);
            }
        } catch (SQLException e) {
            LOGGER.error("CarAdvertisement find-by-brand failed!", e);
            throw new DaoException("CarAdvertisement find-by-brand failed!", e);
        }
        LOGGER.info("Found by price: {}", carAdvertisements);
        return carAdvertisements;
    }
}
