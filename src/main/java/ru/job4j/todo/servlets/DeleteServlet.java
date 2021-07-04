package ru.job4j.todo.servlets;

import org.json.JSONObject;
import ru.job4j.todo.store.StoreHbm;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DeleteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = req.getParameter("item");
        JSONObject requestObject = new JSONObject(json);
        StoreHbm.instOf().delete(requestObject.getInt("id"));

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        JSONObject jsonResponse = new JSONObject();
        try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
            String out = jsonResponse.toString();
            writer.println(out);
            writer.flush();
        }
    }
}
