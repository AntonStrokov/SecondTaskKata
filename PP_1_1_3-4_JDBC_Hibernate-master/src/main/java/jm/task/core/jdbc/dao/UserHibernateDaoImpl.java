package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import java.util.List;

public class UserHibernateDaoImpl implements UserDao {

	public UserHibernateDaoImpl() {
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
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
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
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
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
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}

	@Override
	public void removeUserById(long id) {
		Transaction transaction = null;
		try (Session session = Util.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			User user = session.get(User.class, id);
			if (user != null) {
				session.delete(user);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}

	@Override
	public List<User> getAllUsers() {
		try (Session session = Util.getSessionFactory().openSession()) {
			return session.createQuery("from User", User.class).list();
		} catch (Exception e) {
			return List.of();
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
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}
}