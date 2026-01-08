package lnlearningsystem.ui;

import java.awt.*;
import javax.swing.*;
import lnlearningsystem.AppFrame;

public class MainMenuPanel extends JPanel {

    private final AppFrame frame;
    private final JLabel welcomeLabel;

    public MainMenuPanel(AppFrame frame) {
        this.frame = frame;

        // ===== MAIN PANEL =====
        setLayout(new BorderLayout());
        setBackground(new Color(255, 223, 251)); // light pink

        // ===== TOP PANEL =====
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(new Color(255, 223, 251));

        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        top.add(welcomeLabel);

        add(top, BorderLayout.NORTH);

        // ===== CENTER PANEL (BUTTONS) =====
        // CHANGED: 3 rows only (Admin button removed)
        JPanel center = new JPanel(new GridLayout(3, 1, 20, 20));
        center.setBackground(new Color(255, 223, 251));

        Font buttonFont = new Font("Tahoma", Font.BOLD, 16);
        Dimension buttonSize = new Dimension(220, 55);

        JButton startQuizBtn = new JButton("Start Quiz");
        JButton leaderboardBtn = new JButton("Leaderboard");
        JButton profileBtn = new JButton("Profile");

        JButton[] buttons = {
            startQuizBtn, leaderboardBtn, profileBtn
        };

        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setPreferredSize(buttonSize);
            btn.setBackground(new Color(190, 220, 255)); // soft sky blue
            btn.setFocusPainted(false);
            center.add(btn);
        }

        // Wrapper to center buttons
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(new Color(255, 223, 251));
        centerWrapper.add(center);

        add(centerWrapper, BorderLayout.CENTER);

        // ===== BOTTOM PANEL =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(255, 223, 251));

        JButton logout = new JButton("Logout");
        logout.setFont(new Font("Tahoma", Font.BOLD, 14));
        logout.setPreferredSize(new Dimension(110, 35));
        logout.setBackground(new Color(220, 80, 80));
        logout.setForeground(Color.WHITE);
        logout.setFocusPainted(false);

        bottom.add(logout);
        add(bottom, BorderLayout.SOUTH);

        // ===== ACTION LISTENERS =====
        startQuizBtn.addActionListener(e -> frame.showCategory());
        leaderboardBtn.addActionListener(e -> frame.showLeaderboard());
        profileBtn.addActionListener(e -> frame.showProfile());

        logout.addActionListener(e -> {
            frame.currentUser = null;
            frame.showLogin();
        });
    }

    // ===== REFRESH METHOD =====
    public void refresh() {
        welcomeLabel.setText(
            "Welcome, " + frame.currentUser.username +
            " (" + frame.currentUser.role + ")"
        );
    }
}
