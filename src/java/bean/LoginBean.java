package bean;

import DAO.DAOLogin;
import DAO.DAOUser;
import pojo.Users;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private Users loggedInUser;
    private DAOUser daouser = new DAOUser();

    public LoginBean() {
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Users getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(Users loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    // Method for validating login
    public String validasiLogin() {
        Users user = daouser.findByUsername(username);
        DAOLogin uDao = new DAOLogin();
        List<Users> us = uDao.getBy(username, password);

        if (us != null && !us.isEmpty()) {
            // Set session for logged-in user
            loggedInUser = us.get(0);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", loggedInUser);
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                    .getExternalContext().getSession(true);
            session.setAttribute("user", user);

            return "index?faces-redirect=true";  // Redirect to home page
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Login gagal", "Username atau password salah.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;  // Stay on the login page
        }
    }

    // Method for registering a new user
    public String register() {
        if (!password.equals(confirmPassword)) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Kesalahan", "Password dan Konfirmasi Password harus sama.");
            FacesContext.getCurrentInstance().addMessage("register-form:confirm-password", message);
            return null;  // Stay on the registration page
        }

        DAOUser daoUser = new DAOUser();
        if (daoUser.isEmailExists(email)) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Kesalahan", "Email sudah terdaftar.");
            FacesContext.getCurrentInstance().addMessage("register-form:email", message);
            return null;
        }

        Users newUser = new Users(username, email, password, new Date());
        daoUser.saveUser(newUser);

        FacesMessage successMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Registrasi Berhasil", "Silakan login dengan akun baru Anda.");
        FacesContext.getCurrentInstance().addMessage(null, successMessage);

        return "login?faces-redirect=true";  // Redirect to login page after successful registration
    }

    // Method for logging out
    public String logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();  // Invalidate the session to log out
        }

        return "login?faces-redirect=true";  // Redirect to login page after logging out
    }
}
