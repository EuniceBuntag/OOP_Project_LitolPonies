package lnlearningsystem.ui;

import java.awt.*;
import javax.swing.*;
import lnlearningsystem.AppFrame;
import lnlearningsystem.model.Level;

public class LevelPanel extends JPanel {

    private String category;

    private final JLabel titleLabel;
    private JComboBox<String> levelCombo;
    private final JButton startBtn;
    private final JButton backBtn;

    public LevelPanel(AppFrame frame) {

        /* ===== PANEL STYLE ===== */
        setLayout(new GridBagLayout());
        setBackground(new Color(255, 223, 251)); // same pink background

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(15, 15, 15, 15);
        c.gridx = 0;
        c.anchor = GridBagConstraints.CENTER;

        /* ===== TITLE ===== */
        titleLabel = new JLabel("Select Level");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        titleLabel.setForeground(Color.BLACK);

        c.gridy = 0;
        add(titleLabel, c);

        /* ===== LEVEL COMBO BOX ===== */
        levelCombo = new JComboBox<>(new String[]{
                "Beginner", "Intermediate", "Advanced"
        });
        levelCombo.setFont(new Font("Tahoma", Font.PLAIN, 18));
        levelCombo.setPreferredSize(new Dimension(260, 40));

        c.gridy = 1;
        add(levelCombo, c);

        /* ===== BUTTON PANEL ===== */
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        buttonPanel.setOpaque(false);

        backBtn = new JButton("Back");
        startBtn = new JButton("Start Quiz");

        styleButton(backBtn, new Color(200, 200, 200));
        styleButton(startBtn, new Color(180, 210, 240));

        buttonPanel.add(backBtn);
        buttonPanel.add(startBtn);

        c.gridy = 2;
        add(buttonPanel, c);

        /* ===== ACTION LISTENERS ===== */
        startBtn.addActionListener(e -> {
            String selected = (String) levelCombo.getSelectedItem();
            Level level;

            if (null == selected) {
                level = Level.BEGINNER;
            } else level = switch (selected) {
                case "Intermediate" -> Level.INTERMEDIATE;
                case "Advanced" -> Level.ADVANCED;
                default -> Level.BEGINNER;
            };

            frame.startQuiz(category, level);
        });

        backBtn.addActionListener(e -> frame.showCategory());
    }

    /* ===== BUTTON STYLE METHOD ===== */
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Tahoma", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(140, 40));
        button.setBackground(bgColor);
        button.setFocusPainted(false);
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
