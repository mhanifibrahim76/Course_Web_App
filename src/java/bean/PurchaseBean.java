package bean;

import DAO.DAOPurchase;
import pojo.Courses;
import pojo.PurchasedCourse;
import pojo.Users;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "purchaseBean")
@SessionScoped
public class PurchaseBean {

    private DAOPurchase DAOPurchase = new DAOPurchase();
    private String selectedCourseId;  // Add this property

    // Getter and Setter for selectedCourseId
    public String getSelectedCourseId() {
        return selectedCourseId;
    }

    public void setSelectedCourseId(String selectedCourseId) {
        this.selectedCourseId = selectedCourseId;
    }

    public boolean hasPurchasedCourse(int courseId) {
        // Retrieve the logged-in user from the session
        Users loggedUser = (Users) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

        if (loggedUser == null) {
            return false;  // User is not logged in
        }

        // Get list of purchased courses for this user
        List<PurchasedCourse> purchasedCourses = DAOPurchase.getPurchasedCoursesByUser(loggedUser.getId());

        // Check if the user has purchased the specific course
        for (PurchasedCourse purchasedCourse : purchasedCourses) {
            if (purchasedCourse.getCourses().getCourseId() == courseId) {
                return true;  // User has already purchased this course
            }
        }

        return false;  // User has not purchased this course
    }

    // Method to handle the purchase of the course
    // Method to handle the purchase of the course
    public String buyCourse(int courseId) {
        // Retrieve the logged-in user from the session
        Users loggedUser = (Users) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

        if (loggedUser == null) {
            // Redirect to login page if the user is not logged in
            return "login.xhtml?faces-redirect=true";
        }

        // Fetch the course details using the course ID
        Courses course = DAOPurchase.getCourseById(courseId);

        // Create a new PurchasedCourse entity
        PurchasedCourse purchasedCourse = new PurchasedCourse();
        purchasedCourse.setCourses(course);
        purchasedCourse.setUsers(loggedUser); // Use the logged-in user
        purchasedCourse.setPurchaseDate(new Date());

        // Save the purchase record in the database
        DAOPurchase.savePurchasedCourse(purchasedCourse);

        // Redirect to the course-specific content page
        switch (courseId) {
            case 1:
                return "materi-front-end.xhtml?faces-redirect=true";
            case 2:
                return "materi-back-end.xhtml?faces-redirect=true";
            case 3:
                return "materi-frame-work.xhtml?faces-redirect=true";
            default:
                return "courses.xhtml";
        }
    }
}
