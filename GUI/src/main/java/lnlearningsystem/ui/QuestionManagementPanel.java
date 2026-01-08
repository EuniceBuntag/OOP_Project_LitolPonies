package lnlearningsystem.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import lnlearningsystem.AppFrame;
import lnlearningsystem.DataStore;
import lnlearningsystem.model.Question;

public class QuestionManagementPanel extends JPanel {

    private final AppFrame frame;
    private final JTable table;
    private final QuestionTableModel model;

    private final JButton addBtn = new JButton("Add Question");
    private final JButton editBtn = new JButton("Edit Question");
    private final JButton deleteBtn = new JButton("Delete Question");
    private final JButton backBtn = new JButton("Back to Admin");

    public QuestionManagementPanel(AppFrame frame) {
        this.frame = frame;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(255, 223, 251));

        // Title
        JLabel title = new JLabel("Question Management", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // Table
        model = new QuestionTableModel();
        model.reload(); // 🔴 REQUIRED — missing before

        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.add(addBtn);
        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Actions
        addBtn.addActionListener(e -> addQuestion());
        editBtn.addActionListener(e -> editQuestion());
        deleteBtn.addActionListener(e -> deleteQuestion());
        backBtn.addActionListener(e -> frame.showAdmin());
    }

    public void refresh() {
        model.reload();
    }

    private void addQuestion() {
        QuestionFormDialog dialog = new QuestionFormDialog(frame, null);
        dialog.setVisible(true);

        if (dialog.savedQuestion != null) {
            DataStore.get().addQuestion(dialog.savedQuestion);
            refresh();
        }
    }

    private void editQuestion() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a question to edit.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Question q = model.getQuestionAt(table.convertRowIndexToModel(row));

        QuestionFormDialog dialog = new QuestionFormDialog(frame, q);
        dialog.setVisible(true);

        if (dialog.savedQuestion != null) {
            DataStore.get().updateQuestion(dialog.savedQuestion);
            refresh();
        }
    }

    private void deleteQuestion() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a question to delete.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Question q = model.getQuestionAt(table.convertRowIndexToModel(row));

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete question:\n" + q.text + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            DataStore.get().deleteQuestion(q.id);
            refresh();
        }
    }
    // ================= TABLE MODEL =================
    static class QuestionTableModel extends AbstractTableModel {

        private List<Question> rows = new ArrayList<>();
        private final String[] cols = {
                "Category",
                "Level",
                "Question",
                "Correct Answer"
        };

        void reload() {
            rows = DataStore.get().getQuestionsFiltered(null, null);

            rows.sort(
                Comparator.comparing((Question q) -> q.category)
                    .thenComparing(q -> q.level)
            );

            fireTableDataChanged();
        }

        Question getQuestionAt(int row) {
            return rows.get(row);
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
        public String getColumnName(int column) {
            return cols[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Question q = rows.get(rowIndex);

            return switch (columnIndex) {
                case 0 -> q.category;
                case 1 -> q.level;
                case 2 -> q.text;
                case 3 -> q.options[q.correctIndex];
                default -> "";
            };
        }
    }
}
