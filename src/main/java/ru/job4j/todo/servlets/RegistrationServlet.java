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

public class RegistrationServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(RegistrationServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            User user = new User(name, email, password);

            StoreHbm.instOf().save(user);
            req.getRequestDispatcher("./index.html").forward(req, resp);
        } catch (final Exception e) {
            logger.error("Exception when add user to database", e);
            req.getRequestDispatcher("./registration.html").forward(req, resp);
        }
    }
}
