package edu.bbte.idde.frim1910.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/web/login")
public class LoginServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(LoginServlet.class);
    private String username;
    private String password;

    @Override
    public void init() {
        username = "admin";
        password = "password";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String sessionId = session.getId();
        String username = (String) session.getAttribute("username");
        LOG.info("{} {} (Session: {})", req.getMethod(), req.getRequestURI(), sessionId);
        if (username != null) {
            session.invalidate();
            LOG.info("{} has logged out! (Session: {})", username, sessionId);
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String sessionId = req.getSession(false).getId();
        LOG.info("{} {} (Session: {})", req.getMethod(), req.getRequestURI(), sessionId);

        if (username == null || username.isEmpty()) {
            req.setAttribute("usernameMessage", "Please enter username!");
        } else if (password == null || password.isEmpty()) {
            req.setAttribute("passwordMessage", "Please enter password!");
        } else {
            if (this.username.equals(username) && this.password.equals(password)) {
                req.getSession().setAttribute("username", username);
                resp.sendRedirect(req.getContextPath() + "/web/");
                LOG.info("{} has successfully logged in! (Session: {})", username, sessionId);
                return;
            } else {
                req.setAttribute("loginMessage", "Unknown login, please try again!");
            }
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}

