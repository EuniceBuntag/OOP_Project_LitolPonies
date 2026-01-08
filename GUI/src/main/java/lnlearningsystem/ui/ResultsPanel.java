package lnlearningsystem.ui;

import java.awt.*;
import javax.swing.*;
import lnlearningsystem.AppFrame;
import lnlearningsystem.model.QuizResult;

public class ResultsPanel extends JPanel {
    JLabel resultsLabel = new JLabel("Results will appear here.");

    public ResultsPanel(AppFrame frame) {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(255, 223, 251));

        resultsLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        resultsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(resultsLabel, BorderLayout.CENTER);

        JButton backBtn = new JButton("Back to Main Menu");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.add(backBtn);
        add(bottom, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> frame.showMenu());
    }

    // Required method by AppFrame
    public void setResult(QuizResult result) {
        String html = String.format(
            "<html><h2 style='color: navy;'>Quiz Complete!</h2>" +
            "<h3>Category: %s | Level: %s</h3>" +
            "<p style='font-size: 18px;'>Your Score: <b style='color: green;'>%d</b> out of <b style='color: black;'>%d</b></p>" +
            "<p>Timestamp: %s</p></html>",
            result.category, result.level.toString(), result.correct, result.totalQuestions, result.timestamp.toLocalDate().toString()
        );
        resultsLabel.setText(html);
    }
}