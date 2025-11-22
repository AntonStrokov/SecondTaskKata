package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
	private static final String URL = "jdbc:mysql://localhost:3306/userstable?useSSL=false&serverTimezone=UTC";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "FynjyCnhjrjd8-8008";
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Не удалось загрузить драйвер MySQL", e);
		}
	}

	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			throw new RuntimeException("Ошибка подключения к базе данных", e);
		}
	}
}