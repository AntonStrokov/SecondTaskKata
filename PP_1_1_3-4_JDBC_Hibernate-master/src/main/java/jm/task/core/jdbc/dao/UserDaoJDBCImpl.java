package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
public class UserDaoJDBCImpl implements UserDao {


	@Override
	public void createUsersTable() {
		String sql = "CREATE TABLE IF NOT EXISTS users (" +
				"id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
				"name VARCHAR(255), " +
				"last_name VARCHAR(255), " +
				"age TINYINT)";

		try (Connection connection = Util.getConnection();
		     Statement statement = connection.createStatement()) {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// Исключение обрабатывается в DAO, но не выводится
		}
	}

	@Override
	public void dropUsersTable() {
		String sql = "DROP TABLE IF EXISTS users";

		try (Connection connection = Util.getConnection();
		     Statement statement = connection.createStatement()) {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// Исключение обрабатывается в DAO, но не выводится
		}
	}

	@Override
	public void saveUser(String name, String lastName, byte age) {
		String sql = "INSERT INTO users (name, last_name, age) VALUES (?, ?, ?)";

		try (Connection connection = Util.getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, lastName);
			preparedStatement.setByte(3, age);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// Исключение обрабатывается в DAO, но не выводится
		}
	}

	@Override
	public void removeUserById(long id) {
		String sql = "DELETE FROM users WHERE id = ?";

		try (Connection connection = Util.getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setLong(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// Исключение обрабатывается в DAO, но не выводится
		}
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		String sql = "SELECT id, name, last_name, age FROM users";

		try (Connection connection = Util.getConnection();
		     Statement statement = connection.createStatement();
		     ResultSet resultSet = statement.executeQuery(sql)) {

			while (resultSet.next()) {
				User user = new User();
				user.setId(resultSet.getLong("id"));
				user.setName(resultSet.getString("name"));
				user.setLastName(resultSet.getString("last_name"));
				user.setAge(resultSet.getByte("age"));
				users.add(user);
			}
		} catch (SQLException e) {
			// Исключение обрабатывается в DAO, но не выводится
		}
		return users;
	}

	@Override
	public void cleanUsersTable() {
		String sql = "TRUNCATE TABLE users";

		try (Connection connection = Util.getConnection();
		     Statement statement = connection.createStatement()) {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// Исключение обрабатывается в DAO, но не выводится
		}
	}
}