package ru.job4j.todo.servlets;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.store.StoreHbm;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GetAllItemsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        List<Item> allItems = StoreHbm.instOf().getAllItems();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("items", new JSONArray(allItems));
        try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
            String out = jsonResponse.toString();
            writer.println(out);
            writer.flush();
        }
    }
}
