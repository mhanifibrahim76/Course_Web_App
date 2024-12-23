package pojo;
// Generated Sep 26, 2024 8:20:19 PM by Hibernate Tools 4.3.1

import DAO.DAOLogin;
import DAO.DAOUser;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped

public class Users implements java.io.Serializable {

    private Integer id;
    private String username;
    private String email;
    private String password;
    private Date createdAt;
    private Set progresses = new HashSet(0);
    private Set commentses = new HashSet(0);
    private Set purchasedCourses = new HashSet(0);
    private String confirmPassword;

    public Users() {
    }

    public Users(String username, String email, String password, Date createdAt) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    public Users(String username, String email, String password, Date createdAt, Set progresses, Set commentses, Set purchasedCourses) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.progresses = progresses;
        this.commentses = commentses;
        this.purchasedCourses = purchasedCourses;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Set getProgresses() {
        return this.progresses;
    }

    public void setProgresses(Set progresses) {
        this.progresses = progresses;
    }

    public Set getCommentses() {
        return this.commentses;
    }

    public void setCommentses(Set commentses) {
        this.commentses = commentses;
    }

    public Set getPurchasedCourses() {
        return this.purchasedCourses;
    }

    public void setPurchasedCourses(Set purchasedCourses) {
        this.purchasedCourses = purchasedCourses;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    

}
