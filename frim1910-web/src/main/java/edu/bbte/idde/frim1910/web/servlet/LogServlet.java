package edu.bbte.idde.frim1910.web.servlet;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.idde.frim1910.backend.dao.DaoException;
import edu.bbte.idde.frim1910.backend.dao.DaoFactory;
import edu.bbte.idde.frim1910.backend.dao.LogDao;
import edu.bbte.idde.frim1910.backend.model.EntityOperationLog;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/api/logs")
public class LogServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private LogDao logDao;

    @Override
    public void init() throws ServletException {
        super.init();
        logDao = DaoFactory.getInstance().getLogDao();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Content-Type", "application/json");
        try {
            String param = req.getParameter("id");
            if (param == null) {
                objectMapper.writeValue(resp.getOutputStream(), logDao.findAll());
            } else {
                UUID id = UUID.fromString(param);
                EntityOperationLog opLog = logDao.findByUuid(id);
                objectMapper.writeValue(resp.getOutputStream(), opLog);
            }
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (DaoException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            EntityOperationLog opLog = objectMapper.readValue(req.getInputStream(), EntityOperationLog.class);
            logDao.create(opLog);
        } catch (DaoException | JsonParseException | JsonMappingException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
