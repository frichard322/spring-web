package edu.bbte.idde.frim1910.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/web/*")
public class Filter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpSession session = req.getSession();
        String loginURI = req.getContextPath() + "/web/login";
        String requestURI = req.getRequestURI();

        boolean loggedIn = session != null && session.getAttribute("username") != null;
        boolean loginRequest = requestURI.equals(loginURI);

        if (loggedIn || loginRequest) {
            chain.doFilter(req, resp);
        } else {
            resp.sendRedirect(loginURI);
        }
    }
}
