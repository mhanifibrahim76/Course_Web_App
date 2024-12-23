package DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import pojo.Comments;
import pojo.StudyRoomUtil;
import java.util.List;

public class CommentsDAO {

    // Method untuk menyimpan review ke database
    public void saveComment(Comments comment) {
        Session session = StudyRoomUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(comment);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // Method untuk mengambil daftar review berdasarkan userId
    @SuppressWarnings("unchecked")
public List<Comments> getCommentsByUserId(int userId) {
    Session session = StudyRoomUtil.getSessionFactory().openSession();
    List<Comments> reviews = null;
    try {
        reviews = session.createQuery("FROM Comments WHERE users.id = :userId")
                .setParameter("userId", userId)
                .list();
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        session.close();
    }
    return reviews;
}


    public List<Comments> getAllComments() {
        Session session = StudyRoomUtil.getSessionFactory().openSession();
        List<Comments> allComments = null;
        try {
            allComments = session.createQuery("FROM Comments").list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return allComments;
    }

    // Method untuk mengupdate komentar di database
    public void updateComment(Comments comment) {
    Session session = StudyRoomUtil.getSessionFactory().openSession();
    Transaction tx = null;
    try {
        tx = session.beginTransaction();
        session.update(comment);
        tx.commit();
    } catch (Exception e) {
        if (tx != null) {
            tx.rollback();
        }
        e.printStackTrace();
    } finally {
        session.close();
    }
}


// Method untuk menghapus komentar dari database
    public void deleteComment(Comments comment) {
        Session session = StudyRoomUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(comment);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Comments getCommentById(int commentId) {
        Session session = StudyRoomUtil.getSessionFactory().openSession();
        Comments comment = null;
        try {
            comment = (Comments) session.get(Comments.class, commentId); // atau gunakan session.load
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return comment;
    }

    public boolean hasUserCommented(int userId) {
        Session session = StudyRoomUtil.getSessionFactory().openSession();
        try {
            Long count = (Long) session.createQuery("SELECT COUNT(*) FROM Comments WHERE Users.id = :userId ")
                    .setParameter("userId", userId)
                    .uniqueResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

}
