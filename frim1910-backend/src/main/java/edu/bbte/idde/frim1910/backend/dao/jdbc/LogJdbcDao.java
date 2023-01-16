package edu.bbte.idde.frim1910.backend.dao.jdbc;

import edu.bbte.idde.frim1910.backend.dao.DaoException;
import edu.bbte.idde.frim1910.backend.dao.LogDao;
import edu.bbte.idde.frim1910.backend.model.EntityOperationLog;
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

public class LogJdbcDao extends AbstractJdbcDao<EntityOperationLog> implements LogDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogJdbcDao.class);

    public LogJdbcDao() {
        super();
        try (
                Connection connection = dataSource.getConnection();
                Statement createTable = connection.createStatement();
                Statement dropTable = connection.createStatement()
        ) {
            dropTable.executeUpdate("DROP TABLE IF EXISTS EntityOperationLog");
            createTable.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS EntityOperationLog ("
                    + "date TIMESTAMP, "
                    + "operation VARCHAR, "
                    + "entityName VARCHAR, "
                    + "entityId UUID, "
                    + "uuid UUID PRIMARY KEY"
                    + ");"
            );
        } catch (SQLException e) {
            LOGGER.error("Table creation has failed!", e);
            throw new DaoException("Table creation has failed!", e);
        }
        LOGGER.info("Table 'EntityOperationLog' has been successfully created!");
    }

    private void setOpLogAttributes(ResultSet selectResult, EntityOperationLog opLog) throws SQLException {
        opLog.setDate(selectResult.getTimestamp(1));
        opLog.setOperation(selectResult.getString(2));
        opLog.setEntityName(selectResult.getString(3));
        opLog.setEntityId((UUID) selectResult.getObject(4));
        opLog.setUuid((UUID) selectResult.getObject(5));
    }

    private void setPreparedStatementAttributes(EntityOperationLog opLog,
                                                PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setTimestamp(1, opLog.getDate());
        preparedStatement.setString(2, opLog.getOperation());
        preparedStatement.setString(3, opLog.getEntityName());
        preparedStatement.setObject(4, opLog.getEntityId());
        preparedStatement.setObject(5, opLog.getUuid());
    }

    @Override
    public void create(EntityOperationLog entity) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO EntityOperationLog (date, operation, entityName, entityId, uuid) "
                            + "VALUES (?, ?, ?, ?, ?)"
                )
        ) {
            setPreparedStatementAttributes(entity, preparedStatement);
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error("EntityOperationLog creation failed!", e);
            throw new DaoException("EntityOperationLog creation failed!", e);
        }
        LOGGER.info("Created: {}", entity);
    }

    @Override
    public Collection<EntityOperationLog> findAll() {
        Collection<EntityOperationLog> opLogs = new LinkedList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT date, operation, entityName, entityId, uuid FROM EntityOperationLog")
        ) {
            ResultSet selectResult = preparedStatement.executeQuery();
            EntityOperationLog opLog = new EntityOperationLog();
            while (selectResult.next()) {
                setOpLogAttributes(selectResult, opLog);
                opLogs.add(opLog);
            }
        } catch (SQLException e) {
            LOGGER.error("EntityOperationLog find-all failed!", e);
            throw new DaoException("EntityOperationLog find-all failed!", e);
        }
        LOGGER.info("Found: {}", opLogs);
        return opLogs;
    }

    @Override
    public EntityOperationLog findByUuid(UUID uuid) {
        throw new DaoException("Operation not supported!");
    }

    @Override
    public void delete(UUID uuid) {
        throw new DaoException("Operation not supported!");
    }

    @Override
    public void update(EntityOperationLog entity) {
        throw new DaoException("Operation not supported!");
    }
}
