package lnlearningsystem.ui;

import java.awt.*;
import javax.swing.*;
import lnlearningsystem.AppFrame;
import lnlearningsystem.DataStore;
import lnlearningsystem.model.Role;
import lnlearningsystem.model.User;

public class RegistrationPanel extends JPanel {


    public RegistrationPanel(AppFrame frame) {

        // Same background as Login Panel
        setBackground(new Color(255, 223, 251));
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Tahoma", Font.BOLD, 14);
        Font fieldFont = new Font("Tahoma", Font.PLAIN, 13);
        Font titleFont = new Font("Tahoma", Font.BOLD, 28);

        // ===== TITLE =====
        JLabel title = new JLabel("Register New Account", JLabel.CENTER);
        title.setFont(titleFont);
        title.setForeground(new Color(40, 40, 40));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        add(title, c);

        // ===== USERNAME =====
        c.gridwidth = 1;
        c.gridy++;
        JLabel userLbl = new JLabel("Username:");
        userLbl.setFont(labelFont);
        add(userLbl, c);

        JTextField usernameField = new JTextField(15);
        usernameField.setFont(fieldFont);
        c.gridx = 1;
        add(usernameField, c);

        // ===== PASSWORD =====
        c.gridx = 0;
        c.gridy++;
        JLabel passLbl = new JLabel("Password:");
        passLbl.setFont(labelFont);
        add(passLbl, c);

        JPasswordField passwordField = new JPasswordField(14);
        passwordField.setFont(fieldFont);
        c.gridx = 1;
        add(passwordField, c);

        // ===== ROLE =====
        c.gridx = 0;
        c.gridy++;
        JLabel roleLbl = new JLabel("Role:");
        roleLbl.setFont(labelFont);
        add(roleLbl, c);

        JComboBox<Role> roleBox = new JComboBox<>(Role.values());
        roleBox.setFont(fieldFont);
        c.gridx = 1;
        add(roleBox, c);

        // ===== BUTTONS =====
        JButton backBtn = new JButton("Back");
        JButton registerBtn = new JButton("Register");

        backBtn.setFont(labelFont);
        registerBtn.setFont(labelFont);

        backBtn.setBackground(new Color(220, 220, 220));        // light gray
        registerBtn.setBackground(new Color(185, 220, 255));   // soft sky blue

        backBtn.setFocusPainted(false);
        registerBtn.setFocusPainted(false);

        backBtn.setPreferredSize(new Dimension(110, 35));
        registerBtn.setPreferredSize(new Dimension(110, 35));

        // Swapped order: Back (left) | Register (right)
        c.gridx = 0;
        c.gridy++;
        add(backBtn, c);

        c.gridx = 1;
        add(registerBtn, c);

        // ===== ACTIONS =====
        registerBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            Role role = (Role) roleBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please fill in all fields",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            User user = new User(username, password, role);
            DataStore.addUser(user);
            DataStore.saveUsers();

            JOptionPane.showMessageDialog(
                    this,
                    "Registration successful!"
            );

            frame.showLogin();
        });

        backBtn.addActionListener(e -> frame.showLogin());
    }
}
