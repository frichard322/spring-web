package edu.bbte.idde.frim1910.web.servlet;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.idde.frim1910.backend.dao.CarDao;
import edu.bbte.idde.frim1910.backend.dao.DaoException;
import edu.bbte.idde.frim1910.backend.dao.DaoFactory;
import edu.bbte.idde.frim1910.backend.dao.LogDao;
import edu.bbte.idde.frim1910.backend.model.Car;
import edu.bbte.idde.frim1910.backend.model.EntityOperationLog;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@WebServlet("/api/cars")
public class CarServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(CarServlet.class);
    private ObjectMapper objectMapper;
    private CarDao carDao;
    private LogDao logDao;

    @Override
    public void init() throws ServletException {
        super.init();
        carDao = DaoFactory.getInstance().getCarDao();
        objectMapper = new ObjectMapper();
        logDao = DaoFactory.getInstance().getLogDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LOG.info("{} {}", req.getMethod(), req.getRequestURI());
        resp.setHeader("Content-Type", "application/json");
        try {
            String param = req.getParameter("id");
            if (param == null) {
                objectMapper.writeValue(resp.getOutputStream(), carDao.findAll());
            } else {
                UUID id = UUID.fromString(param);
                Car car = carDao.findByUuid(id);
                objectMapper.writeValue(resp.getOutputStream(), car);
            }
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (DaoException e) {
            LOG.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            LOG.info("{} {}", req.getMethod(), req.getRequestURI());
            Car car = objectMapper.readValue(req.getInputStream(), Car.class);
            LOG.info("Received car: {}", car);
            carDao.create(car);
            EntityOperationLog newLog = new EntityOperationLog();
            newLog.setEntityId(car.getUuid());
            newLog.setOperation("create");
            newLog.setDate(Timestamp.from(new Date().toInstant()));
            newLog.setEntityName(Car.class.getSimpleName());
            logDao.create(newLog);
        } catch (DaoException | JsonParseException | JsonMappingException e) {
            LOG.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            LOG.info("{} {}", req.getMethod(), req.getRequestURI());
            UUID id = UUID.fromString(req.getParameter("id"));
            carDao.delete(id);
            EntityOperationLog newLog = new EntityOperationLog();
            newLog.setEntityId(id);
            newLog.setOperation("delete");
            newLog.setDate(Timestamp.from(new Date().toInstant()));
            newLog.setEntityName(Car.class.getSimpleName());
            logDao.create(newLog);
        } catch (DaoException e) {
            LOG.error(e.getMessage());
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            LOG.info("{} {}", req.getMethod(), req.getContextPath());
            Car car = objectMapper.readValue(req.getInputStream(), Car.class);
            LOG.info("Received car: {}", car);
            carDao.update(car);
            EntityOperationLog newLog = new EntityOperationLog();
            newLog.setEntityId(car.getUuid());
            newLog.setOperation("update");
            newLog.setDate(Timestamp.from(new Date().toInstant()));
            newLog.setEntityName(Car.class.getSimpleName());
            logDao.create(newLog);
        } catch (DaoException | JsonParseException | JsonMappingException e) {
            LOG.error(e.getMessage());
            if ("Not found!".equals(e.getMessage())) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
}

