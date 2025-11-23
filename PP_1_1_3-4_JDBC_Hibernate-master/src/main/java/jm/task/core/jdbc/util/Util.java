package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
	private static final Properties properties = new Properties();
	private static SessionFactory sessionFactory;

	static {
		loadProperties();
		registerDriver();
	}

	private static void loadProperties() {
		try (var input = Util.class.getClassLoader().getResourceAsStream("application.properties")) {
			if (input == null) {
				throw new RuntimeException("Не найден файл application.properties");
			}
			properties.load(input);
		} catch (Exception e) {
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

	// Метод для JDBC
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

	// Метод для Hibernate
	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				Configuration configuration = new Configuration();

				// Hibernate settings
				Properties hibernateProps = new Properties();
				hibernateProps.put(Environment.DRIVER, properties.getProperty("db.driver"));
				hibernateProps.put(Environment.URL, properties.getProperty("db.url"));
				hibernateProps.put(Environment.USER, properties.getProperty("db.username"));
				hibernateProps.put(Environment.PASS, properties.getProperty("db.password"));
				hibernateProps.put(Environment.DIALECT, properties.getProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect"));
				hibernateProps.put(Environment.SHOW_SQL, properties.getProperty("hibernate.show_sql", "true"));
				hibernateProps.put(Environment.HBM2DDL_AUTO, properties.getProperty("hibernate.hbm2ddl.auto", "none"));

				configuration.setProperties(hibernateProps);
				configuration.addAnnotatedClass(User.class);

				ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties()).build();

				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Ошибка инициализации Hibernate", e);
			}
		}
		return sessionFactory;
	}
}