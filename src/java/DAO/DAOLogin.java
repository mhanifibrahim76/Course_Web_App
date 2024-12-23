/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

/**
 *
 * @author mhanifibrahim7890
 */


import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pojo.StudyRoomUtil;
import pojo.Users;

public class DAOLogin {
    public List<Users> getBy (String uName, String uPass) {
        Users u = new Users();
        List<Users> user = new ArrayList();
        
        Transaction trans = null;
        Session session = StudyRoomUtil.getSessionFactory().openSession();
        try{
            trans = session.beginTransaction();
            Query query = session.createQuery("from Users where username= :uName AND password= :uPass");
            query.setString("uName", uName);
            query.setString("uPass", uPass);
            u = (Users) query.uniqueResult();
            user = query.list();
            
            trans.commit();
        } catch (Exception e) {
            System.out.println("error DAOLOGIN: " + e);
        }
        return user;
    }
}
