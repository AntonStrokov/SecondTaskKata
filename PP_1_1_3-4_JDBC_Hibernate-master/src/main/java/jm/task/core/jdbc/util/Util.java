package jm.task.core.jdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
	private static final Properties properties = new Properties();

	static {
		loadProperties();
		registerDriver();
	}

	private static void loadProperties() {
		try (InputStream input = Util.class.getClassLoader().getResourceAsStream("application.properties")) {
			if (input == null) {
				throw new RuntimeException("Не найден файл application.properties");
			}
			properties.load(input);
		} catch (IOException e) {
			throw new RuntimeException("Ошибка загрузки application.properties", e);
		}
	}

	private static void registerDriver() {
		try {
			Class.forName(properties.getProperty("db.driver"));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Не удалось загрузить драйвер MySQL", e);
		}
	}

	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(
					properties.getProperty("db.url"),
					properties.getProperty("db.username"),
					properties.getProperty("db.password")
			);
		} catch (SQLException e) {
			throw new RuntimeException("Ошибка подключения к базе данных", e);
		}
	}
}