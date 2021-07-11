package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class StoreHbm implements Store {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private static final class Lazy {
        private static final Store INST = new StoreHbm();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    private StoreHbm() {
    }

    @Override
    public Item add(Item item) {
        return this.txFunction(
                session -> {
                    session.save(item);
                    return item;
                }
        );
    }

    @Override
    public void update(long id, Item item) {
        this.txConsumer(
                session -> {
                    Item itemFromDB = session.getReference(Item.class, id);
                    itemFromDB.setDone(item.isDone());
                }
        );
    }

    @Override
    public void delete(long id) {
        this.txConsumer(
                session -> {
                    Item item = new Item();
                    item.setId(id);
                    session.delete(item);
                }
        );
    }

    @Override
    public List<Item> getAllItems(long id) {
        return this.txFunction(
                session -> {
                    Query itemsQuery =  session.createQuery("from Item i where i.user.id = :userId");
                    itemsQuery.setParameter("userId", id);
                    return itemsQuery.list();
                }
        );
    }

    @Override
    public void save(User user) {
        this.txConsumer(
                session -> session.save(user)
        );
    }

    @Override
    public User getUserByName(String userName) {
        return this.txFunction(
            session -> {
                Query userQuery = session.createQuery("from User where name = :username");
                userQuery.setParameter("username" , userName);
                User user = (User) userQuery.uniqueResult();
                if (user == null) {
                    throw new RuntimeException("There is no user named " + userName + " in the database");
                }
                return user;
            }
        );
    }

    private <T> T txFunction(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    private void txConsumer(Consumer<Session> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            command.accept(session);
            tx.commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
