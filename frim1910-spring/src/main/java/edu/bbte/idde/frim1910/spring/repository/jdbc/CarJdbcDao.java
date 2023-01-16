package edu.bbte.idde.frim1910.spring.repository.jdbc;

import edu.bbte.idde.frim1910.spring.model.Car;
import edu.bbte.idde.frim1910.spring.repository.CarDao;
import edu.bbte.idde.frim1910.spring.repository.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("jdbc")
public class CarJdbcDao implements CarDao {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initialize() {
        try (
                Connection connection = dataSource.getConnection();
                Statement createTable = connection.createStatement();
                Statement dropTable = connection.createStatement();
                Statement dropConstraint = connection.createStatement()
        ) {
            dropConstraint.executeUpdate(
                    "ALTER TABLE IF EXISTS caradvertisement "
                    + "  DROP CONSTRAINT IF EXISTS fk_car"
            );
            dropTable.executeUpdate("DROP TABLE IF EXISTS car");
            createTable.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS car ("
                    + "brand VARCHAR, "
                    + "model VARCHAR, "
                    + "type VARCHAR, "
                    + "year INTEGER, "
                    + "engine REAL, "
                    + "id UUID PRIMARY KEY"
                    + ");"
            );
        } catch (SQLException e) {
            throw new DaoException("Table creation has failed!");
        }
    }

    private void setCarAttributes(ResultSet selectResult, Car car) throws SQLException {
        car.setBrand(selectResult.getString(1));
        car.setModel(selectResult.getString(2));
        car.setType(selectResult.getString(3));
        car.setYear(selectResult.getInt(4));
        car.setEngine(selectResult.getFloat(5));
        car.setId((UUID)selectResult.getObject(6));
    }

    private void setPreparedStatementAttributes(Car car, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, car.getBrand());
        preparedStatement.setString(2, car.getModel());
        preparedStatement.setString(3, car.getType());
        preparedStatement.setInt(4, car.getYear());
        preparedStatement.setFloat(5, car.getEngine());
        preparedStatement.setObject(6, car.getId());
    }

    @Override
    public Collection<Car> findAll() {
        Collection<Car> cars = new LinkedList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT brand, model, type, year, engine, id FROM car")
        ) {
            ResultSet selectResult = preparedStatement.executeQuery();
            while (selectResult.next()) {
                Car car = new Car();
                setCarAttributes(selectResult, car);
                cars.add(car);
            }
        } catch (SQLException e) {
            throw new DaoException("Car find-all failed!");
        }
        return cars;
    }

    @Override
    public Optional<Car> findById(UUID id) {
        Car car = new Car();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT brand, model, type, year, engine, id FROM car WHERE id = ?")
        ) {
            preparedStatement.setObject(1, id);
            ResultSet selectResult = preparedStatement.executeQuery();
            if (selectResult.next()) {
                setCarAttributes(selectResult, car);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("Car find-by-id failed!");
        }
        return Optional.of(car);
    }

    @Override
    public Collection<Car> findCarsByBrand(String brand) {
        Collection<Car> cars = new LinkedList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT brand, model, type, year, engine, id FROM car WHERE brand = ?")
        ) {
            preparedStatement.setString(1, brand);
            ResultSet selectResult = preparedStatement.executeQuery();
            while (selectResult.next()) {
                Car car = new Car();
                setCarAttributes(selectResult, car);
                cars.add(car);
            }
        } catch (SQLException e) {
            throw new DaoException("Car find-by-brand failed!");
        }
        return cars;
    }

    @Override
    public void updateCarsByDate(Timestamp lastRent, String brand, String model) {

    }

    @Override
    public boolean existsById(UUID id) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT * FROM car WHERE id = ?")
        ) {
            preparedStatement.setObject(1, id);
            ResultSet selectResult = preparedStatement.executeQuery();
            return selectResult.next();
        } catch (SQLException e) {
            throw new DaoException("Car exists-by-id failed!");
        }
    }

    @Override
    public Car saveAndFlush(Car entity) {
        if (!existsById(entity.getId())) {
            try (
                    Connection connection = dataSource.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(
                            "INSERT INTO car (brand, model, type, year, engine, id) VALUES (?, ?, ?, ?, ?, ?)"
                    )
            ) {
                if (entity.getId() == null) {
                    entity.setId(UUID.randomUUID());
                }
                setPreparedStatementAttributes(entity, preparedStatement);
                preparedStatement.execute();
            } catch (SQLException e) {
                throw new DaoException("Car creation failed!");
            }
        }
        return entity;
    }

    @Override
    public void deleteById(UUID id) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement("DELETE FROM car WHERE id = ?")
        ) {
            preparedStatement.setObject(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DaoException("Car deletion failed!");
        }
    }
}
