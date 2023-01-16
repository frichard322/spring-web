package edu.bbte.idde.frim1910.web.servlet;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.idde.frim1910.backend.dao.CarAdvertisementDao;
import edu.bbte.idde.frim1910.backend.dao.DaoException;
import edu.bbte.idde.frim1910.backend.dao.DaoFactory;
import edu.bbte.idde.frim1910.backend.dao.LogDao;
import edu.bbte.idde.frim1910.backend.model.CarAdvertisement;
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

@WebServlet("/api/carAdvertisements")
public class CarAdvertisementServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(CarAdvertisementServlet.class);
    private ObjectMapper objectMapper;
    private CarAdvertisementDao carAdvertisementDao;
    private LogDao logDao;

    @Override
    public void init() throws ServletException {
        super.init();
        carAdvertisementDao = DaoFactory.getInstance().getCarAdvertisementDao();
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
                objectMapper.writeValue(resp.getOutputStream(), carAdvertisementDao.findAll());
            } else {
                UUID id = UUID.fromString(param);
                CarAdvertisement carAdvertisement = carAdvertisementDao.findByUuid(id);
                objectMapper.writeValue(resp.getOutputStream(), carAdvertisement);
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
            CarAdvertisement carAdvertisement = objectMapper.readValue(req.getInputStream(), CarAdvertisement.class);
            LOG.info("Received car advertisement: {}", carAdvertisement);
            carAdvertisementDao.create(carAdvertisement);
            EntityOperationLog newLog = new EntityOperationLog();
            newLog.setEntityId(carAdvertisement.getUuid());
            newLog.setOperation("create");
            newLog.setDate(Timestamp.from(new Date().toInstant()));
            newLog.setEntityName(CarAdvertisement.class.getSimpleName());
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
            carAdvertisementDao.delete(id);
            EntityOperationLog newLog = new EntityOperationLog();
            newLog.setEntityId(id);
            newLog.setOperation("delete");
            newLog.setDate(Timestamp.from(new Date().toInstant()));
            newLog.setEntityName(CarAdvertisement.class.getSimpleName());
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
            CarAdvertisement carAdvertisement = objectMapper.readValue(req.getInputStream(), CarAdvertisement.class);
            LOG.info("Received car advertisement: {}", carAdvertisement);
            carAdvertisementDao.update(carAdvertisement);
            EntityOperationLog newLog = new EntityOperationLog();
            newLog.setEntityId(carAdvertisement.getUuid());
            newLog.setOperation("update");
            newLog.setDate(Timestamp.from(new Date().toInstant()));
            newLog.setEntityName(CarAdvertisement.class.getSimpleName());
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

