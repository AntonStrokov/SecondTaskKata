package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
	public static void main(String[] args) {
		UserService userService = new UserServiceImpl();

		// Создание таблицы User(ов)
		userService.createUsersTable();

		// Добавление 4 User(ов) в таблицу
		userService.saveUser("Иван", "Иванов", (byte) 20);
		System.out.println("User с именем – Иван добавлен в базу данных");

		userService.saveUser("Петр", "Петров", (byte) 25);
		System.out.println("User с именем – Петр добавлен в базу данных");

		userService.saveUser("Сидор", "Сидоров", (byte) 30);
		System.out.println("User с именем – Сидор добавлен в базу данных");

		userService.saveUser("Анна", "Каренина", (byte) 35);
		System.out.println("User с именем – Анна добавлен в базу данных");

		// Получение всех User из базы и вывод в консоль
		List<User> users = userService.getAllUsers();
		if (users != null) {
			for (User user : users) {
				System.out.println(user);
			}
		} else {
			System.out.println("Список пользователей равен null!");
		}

		// Очистка таблицы User(ов)
		userService.cleanUsersTable();

		// Удаление таблицы
		userService.dropUsersTable();
	}
}