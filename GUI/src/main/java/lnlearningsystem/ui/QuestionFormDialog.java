package lnlearningsystem.ui;

import java.awt.*;
import javax.swing.*;
import lnlearningsystem.model.Level;
import lnlearningsystem.model.Question;

public class QuestionFormDialog extends JDialog {

    // ===== CHANGED: Category is now a ComboBox =====
    JComboBox<String> categoryCombo = new JComboBox<>(
            new String[]{"Literacy", "Numeracy","Spelling"}
    );

    JComboBox<String> levelCombo = new JComboBox<>(
            new String[]{"Beginner", "Intermediate", "Advanced"}
    );

    JTextArea questionArea = new JTextArea(3, 30);
    JTextField[] optFields = new JTextField[4];
    JComboBox<String> answerCombo = new JComboBox<>(new String[]{"A", "B", "C", "D"});

    JButton saveBtn = new JButton("Save");
    JButton cancelBtn = new JButton("Cancel");

    public Question savedQuestion = null;
    Question editing;

    public QuestionFormDialog(Window owner, Question q) {
        super(owner, "Question Form", ModalityType.APPLICATION_MODAL);
        editing = q;

        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;

        // ===== CATEGORY =====
        form.add(new JLabel("Category:"), c);
        c.gridx = 1;
        form.add(categoryCombo, c);

        // ===== LEVEL =====
        c.gridx = 0;
        c.gridy++;
        form.add(new JLabel("Level:"), c);
        c.gridx = 1;
        form.add(levelCombo, c);

        // ===== QUESTION =====
        c.gridx = 0;
        c.gridy++;
        form.add(new JLabel("Question:"), c);
        c.gridx = 1;
        form.add(new JScrollPane(questionArea), c);

        // ===== OPTIONS =====
        for (int i = 0; i < 4; i++) {
            c.gridx = 0;
            c.gridy++;
            form.add(new JLabel("Option " + (char) ('A' + i) + ":"), c);
            c.gridx = 1;
            optFields[i] = new JTextField(30);
            form.add(optFields[i], c);
        }

        // ===== CORRECT ANSWER =====
        c.gridx = 0;
        c.gridy++;
        form.add(new JLabel("Correct Answer:"), c);
        c.gridx = 1;
        form.add(answerCombo, c);

        add(form, BorderLayout.CENTER);

        // ===== BOTTOM BUTTONS =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(saveBtn);
        bottom.add(cancelBtn);
        add(bottom, BorderLayout.SOUTH);

        // ===== LOAD DATA IF EDITING =====
        if (q != null) {
            categoryCombo.setSelectedItem(q.category);

            String lvlStr = "Beginner";
            if (q.level == Level.INTERMEDIATE) lvlStr = "Intermediate";
            else if (q.level == Level.ADVANCED) lvlStr = "Advanced";
            levelCombo.setSelectedItem(lvlStr);

            questionArea.setText(q.text);
            for (int i = 0; i < 4; i++) optFields[i].setText(q.options[i]);
            answerCombo.setSelectedIndex(q.correctIndex);
        }

        saveBtn.addActionListener(e -> doSave());
        cancelBtn.addActionListener(e -> {
            savedQuestion = null;
            setVisible(false);
        });

        pack();
        setLocationRelativeTo(owner);
    }

    void doSave() {
        String cat = (String) categoryCombo.getSelectedItem();
        String qtxt = questionArea.getText().trim();

        if (cat == null || qtxt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category and question text are required.");
            return;
        }

        String[] opts = new String[4];
        for (int i = 0; i < 4; i++) {
            opts[i] = optFields[i].getText().trim();
            if (opts[i].isEmpty()) {
                JOptionPane.showMessageDialog(this, "All options are required.");
                return;
            }
        }

        String selLvl = (String) levelCombo.getSelectedItem();
        Level lvl = switch (selLvl) {
            case "Intermediate" -> Level.INTERMEDIATE;
            case "Advanced" -> Level.ADVANCED;
            default -> Level.BEGINNER;
        };

        int ans = answerCombo.getSelectedIndex();

        if (editing == null) {
            savedQuestion = new Question(
                    cat, lvl, qtxt,
                    opts[0], opts[1], opts[2], opts[3],
                    ans
            );
        } else {
            editing.category = cat;
            editing.level = lvl;
            editing.text = qtxt;
            editing.options = opts;
            editing.correctIndex = ans;
            savedQuestion = editing;
        }

        setVisible(false);
    }
}
