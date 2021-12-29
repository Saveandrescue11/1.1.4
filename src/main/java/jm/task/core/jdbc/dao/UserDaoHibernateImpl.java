package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jm.task.core.jdbc.util.Util;


public class UserDaoHibernateImpl implements UserDao {
    Session session = Util.getSessionFactory().openSession();
    Transaction transaction = session.beginTransaction();

    public UserDaoHibernateImpl() {
    }


    @Override
    public void createUsersTable() {
        try {
            Query query = session.createSQLQuery(("CREATE TABLE IF NOT EXISTS Users " +
                    "(id INTEGER not NULL AUTO_INCREMENT, " +
                    " name VARCHAR(255), " +
                    " lastName VARCHAR(255), " +
                    " age INTEGER, " +
                    " PRIMARY KEY (id))"));
            query.executeUpdate();
            transaction.commit();
        } catch (
                Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    @Override
    public void dropUsersTable() {
        try {
            Query query = session.createSQLQuery("DROP TABLE users");
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            User user = new User(name, lastName, age);
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            Query query = session.createSQLQuery("DELETE FOM users WHERE id");
            query.setParameter("id", id);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> usersList = session.createSQLQuery("From Users").list();
        return usersList;
    }

    @Override
    public void cleanUsersTable() {
        try {
            Query query = session.createSQLQuery("DELETE FROM Users");
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }
}
