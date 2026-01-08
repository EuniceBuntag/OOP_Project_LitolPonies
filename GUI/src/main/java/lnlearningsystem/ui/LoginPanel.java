package lnlearningsystem.ui;

import java.awt.*;
import javax.swing.*;
import lnlearningsystem.AppFrame;
import lnlearningsystem.DataStore;
import lnlearningsystem.model.Role;
import lnlearningsystem.model.User;

public class LoginPanel extends JPanel {

    private final JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPassword;
    private final AppFrame parent;

    public LoginPanel(AppFrame parent) {
        this.parent = parent;

        /* ===== PANEL SETUP ===== */
        setLayout(new GridBagLayout());
        setBackground(new Color(255, 223, 251));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;

        /* ===== TITLE ===== */
        JLabel title = new JLabel("Welcome to EduQuiz!", JLabel.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 28));

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        add(title, c);

        /* ===== USERNAME ===== */
        c.gridwidth = 1;
        c.gridy++;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        add(userLabel, c);

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        usernameField.setPreferredSize(new Dimension(220, 32));

        c.gridx = 1;
        add(usernameField, c);

        /* ===== PASSWORD ===== */
        c.gridx = 0;
        c.gridy++;

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        add(passLabel, c);

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(220, 32));

        c.gridx = 1;
        add(passwordField, c);

        /* ===== SHOW PASSWORD ===== */
        c.gridy++;
        c.gridx = 1;

        showPassword = new JCheckBox("Show Password");
        showPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
        showPassword.setBackground(new Color(255, 223, 251));

        add(showPassword, c);

        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });

        /* ===== BUTTONS ===== */
        c.gridy++;
        c.gridx = 0;

        JButton registerBtn = new JButton("Register");
        styleButton(registerBtn, new Color(190, 215, 245)); // soft sky blue
        add(registerBtn, c);

        c.gridx = 1;
        JButton loginBtn = new JButton("Login");
        styleButton(loginBtn, new Color(190, 230, 210)); // soft mint green
        add(loginBtn, c);

        /* ===== HINT ===== */
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;

        JLabel hint = new JLabel(
                "Try 'admin/admin' or 'student/student' (sample users)."
        );
        hint.setFont(new Font("Tahoma", Font.ITALIC, 12));
        add(hint, c);

        /* ===== ACTIONS ===== */
        loginBtn.addActionListener(e -> doLogin());
        registerBtn.addActionListener(e -> parent.showRegister());
    }

    /* ===== BUTTON STYLE ===== */
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Tahoma", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 34));
        button.setBackground(bgColor);
        button.setFocusPainted(false);
    }

    /* ===== LOGIN LOGIC ===== */
    private void doLogin() {
        String u = usernameField.getText().trim();
        String p = new String(passwordField.getPassword());

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter username and password",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = DataStore.getUser(u);

        if (user == null || !user.password.equals(p)) {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        parent.currentUser = user;

        if (user.role == Role.ADMIN) {
            parent.showAdmin();
        } else {
            parent.showMenu();
        }
    }
}
