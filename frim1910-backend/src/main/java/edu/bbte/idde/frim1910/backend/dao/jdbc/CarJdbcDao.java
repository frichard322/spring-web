package edu.bbte.idde.frim1910.backend.dao.jdbc;

import edu.bbte.idde.frim1910.backend.dao.CarDao;
import edu.bbte.idde.frim1910.backend.dao.DaoException;
import edu.bbte.idde.frim1910.backend.model.Car;
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

public class CarJdbcDao extends AbstractJdbcDao<Car> implements CarDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarJdbcDao.class);

    public CarJdbcDao() {
        super();
        try (
                Connection connection = dataSource.getConnection();
                Statement createTable = connection.createStatement();
                Statement dropTable = connection.createStatement();
                Statement dropConstraint = connection.createStatement()
        ) {
            dropConstraint.executeUpdate(
                    "ALTER TABLE IF EXISTS CarAdvertisement "
                    + "  DROP CONSTRAINT IF EXISTS fk_car"
            );
            dropTable.executeUpdate("DROP TABLE IF EXISTS Car");
            createTable.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Car ("
                    + "brand VARCHAR, "
                    + "model VARCHAR, "
                    + "type VARCHAR, "
                    + "year INTEGER, "
                    + "engine REAL, "
                    + "uuid UUID PRIMARY KEY"
                    + ");"
            );
        } catch (SQLException e) {
            LOGGER.error("Table creation has failed!", e);
            throw new DaoException("Table creation has failed!", e);
        }
        LOGGER.info("Table 'Car' has been successfully created!");
    }

    private void setCarAttributes(ResultSet selectResult, Car car) throws SQLException {
        car.setBrand(selectResult.getString(1));
        car.setModel(selectResult.getString(2));
        car.setType(selectResult.getString(3));
        car.setYear(selectResult.getInt(4));
        car.setEngine(selectResult.getFloat(5));
        car.setUuid((UUID)selectResult.getObject(6));
    }

    private void setPreparedStatementAttributes(Car car, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, car.getBrand());
        preparedStatement.setString(2, car.getModel());
        preparedStatement.setString(3, car.getType());
        preparedStatement.setInt(4, car.getYear());
        preparedStatement.setFloat(5, car.getEngine());
        preparedStatement.setObject(6, car.getUuid());
    }

    @Override
    public void create(Car entity) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Car (brand, model, type, year, engine, uuid) VALUES (?, ?, ?, ?, ?, ?)"
                )
        ) {
            setPreparedStatementAttributes(entity, preparedStatement);
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error("Car creation failed!", e);
            throw new DaoException("Car creation failed!", e);
        }
        LOGGER.info("Created: {}", entity);
    }

    @Override
    public Collection<Car> findAll() {
        Collection<Car> cars = new LinkedList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT brand, model, type, year, engine, uuid FROM Car")
        ) {
            ResultSet selectResult = preparedStatement.executeQuery();
            Car car = new Car();
            while (selectResult.next()) {
                setCarAttributes(selectResult, car);
                cars.add(car);
            }
        } catch (SQLException e) {
            LOGGER.error("Car find-all failed!", e);
            throw new DaoException("Car find-all failed!", e);
        }
        LOGGER.info("Found: {}", cars);
        return cars;
    }

    @Override
    public Car findByUuid(UUID uuid) {
        Car car = new Car();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT brand, model, type, year, engine, uuid FROM Car WHERE uuid = ?")
        ) {
            preparedStatement.setObject(1, uuid);
            ResultSet selectResult = preparedStatement.executeQuery();
            if (selectResult.next()) {
                setCarAttributes(selectResult, car);
            } else {
                throw new DaoException("Not found!");
            }
        } catch (SQLException e) {
            LOGGER.error("Car find-by-uuid failed!", e);
            throw new DaoException("Car find-by-uuid failed!", e);
        }
        LOGGER.info("Found: {}", car);
        return car;
    }

    @Override
    public void delete(UUID uuid) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement("DELETE FROM Car WHERE uuid = ?")
        ) {
            preparedStatement.setObject(1, uuid);
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error("Car deletion failed!", e);
            throw new DaoException("Car deletion failed!", e);
        }
        LOGGER.info("Deleted: {}", uuid);
    }

    @Override
    public void update(Car entity) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Car "
                         + "SET brand = ?, "
                         + "model = ?, "
                         + "type = ?, "
                         + "year = ?, "
                         + "engine = ? "
                         + "WHERE uuid = ?")
        ) {
            setPreparedStatementAttributes(entity, preparedStatement);
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error("Car update failed!", e);
            throw new DaoException("Car update failed!", e);
        }
        LOGGER.info("Updated: {}", entity.getUuid());
    }

    @Override
    public Collection<Car> findByBrand(String brand) {
        Collection<Car> cars = new LinkedList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT brand, model, type, year, engine, uuid FROM Car WHERE brand = ?")
        ) {
            preparedStatement.setString(1, brand);
            ResultSet selectResult = preparedStatement.executeQuery();
            Car car = new Car();
            while (selectResult.next()) {
                setCarAttributes(selectResult, car);
                cars.add(car);
            }
        } catch (SQLException e) {
            LOGGER.error("Car find-by-brand failed!", e);
            throw new DaoException("Car find-by-brand failed!", e);
        }
        LOGGER.info("Found by brand: {}", cars);
        return cars;
    }
}
