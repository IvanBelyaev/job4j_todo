package ru.job4j.todo.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.StoreHbm;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(AuthorizationServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            String name = req.getParameter("name");
            String password = req.getParameter("password");
            User user = StoreHbm.instOf().getUserByName(name);
            if (user.getPassword().equals(password)) {
                req.getSession().setAttribute("user", user);
                req.getRequestDispatcher("./todo.html").forward(req, resp);
            }
        } catch (final Exception e) {
            logger.error("Exception when singing in", e);
        }
        req.getRequestDispatcher("./index.html").forward(req, resp);
    }
}
