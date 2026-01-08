package lnlearningsystem.ui;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import lnlearningsystem.AppFrame;
import lnlearningsystem.DataStore;
import lnlearningsystem.model.QuizResult;

public class LeaderboardPanel extends JPanel {
    JTable table;
    LeaderboardModel model;

    public LeaderboardPanel(AppFrame frame) {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 223, 251));
        add(new JLabel("<html><h2>Leaderboard</h2></html>"), BorderLayout.NORTH);
        model = new LeaderboardModel();
        table = new JTable(model);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton back = new JButton("Back");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        back.addActionListener(e -> frame.showMenu());
    }

    public void refresh() {
        model.reload();
    }

    static class LeaderboardModel extends AbstractTableModel {
        List<QuizResult> rows = new ArrayList<>();
        String[] cols = {"Rank", "User", "Category", "Level", "Score", "Date"};

        void reload() {
            rows = new ArrayList<>(DataStore.getAllResults());
            // Sort by score (correct/totalQuestions) in descending order
            rows.sort(Comparator.comparingDouble(
                    (QuizResult r) -> (double) r.correct / r.totalQuestions
            ).reversed());
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return rows.size();
        }

        @Override
        public int getColumnCount() {
            return cols.length;
        }

        @Override
        public String getColumnName(int c) {
            return cols[c];
        }

        @Override
        public Object getValueAt(int r, int c) {
            QuizResult qr = rows.get(r);
            switch (c) {
                case 0 -> { // Rank column
                    return r + 1; // Rank starts at 1
                }
                case 1 -> { // User
                    return qr.username;
                }
                case 2 -> { // Category
                    return qr.category;
                }
                case 3 -> { // Level
                    return qr.level;
                }
                case 4 -> { // Score
                    return qr.correct + "/" + qr.totalQuestions;
                }
                case 5 -> { // Date
                    return qr.timestamp.toLocalDate().toString();
                }
            }
            return "";
        }
    }
}
