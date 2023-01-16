package edu.bbte.idde.frim1910.spring.repository.jdbc;

import edu.bbte.idde.frim1910.spring.model.CarAdvertisement;
import edu.bbte.idde.frim1910.spring.repository.CarAdvertisementDao;
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
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("jdbc")
public class CarAdvertisementJdbcDao implements CarAdvertisementDao {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CarJdbcDao carJdbcDao;

    @PostConstruct
    public void initialize() {
        try (
                Connection connection = dataSource.getConnection();
                Statement createTable = connection.createStatement();
                Statement dropTable = connection.createStatement();
        ) {
            dropTable.executeUpdate("DROP TABLE IF EXISTS caradvertisement");
            createTable.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS caradvertisement ("
                    + "title VARCHAR, "
                    + "description VARCHAR, "
                    + "creation_date DATE, "
                    + "modification_date DATE, "
                    + "price REAL, "
                    + "car_id UUID, "
                    + "id UUID PRIMARY KEY, "
                    + "CONSTRAINT fk_car FOREIGN KEY(car_id) references car(id)"
                    + ");"
            );
        } catch (SQLException e) {
            throw new DaoException("Table creation has failed!");
        }
    }

    private void setCarAdvertisementAttributes(ResultSet selectResult, CarAdvertisement carAdvertisement)
            throws SQLException {
        carAdvertisement.setTitle(selectResult.getString(1));
        carAdvertisement.setDescription(selectResult.getString(2));
        carAdvertisement.setCreationDate(selectResult.getDate(3));
        carAdvertisement.setModificationDate(selectResult.getDate(4));
        carAdvertisement.setPrice(selectResult.getFloat(5));
        carAdvertisement.setCar(carJdbcDao.findById((UUID)selectResult.getObject(6)).get());
        carAdvertisement.setId((UUID)selectResult.getObject(7));
    }

    private void setPreparedStatementAttributes(CarAdvertisement carAdvertisement, PreparedStatement preparedStatement)
            throws SQLException {
        preparedStatement.setString(1, carAdvertisement.getTitle());
        preparedStatement.setString(2, carAdvertisement.getDescription());
        preparedStatement.setDate(3, carAdvertisement.getCreationDate());
        preparedStatement.setDate(4, carAdvertisement.getModificationDate());
        preparedStatement.setFloat(5, carAdvertisement.getPrice());
        preparedStatement.setObject(6, carAdvertisement.getCar().getId());
        preparedStatement.setObject(7, carAdvertisement.getId());
    }

    @Override
    public Collection<CarAdvertisement> findAll() {
        Collection<CarAdvertisement> carAdvertisements = new LinkedList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement(
                            "SELECT title, description, creation_date, modification_date, price, car_id, id "
                            + "FROM caradvertisement"
                    )
        ) {
            ResultSet selectResult = preparedStatement.executeQuery();
            while (selectResult.next()) {
                CarAdvertisement carAdvertisement = new CarAdvertisement();
                setCarAdvertisementAttributes(selectResult, carAdvertisement);
                carAdvertisements.add(carAdvertisement);
            }
        } catch (SQLException e) {
            throw new DaoException("CarAdvertisement find-all failed!");
        }
        return carAdvertisements;
    }

    @Override
    public Optional<CarAdvertisement> findById(UUID id) {
        CarAdvertisement carAdvertisement = new CarAdvertisement();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement(
                        "SELECT title, description, creation_date, modification_date, price, car_id, id  "
                            + "FROM caradvertisement WHERE id = ?"
                    )
        ) {
            preparedStatement.setObject(1, id);
            ResultSet selectResult = preparedStatement.executeQuery();
            if (selectResult.next()) {
                setCarAdvertisementAttributes(selectResult, carAdvertisement);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("CarAdvertisement find-by-id failed!");
        }
        return Optional.of(carAdvertisement);
    }

    @Override
    public Collection<CarAdvertisement> findCarAdvertisementsByPrice(Float min, Float max) {
        Collection<CarAdvertisement> carAdvertisements = new LinkedList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement(
                        "SELECT title, description, creation_date, modification_date, price, car_id, id "
                            + "FROM caradvertisement WHERE price >= ? AND price <= ?"
                    )
        ) {
            preparedStatement.setFloat(1, min);
            preparedStatement.setFloat(2, max);
            ResultSet selectResult = preparedStatement.executeQuery();
            while (selectResult.next()) {
                CarAdvertisement carAdvertisement = new CarAdvertisement();
                setCarAdvertisementAttributes(selectResult, carAdvertisement);
                carAdvertisements.add(carAdvertisement);
            }
        } catch (SQLException e) {
            throw new DaoException("CarAdvertisement find-by-price failed!");
        }
        return carAdvertisements;
    }

    @Override
    public boolean existsById(UUID id) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement(
                        "SELECT * FROM caradvertisement WHERE id = ?"
                        )
        ) {
            preparedStatement.setObject(1, id);
            ResultSet selectResult = preparedStatement.executeQuery();
            return selectResult.next();
        } catch (SQLException e) {
            throw new DaoException("CarAdvertisement find-by-id failed!");
        }
    }

    @Override
    public CarAdvertisement saveAndFlush(CarAdvertisement entity) {
        if (!existsById(entity.getId())) {
            try (
                    Connection connection = dataSource.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(
                            "INSERT INTO caradvertisement "
                            + "(title, description, creation_date, modification_date, price, car_id,  id) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?)"
                    )
            ) {
                if (entity.getId() == null) {
                    entity.setId(UUID.randomUUID());
                }
                setPreparedStatementAttributes(entity, preparedStatement);
                preparedStatement.execute();
            } catch (SQLException e) {
                throw new DaoException("CarAdvertisement creation failed!");
            }
        }
        return entity;
    }

    @Override
    public void deleteById(UUID id) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement("DELETE FROM caradvertisement WHERE id = ?")
        ) {
            preparedStatement.setObject(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DaoException("CarAdvertisement deletion failed!");
        }
    }
}
