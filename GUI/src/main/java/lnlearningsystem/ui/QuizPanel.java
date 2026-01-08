package lnlearningsystem.ui;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import lnlearningsystem.AppFrame;
import lnlearningsystem.DataStore;
import lnlearningsystem.model.Level;
import lnlearningsystem.model.Question;
import lnlearningsystem.model.QuizResult;

public class QuizPanel extends JPanel {

    private final AppFrame frame;
    private List<Question> questions;
    private int currentQuestionIndex;
    private final Map<Integer, Integer> userAnswers = new HashMap<>();
    private String currentCategory;
    private Level currentLevel;

    /* ---------- UI ---------- */
    private JLabel progressLabel;
    private JProgressBar progressBar;

    /* ---------- BASE COLORS ---------- */
    private static final Color BG_COLOR = new Color(245, 247, 250);

    /* ---------- FONTS ---------- */
    private static final Font QUESTION_FONT =
            new Font("Segoe UI", Font.BOLD, 20);
    private static final Font OPTION_FONT =
            new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font META_FONT =
            new Font("Segoe UI", Font.BOLD, 13);
    private static final Font BUTTON_FONT =
            new Font("Segoe UI", Font.BOLD, 14);

    public QuizPanel(AppFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(20, 20));
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    /* ================= START QUIZ ================= */

public void startQuiz(String category, Level level) {
    this.currentCategory = category;
    this.currentLevel = level;

        questions = DataStore.get().getQuestionsFiltered(category, level);

        if (questions == null || questions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No questions available for this category and level.",
                    "No Questions",
                    JOptionPane.WARNING_MESSAGE);
            frame.showMenu();
            return;
        }

        currentQuestionIndex = 0;
        userAnswers.clear();
        showQuestion(currentQuestionIndex);
    }

    /* ================= SHOW QUESTION ================= */

    private void showQuestion(int index) {
        removeAll();

        Color themeColor = getCategoryColor(currentCategory);
        Color cardColor = getCardBackground(themeColor);
        Color optionColor = getOptionBackground(themeColor);

        Question q = questions.get(index);

        /* ---------- CARD ---------- */
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(cardColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeColor, 2),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        /* ---------- META ---------- */
        progressLabel = new JLabel(
                "Question " + (index + 1) + " of " + questions.size()
        );
        progressLabel.setFont(META_FONT);

        progressBar = new JProgressBar(0, questions.size());
        progressBar.setValue(index + 1);
        progressBar.setForeground(themeColor);
        progressBar.setBackground(new Color(220, 220, 220));
        progressBar.setPreferredSize(new Dimension(200, 8));
        progressBar.setBorderPainted(false);

        JPanel metaPanel = new JPanel(new BorderLayout(10, 5));
        metaPanel.setOpaque(false);
        metaPanel.add(progressLabel, BorderLayout.WEST);
        metaPanel.add(progressBar, BorderLayout.SOUTH);

        /* ---------- QUESTION ---------- */
        JLabel questionLabel = new JLabel(
                "<html><div style='text-align:center;'>" + q.text + "</div></html>"
        );
        questionLabel.setFont(QUESTION_FONT);
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        /* ---------- OPTIONS PANEL ---------- */
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(optionColor);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ButtonGroup group = new ButtonGroup();

        for (int i = 0; i < q.options.length; i++) {
            JRadioButton rb = new JRadioButton(q.options[i]);
            rb.setFont(OPTION_FONT);
            rb.setBackground(optionColor);
            rb.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            rb.setFocusPainted(false);

            final int optionIndex = i;
            rb.addActionListener(e ->
                    userAnswers.put(index, optionIndex)
            );

            if (userAnswers.getOrDefault(index, -1) == i) {
                rb.setSelected(true);
            }

            group.add(rb);
            optionsPanel.add(rb);
        }

        /* ---------- BUTTON ---------- */
        JButton nextBtn = new JButton(
                index < questions.size() - 1 ? "Next" : "Finish"
        );
        nextBtn.setFont(BUTTON_FONT);
        nextBtn.setPreferredSize(new Dimension(120, 40));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        bottom.add(nextBtn);

        nextBtn.addActionListener(e -> {
            if (currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++;
                showQuestion(currentQuestionIndex);
            } else {
                finishQuiz();
            }
        });

        /* ---------- ASSEMBLE ---------- */
        card.add(metaPanel, BorderLayout.NORTH);
        card.add(questionLabel, BorderLayout.CENTER);
        card.add(optionsPanel, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    /* ================= COLOR HELPERS ================= */

    private Color getCategoryColor(String category) {
        if (category == null) return Color.GRAY;

        return switch (category.toLowerCase()) {
            case "literacy" -> new Color(186, 104, 200);
            case "numeracy" -> new Color(66, 133, 244);
            case "science" -> new Color(76, 175, 80);
            default -> new Color(120, 120, 120);
        }; // purple
        // blue
        // green
    }

    private Color getCardBackground(Color accent) {
        return new Color(
                mix(accent.getRed(), 255, 0.90f),
                mix(accent.getGreen(), 255, 0.90f),
                mix(accent.getBlue(), 255, 0.90f)
        );
    }

    private Color getOptionBackground(Color accent) {
        return new Color(
                mix(accent.getRed(), 255, 0.95f),
                mix(accent.getGreen(), 255, 0.95f),
                mix(accent.getBlue(), 255, 0.95f)
        );
    }

    private int mix(int c1, int c2, float ratio) {
        return (int) (c1 * (1 - ratio) + c2 * ratio);
    }

    /* ================= FINISH QUIZ ================= */

    private void finishQuiz() {
        QuizResult result = new QuizResult(
                frame.currentUser.username,
                currentCategory,
                currentLevel,
                questions.size()
        );

        result.correct = calculateCorrectAnswers();
        DataStore.get().addResult(result);
        frame.showResults(result);
    }

    /* ================= SCORING ================= */

    private int calculateCorrectAnswers() {
        int correct = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (userAnswers.getOrDefault(i, -1) == questions.get(i).correctIndex) {
                correct++;
            }
        }
        return correct;
    }

    /* ================= REFRESH ================= */

    public void refreshQuiz() {
        if (currentCategory == null || currentLevel == null) return;

        questions = DataStore.get()
                .getQuestionsFiltered(currentCategory, currentLevel);

        currentQuestionIndex = 0;
        userAnswers.clear();
        showQuestion(currentQuestionIndex);
    }
}
