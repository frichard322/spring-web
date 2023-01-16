package edu.bbte.idde.frim1910.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.idde.frim1910.backend.dao.CarAdvertisementDao;
import edu.bbte.idde.frim1910.backend.dao.DaoFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/web/")
public class IndexServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(IndexServlet.class);
    private ObjectMapper objectMapper;
    private CarAdvertisementDao carAdvertisementDao;

    @Override
    public void init() throws ServletException {
        super.init();
        carAdvertisementDao = DaoFactory.getInstance().getCarAdvertisementDao();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession(false).getId();
        LOG.info("{} {} (Session: {})", req.getMethod(), req.getRequestURI(), sessionId);

        req.setAttribute("carAdvertisements", objectMapper.writeValueAsString(carAdvertisementDao.findAll()));

        req.getRequestDispatcher("../home.jsp").forward(req, resp);
    }
}
