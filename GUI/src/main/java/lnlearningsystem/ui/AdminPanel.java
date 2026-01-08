package lnlearningsystem.ui;

import java.awt.*;
import javax.swing.*;
import lnlearningsystem.AppFrame;
import lnlearningsystem.DataStore;
import lnlearningsystem.model.Level;
import lnlearningsystem.model.Question;

public class AdminPanel extends JPanel {

    private final AppFrame frame;

    public AdminPanel(AppFrame frame) {
        this.frame = frame;

        // ===== MAIN PANEL =====
        setLayout(new BorderLayout());
        setBackground(new Color(255, 223, 251)); // light pink

        // ===== CENTER PANEL =====
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(255, 223, 251));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(15, 15, 15, 15);
        c.gridx = 0;

        // ===== TITLE =====
        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(new Font("Tahoma", Font.BOLD, 22));
        c.gridy = 0;
        center.add(title, c);

        // ===== BUTTON STYLE (same as Main Menu) =====
        Font buttonFont = new Font("Tahoma", Font.BOLD, 16);
        Dimension buttonSize = new Dimension(220, 55);
        Color buttonColor = new Color(190, 220, 255); // soft sky blue

        JButton manageQBtn = new JButton("Manage Questions");
        JButton reportsBtn = new JButton("Reports");

        JButton[] buttons = { manageQBtn, reportsBtn };

        c.insets = new Insets(20, 15, 20, 15);
        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setPreferredSize(buttonSize);
            btn.setBackground(buttonColor);
            btn.setFocusPainted(false);
            c.gridy++;
            center.add(btn, c);
        }

        add(center, BorderLayout.CENTER);

        // ===== BOTTOM PANEL (LOGOUT) =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(255, 223, 251));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
        logoutBtn.setPreferredSize(new Dimension(110, 35));
        logoutBtn.setBackground(new Color(220, 80, 80)); // red
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);

        bottom.add(logoutBtn);
        add(bottom, BorderLayout.SOUTH);

        // ===== ACTION LISTENERS =====
        manageQBtn.addActionListener(e -> frame.showQuestionsManager());
        reportsBtn.addActionListener(e -> frame.showReports());

        logoutBtn.addActionListener(e -> {
            frame.currentUser = null;
            frame.showLogin();
        });
    }

    // New method to add a question
    public void addQuestionToQuiz(String category, Level level, String questionText, String[] options, int correctIndex) {
        Question newQuestion = new Question(category, level, questionText, options[0], options[1], options[2], options[3], correctIndex);

        // Add the question to DataStore
        DataStore.get().addQuestion(newQuestion);

        // Now refresh the quiz panel
        ((QuizPanel) frame.getQuizPanel()).refreshQuiz();  // This triggers the QuizPanel to reload the updated question list
    }

    public void refresh() {
    // Nothing to refresh for now
}

}


