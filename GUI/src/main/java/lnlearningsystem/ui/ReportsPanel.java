package lnlearningsystem.ui;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import lnlearningsystem.AppFrame;
import lnlearningsystem.DataStore;
import lnlearningsystem.model.QuizResult;

public class ReportsPanel extends JPanel {

    private final JTable table;
    private final ReportsTableModel model;

    private final JLabel usersLabel = new JLabel();
    private final JLabel attemptsLabel = new JLabel();
    private final JLabel accuracyLabel = new JLabel();
    private final JLabel bestCategoryLabel = new JLabel();

    public ReportsPanel(AppFrame frame) {

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(255, 223, 251));

        // ===== TITLE =====
        JLabel title = new JLabel("Reports Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // ===== SUMMARY PANEL =====
        JPanel summary = new JPanel(new GridLayout(2, 2, 20, 10));
        summary.setBackground(new Color(255, 223, 251));
        summary.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        setBold(usersLabel);
        setBold(attemptsLabel);
        setBold(accuracyLabel);
        setBold(bestCategoryLabel);

        summary.add(usersLabel);
        summary.add(attemptsLabel);
        summary.add(accuracyLabel);
        summary.add(bestCategoryLabel);

        add(summary, BorderLayout.SOUTH);

        // ===== TABLE =====
        model = new ReportsTableModel();
        model.reload();

        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== BOTTOM BUTTONS =====
        JButton deleteUserBtn = new JButton("Delete User");
        JButton backBtn = new JButton("Back to Admin");

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(deleteUserBtn);
        bottom.add(backBtn);
        add(bottom, BorderLayout.PAGE_END);

        deleteUserBtn.addActionListener(e -> deleteSelectedUser());
        backBtn.addActionListener(e -> frame.showAdmin());

        updateSummary();
    }

    private void setBold(JLabel lbl) {
        lbl.setFont(new Font("Tahoma", Font.BOLD, 14));
    }

    public void refresh() {
        model.reload();
        updateSummary();
    }

    // ===== DELETE USER LOGIC =====
    private void deleteSelectedUser() {
    int row = table.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this,
            "Please select a user to delete.");
        return;
    }

    String username = table.getValueAt(row, 0).toString();

    int confirm = JOptionPane.showConfirmDialog(
        this,
        "This will delete ALL quiz records of user: " + username +
        "\nAre you sure?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {

        // 🔥 DIRECTLY REMOVE USER RESULTS FROM DATASTORE
        DataStore.getAllResults()
                .removeIf(r -> r.username.equals(username));

        // refresh UI
        model.reload();
        updateSummary();

        JOptionPane.showMessageDialog(this,
            "User quiz records deleted successfully.");
    }
}


    // ===== SUMMARY UPDATE =====
    private void updateSummary() {
        usersLabel.setText("Students Attempted Quizzes: " + model.getUniqueUserCount());
        attemptsLabel.setText("Total Quiz Attempts Recorded: " + model.getAttemptCount());
        accuracyLabel.setText("Overall Student Accuracy (All Attempts): "
                + model.getOverallAccuracy() + "%");
        bestCategoryLabel.setText("Highest Scoring Category (By Accuracy): "
                + model.getBestCategory());
    }

    // ================= TABLE MODEL =================
    static class ReportsTableModel extends AbstractTableModel {

        private QuizResult[] rows = new QuizResult[0];

        private final String[] cols = {
            "Username",
            "Category",
            "Level",
            "Correct / Total",
            "Attempt Accuracy (%)",
            "Highest Score (%)",
            "Attempt Date"
        };

        private final Map<String, Integer> highestScoreMap = new HashMap<>();

        void reload() {
            Collection<QuizResult> results = DataStore.getAllResults();
            rows = results.toArray(QuizResult[]::new);

            calculateHighestScores();

            Arrays.sort(rows,
                Comparator.comparing((QuizResult r) -> r.username)
            );

            fireTableDataChanged();
        }

        // ===== HIGHEST SCORE LOGIC =====
        private void calculateHighestScores() {
            highestScoreMap.clear();

            for (QuizResult r : rows) {
                int score = (int) ((r.correct * 100.0) / r.totalQuestions);
                highestScoreMap.merge(r.username, score, Math::max);
            }
        }

        // ===== SUMMARY HELPERS =====
        int getAttemptCount() {
            return rows.length;
        }

        int getUniqueUserCount() {
            return highestScoreMap.size();
        }

        int getOverallAccuracy() {
            if (rows.length == 0) return 0;

            int correct = 0;
            int total = 0;

            for (QuizResult r : rows) {
                correct += r.correct;
                total += r.totalQuestions;
            }

            return (int) ((correct * 100.0) / total);
        }

        String getBestCategory() {
            if (rows.length == 0) return "-";

            Map<String, int[]> stats = new HashMap<>();

            for (QuizResult r : rows) {
                stats.putIfAbsent(r.category, new int[2]);
                stats.get(r.category)[0] += r.correct;
                stats.get(r.category)[1] += r.totalQuestions;
            }

            return stats.entrySet().stream()
                .max(Comparator.comparingDouble(
                    e -> (e.getValue()[0] * 100.0) / e.getValue()[1]))
                .map(Map.Entry::getKey)
                .orElse("-");
        }

        // ===== TABLE OVERRIDES =====
        @Override
        public int getRowCount() {
            return rows.length;
        }

        @Override
        public int getColumnCount() {
            return cols.length;
        }

        @Override
        public String getColumnName(int column) {
            return cols[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            QuizResult r = rows[rowIndex];
            int accuracy = (int) ((r.correct * 100.0) / r.totalQuestions);

            return switch (columnIndex) {
                case 0 -> r.username;
                case 1 -> r.category;
                case 2 -> r.level;
                case 3 -> r.correct + " / " + r.totalQuestions;
                case 4 -> accuracy + "%";
                case 5 -> highestScoreMap.get(r.username) + "%";
                case 6 -> r.timestamp.toLocalDate().toString();
                default -> "";
            };
        }
    }
}
