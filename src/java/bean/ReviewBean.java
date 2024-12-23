package bean;

import DAO.CommentsDAO;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import pojo.Comments;
import pojo.Users;

import java.util.List;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;

@ManagedBean
@ViewScoped
public class ReviewBean {

    private String comment;
    private int rating;
    private String username;
    private int userId;  // Menyimpan userId
    private List<Comments> reviews;
    private List<Comments> allComments;
    private CommentsDAO commentsDAO;
    private int commentId;
    

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean sessionController;

    public LoginBean getSessionController() {
        return sessionController;
    }

    public void setSessionController(LoginBean sessionController) {
        this.sessionController = sessionController;
    }

    public ReviewBean() {
        commentsDAO = new CommentsDAO();
    }

    // Getter dan Setter
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public List<Comments> getReviews() {
        Users loggedInUser = sessionController.getLoggedInUser();
        if (loggedInUser != null) {
            return commentsDAO.getCommentsByUserId(loggedInUser.getId());
        } else {
            return null;  // Jika tidak ada user yang login, kembalikan null atau kosong
        }
    }

    public List<Comments> getAllComments() {
        return allComments;
    }

    // Method untuk submit review
    public void submitReview() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);

        Integer userId = (Integer) httpSession.getAttribute("userId");
        if (userId == null) {
            throw new IllegalStateException("User is not logged in!");
        }

        // Buat objek comment dan simpan
        Comments newComment = new Comments();
        newComment.setComment(comment);
        newComment.setRating(rating);
        newComment.setUsername(username);  // Atau ambil dari session jika username sudah disimpan di session
        newComment.setCreatedAt(new Date());

        // Set user yang terkait dengan review
        Users user = new Users();
        user.setId(userId);
        newComment.setUsers(user);

        // Simpan review
        commentsDAO.saveComment(newComment);

        // Reset form setelah submit
        comment = "";
        rating = 0;

        // Muat ulang review
        loadReviews();
    }

    public void addComment() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    Users user = sessionController.getLoggedInUser(); // Ambil user yang sedang login

    // Jika user belum login, redirect ke halaman login dan tampilkan pesan
    if (user == null) {
        try {
            // Tambahkan pesan pemberitahuan bahwa user harus login
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "You need to log in to add a comment.", null));
            
            // Redirect ke halaman login
            facesContext.getExternalContext().redirect("login.xhtml?faces-redirect=true");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return; // Hentikan eksekusi jika user belum login
    }

    // Validasi komentar dan rating
    if (comment == null || comment.isEmpty()) {
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Comment cannot be empty!", null));
        return;
    }

    if (rating < 1 || rating > 5) {
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Rating must be between 1 and 5!", null));
        return;
    }

    // Buat objek komentar baru
    Comments newComment = new Comments();
    newComment.setComment(comment);
    newComment.setRating(rating);
    newComment.setUsername(sessionController.getUsername()); // Gunakan username dari session
    newComment.setCreatedAt(new Date());

    // Hubungkan komentar dengan user yang login
    newComment.setUsers(user);

    // Simpan komentar ke database
    commentsDAO.saveComment(newComment);

    // Reset form setelah submit
    comment = "";
    rating = 0;

    // Muat ulang komentar setelah menambah
    loadReviews();
}


    // Method to load reviews from the database based on userId
    @PostConstruct
    public void loadReviews() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Integer userId = (Integer) facesContext.getExternalContext().getSessionMap().get("userId");
        if (userId != null) {
            reviews = commentsDAO.getCommentsByUserId(userId);
        }

        // Load all comments
        loadAllComments();
    }

    // Method to load all comments
    public void loadAllComments() {
        allComments = commentsDAO.getAllComments();
    }

    public void editComment(Comments comment) {
    this.commentId = comment.getCommentId(); // Set commentId dari komentar yang sedang diedit
    this.comment = comment.getComment();     // Set nilai komentar untuk ditampilkan di form
    this.rating = comment.getRating();       // Set nilai rating untuk ditampilkan di form
}


    public void updateComment() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Users user = sessionController.getLoggedInUser(); // Ambil user yang sedang login

        if (user == null) {
            throw new IllegalStateException("User is not logged in!");
        }

        // Ambil komentar yang sedang diedit menggunakan commentId
        Comments updatedComment = commentsDAO.getCommentById(this.commentId);

        if (updatedComment != null && updatedComment.getUsers().getId() == user.getId()) {
            // Perbarui nilai komentar dan rating
            updatedComment.setComment(this.comment);
            updatedComment.setRating(this.rating);
            updatedComment.setCreatedAt(new Date()); // Menyimpan tanggal update

            // Simpan perubahan ke database
            commentsDAO.updateComment(updatedComment);

            // Reset form setelah update
            this.comment = "";
            this.rating = 0;
            this.commentId = 0; // Reset commentId setelah update

            // Berikan pesan sukses kepada user
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Comment updated successfully!", null));
        } else {
            // Jika komentar tidak ditemukan atau user tidak memiliki hak untuk mengubahnya
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to update comment!", null));
        }

        // Muat ulang komentar
        loadReviews();
    }

    public void deleteComment(Comments comment) {
        commentsDAO.deleteComment(comment);

        // Reload comments after deletion
        loadReviews();
    }

    public boolean hasUserCommented(int userId) {
        for (Comments comment : allComments) {
            if (comment.getUsers().getId() == userId) {
                return true;
            }
        }
        return false;
    }

    public void loadUserComments() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Users loggedInUser = sessionController.getLoggedInUser();

        if (loggedInUser != null) {
            // Ambil komentar dari user yang sedang login
            reviews = commentsDAO.getCommentsByUserId(loggedInUser.getId());
        } else {
            reviews = null; // Jika tidak ada user yang login, set reviews menjadi null atau kosong
        }
    }

}
