package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    Util util = new Util();
    Connection connect = util.connection();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Statement statement = connect.createStatement()) {
            connect.setAutoCommit(false);
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS test (id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(45) NOT NULL, " +
                    "lastName VARCHAR(45) NOT NULL, " +
                    "age INT NOT NULL)");
            connect.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connect.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    connect.setAutoCommit(true);
                } catch (SQLException throwables){
                    throwables.printStackTrace();
                }
            }
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connect.createStatement()) {
            connect.setAutoCommit(false);
            statement.executeUpdate("DROP TABLE IF EXISTS test");
            connect.commit();
        } catch (SQLException e) {
            try {
                connect.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connect.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement statement = connect.prepareStatement("INSERT INTO test (name, lastName, age) VALUES(?, ?, ?)")) {
            connect.setAutoCommit(false);
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
            System.out.println("Пользователь " + name + " добавлен в таблицу");
            connect.commit();
        } catch (SQLException throwables) {
            try {
                connect.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        } finally {
            try {
                connect.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement statement = connect.prepareStatement("DELETE FROM test WHERE id = ?")) {
            connect.setAutoCommit(false);
            statement.setLong(1, id);
            statement.executeUpdate();
            connect.commit();
        } catch (SQLException e) {
            try {
                connect.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connect.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> results = new ArrayList<>();
        //querry = "SELECT * FROM test";

        try (PreparedStatement statement = connect.prepareStatement("SELECT * FROM test")) {
            connect.setAutoCommit(false);
            ResultSet rs = statement.executeQuery("SELECT * FROM test");
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong(1));
                user.setName(rs.getString(2));
                user.setLastName(rs.getString(3));
                user.setAge(rs.getByte(4));
                System.out.println(user);
                results.add(user);
            }
            connect.commit();
        } catch (SQLException e) {
            try {
                connect.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connect.setAutoCommit(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return results;
    }

    public void cleanUsersTable() {
        try (Statement statement = connect.createStatement()) {
            connect.setAutoCommit(false);
            statement.executeUpdate("TRUNCATE TABLE test");
            connect.commit();
        } catch (SQLException e) {
            try {
                connect.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connect.setAutoCommit(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
}
