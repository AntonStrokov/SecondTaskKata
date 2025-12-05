package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import java.util.List;

@Slf4j
public class UserDaoHibernateImpl implements UserDao {

	public UserDaoHibernateImpl() {
	}

	@Override
	public void createUsersTable() {
		String sql = "CREATE TABLE IF NOT EXISTS users (" +
				"id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
				"name VARCHAR(255), " +
				"last_name VARCHAR(255), " +
				"age TINYINT)";

		Transaction transaction = null;
		try (Session session = Util.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			NativeQuery<?> query = session.createNativeQuery(sql);
			query.executeUpdate();
			transaction.commit();
			log.info("Таблица users успешно создана");
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			log.error("Ошибка при создании таблицы users", e);
			throw new RuntimeException("Не удалось создать таблицу users", e);
		}
	}

	@Override
	public void dropUsersTable() {
		String sql = "DROP TABLE IF EXISTS users";

		Transaction transaction = null;
		try (Session session = Util.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			NativeQuery<?> query = session.createNativeQuery(sql);
			query.executeUpdate();
			transaction.commit();
			log.info("Таблица users успешно удалена");
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			log.error("Ошибка при удалении таблицы users", e);
			throw new RuntimeException("Не удалось удалить таблицу users", e);
		}
	}

	@Override
	public void saveUser(String name, String lastName, byte age) {
		Transaction transaction = null;
		try (Session session = Util.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			User user = new User(name, lastName, age);
			session.save(user);
			transaction.commit();
			log.info("Пользователь {} {} добавлен в базу", name, lastName);
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			log.error("Ошибка при сохранении пользователя {} {}", name, lastName, e);
			throw new RuntimeException("Не удалось сохранить пользователя", e);
		}
	}

	@Override
	public void removeUserById(long id) {
		Transaction transaction = null;
		try (Session session = Util.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			User user = session.get(User.class, id);

			if (user == null) {
				log.warn("Пользователь с id {} не найден для удаления", id);
				throw new RuntimeException("Пользователь не найден: id=" + id);
			}

			session.delete(user);
			transaction.commit();
			log.info("Пользователь с id {} успешно удалён", id);
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			log.error("Ошибка при удалении пользователя с id {}", id, e);
			throw new RuntimeException("Не удалось удалить пользователя: id=" + id, e);
		}
	}

	@Override
	public List<User> getAllUsers() {
		try (Session session = Util.getSessionFactory().openSession()) {
			List<User> users = session.createQuery("from User", User.class).list();
			log.info("Получено {} пользователей", users.size());
			return users;
		} catch (Exception e) {
			log.error("Ошибка при получении списка пользователей", e);
			throw new RuntimeException("Не удалось получить список пользователей", e);
		}
	}

	@Override
	public void cleanUsersTable() {
		String sql = "TRUNCATE TABLE users";

		Transaction transaction = null;
		try (Session session = Util.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			NativeQuery<?> query = session.createNativeQuery(sql);
			query.executeUpdate();
			transaction.commit();
			log.info("Таблица users успешно очищена");
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			log.error("Ошибка при очистке таблицы users", e);
			throw new RuntimeException("Не удалось очистить таблицу users", e);
		}
	}
}
