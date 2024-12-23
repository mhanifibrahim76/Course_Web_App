/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pojo.StudyRoomUtil;
import pojo.Users;

/**
 *
 * @author mhanifibrahim7890
 */


public class DAOUser {
    public void saveUser(Users user) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = StudyRoomUtil.getSessionFactory().openSession();  // Buka session Hibernate
            transaction = session.beginTransaction();  // Mulai transaksi

            // Simpan objek Users
            session.save(user);

            // Commit transaksi
            transaction.commit();
        } catch (Exception e) {
            // Rollback transaksi jika terjadi error
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();  // Atau gunakan logger
        } finally {
            if (session != null) {
                session.close();  // Tutup session
            }
        }
    }
    
    public boolean isEmailRegistered(String email) {
    Transaction transaction = null;
    Users user = null;
    try {
        Session session = StudyRoomUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        Query query = session.createQuery("from Users where email= :email");
        query.setString("email", email);
        user = (Users) query.uniqueResult();
        transaction.commit();
    } catch (Exception e) {
        if (transaction != null) {
            transaction.rollback();
        }
        e.printStackTrace();
    }
    return user != null;
}
    
    public boolean isEmailExists(String email) {
    Session session = null;
    boolean exists = false;
    try {
        session = StudyRoomUtil.getSessionFactory().openSession();
        Query query = session.createQuery("from Users where email = :email");
        query.setString("email", email);
        exists = query.uniqueResult() != null;
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (session != null) {
            session.close();
        }
    }
    return exists;
}
    
    public Users findByUsername(String username) {
        Transaction transaction = null;
        Users user = null;
        Session session = StudyRoomUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("FROM Users WHERE username = :username");
            query.setParameter("username", username);
            user = (Users) query.uniqueResult();  // Explicit cast to User
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();  // Always close the session
        }
        return user;
    }

    

}
