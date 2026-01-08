package lnlearningsystem.ui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import lnlearningsystem.AppFrame;
import lnlearningsystem.DataStore;

public class CategoryPanel extends JPanel {

    private JComboBox<String> categoryCombo;
    private JButton nextBtn;
    private JButton backBtn;
    private JLabel titleLabel;

    public CategoryPanel(AppFrame parent) {

        // ===== MAIN PANEL =====
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 220, 245)); // light pink theme

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(15, 15, 15, 15);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;

        // ===== TITLE =====
        titleLabel = new JLabel("Select Category");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        add(titleLabel, c);

        // ===== CATEGORY COMBO =====
        c.gridy++;
        categoryCombo = new JComboBox<>();
        categoryCombo.setFont(new Font("Tahoma", Font.PLAIN, 18));
        categoryCombo.setPreferredSize(new Dimension(220, 40));
        add(categoryCombo, c);

        // ===== BUTTONS =====
        Font buttonFont = new Font("Tahoma", Font.BOLD, 15);
        Dimension btnSize = new Dimension(120, 40);

        nextBtn = new JButton("Next");
        backBtn = new JButton("Back");

        nextBtn.setFont(buttonFont);
        backBtn.setFont(buttonFont);

        nextBtn.setPreferredSize(btnSize);
        backBtn.setPreferredSize(btnSize);

        nextBtn.setBackground(new Color(180, 210, 240));
        backBtn.setBackground(new Color(200, 200, 200));

        nextBtn.setFocusPainted(false);
        backBtn.setFocusPainted(false);

        // ===== BUTTON PANEL =====
        c.gridy++;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonPanel.setBackground(new Color(245, 220, 245));

        buttonPanel.add(backBtn);
        buttonPanel.add(nextBtn);

        add(buttonPanel, c);

        // ===== ACTIONS =====
        nextBtn.addActionListener(e -> {
            String cat = (String) categoryCombo.getSelectedItem();
            if (cat == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "No categories available.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            parent.showLevel(cat);
        });

        backBtn.addActionListener(e -> parent.showMenu());
    }

    // ===== REFRESH =====
    public void refresh() {
        categoryCombo.removeAllItems();
        DataStore ds = DataStore.get();
        List<String> cats = ds.getCategories();
        for (String c : cats) {
            categoryCombo.addItem(c);
        }
    }
}
