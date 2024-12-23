package bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

@ManagedBean
@Named("quizBean")

public class QuizBean implements Serializable {
    private String q1;
    private String q2;
    private String q3;
    private String q4;
    private String q5;
    private String results;

    // Getters and Setters
    public String getQ1() { return q1; }
    public void setQ1(String q1) { this.q1 = q1; }

    public String getQ2() { return q2; }
    public void setQ2(String q2) { this.q2 = q2; }

    public String getQ3() { return q3; }
    public void setQ3(String q3) { this.q3 = q3; }

    public String getQ4() { return q4; }
    public void setQ4(String q4) { this.q4 = q4; }

    public String getQ5() { return q5; }
    public void setQ5(String q5) { this.q5 = q5; }

    public String getResults() { return results; }

    // Method to check the quiz answers
    public String checkAnswers() {
        int score = 0;

        // Define correct answers
        if ("A".equals(q1)) { score++; }
        if ("D".equals(q2)) { score++; }
        if ("B".equals(q3)) { score++; }
        if ("A".equals(q4)) { score++; }
        if ("D".equals(q5)) { score++; }

        // Set the result based on the score
        results = "You scored " + score + "/5.";

        return null; // Stay on the same page
    }
}
