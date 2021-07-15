package ru.job4j.todo.servlets;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.StoreHbm;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

public class GetAllItemsServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(GetAllItemsServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            User user = (User) req.getSession().getAttribute("user");

            List<Item> allItems = StoreHbm.instOf().getAllItems(user.getId());
            List<Category> allCategories = StoreHbm.instOf().getAllCategories();

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("user", user.getName());
            jsonResponse.put("items", new JSONArray(allItems));
            jsonResponse.put("categories", new JSONArray(allCategories));
            try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
                String out = jsonResponse.toString();
                writer.println(out);
                writer.flush();
            }
        } catch (final Exception e) {
            logger.error("Exception when getting items from database", e);
        }
    }
}
