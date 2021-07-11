package ru.job4j.todo.store;

import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.List;

public interface Store {
    Item add(Item item);
    void update(long id, Item item);
    void delete(long id);
    List<Item> getAllItems(long id);
    void save(User user);
    User getUserByName(String userName);
}
