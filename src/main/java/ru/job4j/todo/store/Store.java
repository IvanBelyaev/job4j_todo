package ru.job4j.todo.store;

import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.List;

public interface Store {
    Item add(Item item, List<Long> categoriesIds);
    void update(long id, Item item);
    void delete(long id);
    List<Item> getAllItems(long id);
    void save(User user);
    User getUserByName(String userName);
    List<Category> getAllCategories();
}
