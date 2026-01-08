package lnlearningsystem.ui;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import lnlearningsystem.*;
import lnlearningsystem.model.*;

public class ProfilePanel extends JPanel {

    private final AppFrame frame;

    private final JLabel usernameLabel = new JLabel();
    private final JLabel roleLabel = new JLabel();
    private JTable scoreTable;
    private final JTextArea levelArea = new JTextArea();

    public ProfilePanel(AppFrame frame) {
        this.frame = frame;

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    /* ---------- HEADER ---------- */
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(255, 223, 251));
        header.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel title = new JLabel("Profile & Progress");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.BLACK);

        header.add(title, BorderLayout.WEST);
        return header;
    }

    /* ---------- MAIN CONTENT ---------- */
    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setOpaque(false);

        main.add(createProfileCard(), BorderLayout.NORTH);
        main.add(createScoresCard(), BorderLayout.CENTER);

        return main;
    }

    /* ---------- PROFILE CARD ---------- */
    private JPanel createProfileCard() {
        JPanel card = createCard("User Information");

        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel content = new JPanel(new GridLayout(2, 1, 5, 5));
        content.setOpaque(false);
        content.add(usernameLabel);
        content.add(roleLabel);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    /* ---------- SCORES CARD ---------- */
    private JPanel createScoresCard() {
        JPanel card = createCard("Previous Scores");

        scoreTable = new JTable();
        scoreTable.setRowHeight(24);
        scoreTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        scoreTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(scoreTable);

        levelArea.setEditable(false);
        levelArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        levelArea.setBackground(new Color(250, 250, 250));
        levelArea.setBorder(new TitledBorder("Levels Completed"));

        JPanel bottom = new JPanel(new BorderLayout(10, 10));
        bottom.setOpaque(false);
        bottom.add(levelArea, BorderLayout.SOUTH);

        card.add(scroll, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    /* ---------- FOOTER ---------- */
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);

        JButton back = new JButton("Back");
        back.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        back.addActionListener(e -> frame.showMenu());

        footer.add(back);
        return footer;
    }

    /* ---------- CARD HELPER ---------- */
    private JPanel createCard(String title) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));

        card.add(label, BorderLayout.NORTH);
        return card;
    }

    /* ---------- DATA REFRESH ---------- */
    public void refresh() {
        if (frame.currentUser == null) return;

        User u = frame.currentUser;
        usernameLabel.setText("Username: " + u.username);
        roleLabel.setText("Role: " + u.role);

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Date", "Category", "Level", "Score"}, 0
        );

        for (UUID id : u.resultIds) {
            QuizResult r = DataStore.getResult(id);
            if (r == null) continue;

            model.addRow(new Object[]{
                    r.timestamp.toString(),
                    r.category,
                    r.level,
                    r.correct + "/" + r.totalQuestions
            });
        }

        scoreTable.setModel(model);

        Map<Level, Long> completed = u.resultIds.stream()
                .map(DataStore::getResult)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(r -> r.level, Collectors.counting()));

        StringBuilder sb = new StringBuilder();
        for (Level l : Level.values()) {
            sb.append(l).append(": ")
            .append(completed.getOrDefault(l, 0L))
            .append("\n");
        }

        levelArea.setText(sb.toString());
    }
}
