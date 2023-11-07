package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import com.example.demo.exception.UserDaoException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserDao {
    private final SessionFactory sessionFactory;

    public UserDao() {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
    }

    public UserEntity save(UserEntity user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Serializable id = (Serializable) session.save(user);
            transaction.commit();
            return session.get(UserEntity.class, id);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new UserDaoException("Failed to save user: " + e.getMessage(), e);
        }
    }


    public Optional<UserEntity> findByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            UserEntity user = session.get(UserEntity.class, email);
            return Optional.ofNullable(user);
        }
    }

    public void deleteUser(UUID id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserEntity user = session.get(UserEntity.class, id);
        if (user != null) {
            session.delete(user);
        }
        transaction.commit();
        session.close();
    }

    public Optional<UserEntity> findById(UUID id) {
        try (Session session = sessionFactory.openSession()) {
            UserEntity user = session.get(UserEntity.class, id);
            return Optional.ofNullable(user);
        }
    }

    public List<UserEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM UserEntity ", UserEntity.class).list();
        }
    }
}
