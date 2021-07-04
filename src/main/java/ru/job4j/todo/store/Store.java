package ru.job4j.todo.store;

import ru.job4j.todo.model.Item;

import java.util.List;

public interface Store {
    Item add(Item item);
    void update(long id, Item item);
    void delete(long id);
    List<Item> getAllItems();
}
