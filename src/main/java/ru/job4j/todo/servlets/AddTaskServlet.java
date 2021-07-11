package ru.job4j.todo.servlets;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.StoreHbm;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class AddTaskServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(AddTaskServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String json = req.getParameter("item");
        JSONObject requestObject = new JSONObject(json);
        Item item = new Item(
                requestObject.getInt("id"),
                requestObject.getString("description"),
                requestObject.getBoolean("done")
        );
        User user = (User) req.getSession().getAttribute("user");
        item.setUser(user);
        try {
            Item itemFromDB = StoreHbm.instOf().add(item);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("item", new JSONObject(itemFromDB));
            try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
                String out = jsonResponse.toString();
                writer.println(out);
                writer.flush();
            }
        } catch (final Exception e) {
            logger.error("Exception when add item to database", e);
        }
    }
}
