package ru.job4j.todo.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "items")
public class Item {
    private long id;
    private String description;
    private Date created;
    private boolean done;
    private List<Category> categories = new ArrayList<>();
    private User user;

    public Item() {
    }

    public Item(long id, String description, boolean done) {
        this.id = id;
        this.description = description;
        this.done = done;
        this.created = new Date(System.currentTimeMillis());
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = new Date(created.getTime());
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", created=" + created +
                ", done=" + done +
                ", categories=" + categories +
                ", user=" + user +
                '}';
    }
}