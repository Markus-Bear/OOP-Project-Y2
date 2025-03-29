package view;

import javax.swing.*;
import java.awt.Component;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import controller.LoginManager;
import model.User;
import exception.AuthenticationException;
import exception.DatabaseOperationException;
import exception.RoleAccessException;


public class LoginFrame extends JFrame {
    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Log in");
        setSize(500, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Main panel using a dark gray background color.
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // --- TOP ICON (replace "/view/icons/SETU_logo.png" with your actual path) ---
        JLabel iconLabel = new JLabel(new ImageIcon(getClass().getResource("/view/icons/SETU_logo.png")));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createVerticalStrut(20));  // top spacing
        mainPanel.add(iconLabel);

        // --- TITLE & SUBTITLE ---
        JLabel titleLabel = new JLabel("SETU Media Equipment Rental System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0x435465));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(titleLabel);

        JLabel subTitleLabel = new JLabel("Allowing lecturers and students to rent equipment for projects.");
        subTitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subTitleLabel.setForeground(new Color(0x435465));
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(subTitleLabel);

        // --- CREDENTIALS PANEL (username & password fields) ---
        JPanel credsPanel = new JPanel();
        credsPanel.setOpaque(false); // Make background transparent so mainPanel shows through
        credsPanel.setLayout(new BoxLayout(credsPanel, BoxLayout.Y_AXIS));
        credsPanel.setMaximumSize(new Dimension(300, 150)); // Limit width
        credsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Font labelFont = new Font("SansSerif", Font.BOLD, 18);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 18);
        // "Username or Email Address" label
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setForeground(new Color(0x435465));
        emailLabel.setFont(labelFont);
        credsPanel.add(Box.createVerticalStrut(10));
        credsPanel.add(emailLabel);

        // Email text field
        emailTextField = new JTextField(20);
        emailTextField.setFont(fieldFont);
        credsPanel.add(emailTextField);

        // "Password" label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(new Color(0x435465));
        passwordLabel.setFont(labelFont);
        credsPanel.add(Box.createVerticalStrut(10));
        credsPanel.add(passwordLabel);

        // Password field
        passwordField = new JPasswordField(20);
        passwordField.setFont(fieldFont);
        credsPanel.add(passwordField);

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(credsPanel);

        // --- LOG IN BUTTON ---
        loginButton = new JButton("Log In");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setBackground(Color.DARK_GRAY);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(300, 40));
        loginButton.setMaximumSize(new Dimension(300, 40));
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(loginButton);

        // Removed the "Lost your password?" label

        getContentPane().add(mainPanel);

        // Action listeners for login
        loginButton.addActionListener(e -> {
            try {
                performLogin();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Incorrect Log In details. Please try again.",
                        "Login Error", JOptionPane.ERROR_MESSAGE
                );
            }
        });
        passwordField.addActionListener(e -> {
            try {
                performLogin();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Incorrect Log In details. Please try again.",
                        "Login Error", JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void performLogin() throws DatabaseOperationException, RoleAccessException {
        String email = emailTextField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email or password cannot be empty.",
                    "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LoginManager loginManager = new LoginManager();
            User user = loginManager.loginUser(email, password);
            // Launch appropriate main GUI based on the user's role.
            if (user.getRole().equalsIgnoreCase("Admin")) {
                new AdminFrame(user).setVisible(true);
            } else if (user.getRole().equalsIgnoreCase("MediaStaff")) {
                new MediaStaffFrame(user).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Only Admin and Media Staff GUI is implemented in this version.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();
        } catch (AuthenticationException ex) {
            JOptionPane.showMessageDialog(this, "Incorrect Log In details. Please try again.",
                    "Login Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
