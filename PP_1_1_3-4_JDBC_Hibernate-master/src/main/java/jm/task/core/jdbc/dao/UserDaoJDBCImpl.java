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
			System.out.println("Таблица users создана успешно");
		} catch (SQLException e) {
			System.out.println("Ошибка при создании таблицы: " + e.getMessage());
		}
	}

	@Override
	public void dropUsersTable() {
		String sql = "DROP TABLE IF EXISTS users";

		try (Connection connection = Util.getConnection();
		     Statement statement = connection.createStatement()) {
			statement.executeUpdate(sql);
			System.out.println("Таблица users удалена успешно");
		} catch (SQLException e) {
			System.out.println("Ошибка при удалении таблицы: " + e.getMessage());
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
			System.out.println("Пользователь " + name + " успешно добавлен"); // Отладочное сообщение
		} catch (SQLException e) {
			System.out.println("Ошибка при сохранении пользователя: " + e.getMessage());
			e.printStackTrace();
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
			System.out.println("Ошибка при удалении пользователя: " + e.getMessage());
		}
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		String sql = "SELECT id, name, last_name, age FROM users";

		System.out.println("Пытаемся получить пользователей из БД..."); // Отладочное сообщение

		try (Connection connection = Util.getConnection();
		     Statement statement = connection.createStatement();
		     ResultSet resultSet = statement.executeQuery(sql)) {

			System.out.println("Запрос выполнен, обрабатываем результаты..."); // Отладочное сообщение

			while (resultSet.next()) {
				User user = new User();
				user.setId(resultSet.getLong("id"));
				user.setName(resultSet.getString("name"));
				user.setLastName(resultSet.getString("last_name"));
				user.setAge(resultSet.getByte("age"));
				users.add(user);
				System.out.println("Добавлен пользователь: " + user); // Отладочное сообщение
			}

			System.out.println("Всего получено пользователей: " + users.size()); // Отладочное сообщение

		} catch (SQLException e) {
			System.out.println("Ошибка при получении пользователей: " + e.getMessage());
			e.printStackTrace();
			// В случае ошибки возвращаем пустой список вместо null
			return new ArrayList<>();
		}

		return users;
	}

	@Override
	public void cleanUsersTable() {
		String sql = "TRUNCATE TABLE users";

		try (Connection connection = Util.getConnection();
		     Statement statement = connection.createStatement()) {
			statement.executeUpdate(sql);
			System.out.println("Таблица users очищена успешно");
		} catch (SQLException e) {
			System.out.println("Ошибка при очистке таблицы: " + e.getMessage());
		}
	}
}