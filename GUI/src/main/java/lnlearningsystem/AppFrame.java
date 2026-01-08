package lnlearningsystem;

import java.awt.*;
import javax.swing.*; 
import lnlearningsystem.model.*;
import lnlearningsystem.ui.*; 

public final class AppFrame extends JFrame {
    CardLayout cards = new CardLayout();
    JPanel mainPanel = new JPanel(cards);

    LoginPanel loginPanel;
    RegistrationPanel registrationPanel;
    MainMenuPanel mainMenuPanel;
    CategoryPanel categoryPanel;
    LevelPanel levelPanel;
    QuizPanel quizPanel;
    ResultsPanel resultsPanel;
    ProfilePanel profilePanel;
    LeaderboardPanel leaderboardPanel;
    AdminPanel adminPanel;
    QuestionManagementPanel questionManagementPanel;
    ReportsPanel reportsPanel;

    public User currentUser;

    public AppFrame() {
        setTitle("EduQuiz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

    System.out.println("AppFrame created: " + this);
        loginPanel = new LoginPanel(this);
        registrationPanel = new RegistrationPanel(this);
        mainMenuPanel = new MainMenuPanel(this);
        categoryPanel = new CategoryPanel(this);
        levelPanel = new LevelPanel(this);
        quizPanel = new QuizPanel(this);
        resultsPanel = new ResultsPanel(this);
        profilePanel = new ProfilePanel(this);
        leaderboardPanel = new LeaderboardPanel(this);
        adminPanel = new AdminPanel(this);
        questionManagementPanel = new QuestionManagementPanel(this);
        reportsPanel = new ReportsPanel(this);

        // Add panels to the main panel (CardLayout)
        mainPanel.add(loginPanel, "login");
        mainPanel.add(registrationPanel, "register");
        mainPanel.add(mainMenuPanel, "menu");
        mainPanel.add(categoryPanel, "category");
        mainPanel.add(levelPanel, "level");
        mainPanel.add(quizPanel, "quiz");
        mainPanel.add(resultsPanel, "results");
        mainPanel.add(profilePanel, "profile");
        mainPanel.add(leaderboardPanel, "leaderboard");
        mainPanel.add(adminPanel, "admin");
        mainPanel.add(questionManagementPanel, "questions");
        mainPanel.add(reportsPanel, "reports");

        add(mainPanel);
        // Initially show the login panel
        showLogin();
    }

    // Panel switching methods
    public void showLogin() { 
        cards.show(mainPanel, "login"); 
    }

    public void showRegister() { 
        cards.show(mainPanel, "register"); 
    }

    public void showMenu() {
        mainMenuPanel.refresh();
        cards.show(mainPanel, "menu");
    }

    public void showCategory() {
        categoryPanel.refresh();
        cards.show(mainPanel, "category");
    }

    public void showLevel(String category) {
        levelPanel.setCategory(category);
        cards.show(mainPanel, "level");
    }

    public void startQuiz(String category, Level level) {
        quizPanel.startQuiz(category, level);
        cards.show(mainPanel, "quiz");
    }

    public void showResults(QuizResult result) {
        resultsPanel.setResult(result);
        cards.show(mainPanel, "results");
    }

    public void showProfile() {
        profilePanel.refresh();
        cards.show(mainPanel, "profile");
    }

/*  public void showLeaderboard() {
        leaderboardPanel.refresh();
        cards.show(mainPanel, "leaderboard");
    }
*/
    public void showAdmin() {
        cards.show(mainPanel, "admin");
    }

    public void showQuestionsManager() {
        questionManagementPanel.refresh();
        cards.show(mainPanel, "questions");
    }

    public void showReports() {
    System.out.println("Switching to Reports Panel...");
    reportsPanel.refresh();
    cards.show(mainPanel, "reports");
}

    public QuizPanel getQuizPanel() {
    return quizPanel;
}
public void showLeaderboard() {
    leaderboardPanel.refresh();      // Refresh the table
    cards.show(mainPanel, "leaderboard");
}

}
