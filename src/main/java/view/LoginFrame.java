package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Build panel with GridBagLayout to center components.
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Log in");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        // Reset for fields.
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;

        // Email label and field.
        JLabel emailLabel = new JLabel("Email: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(emailLabel, gbc);

        emailTextField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(emailTextField, gbc);

        // Password label and field.
        JLabel passwordLabel = new JLabel("Password: ");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(passwordField, gbc);

        // Log in button.
        loginButton = new JButton("Log in");
        loginButton.setBackground(Color.DARK_GRAY);
        loginButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        // Action listeners.
        loginButton.addActionListener(e -> {
            try {
                performLogin();
            } catch (DatabaseOperationException ex) {
                throw new RuntimeException(ex);
            } catch (RoleAccessException ex) {
                throw new RuntimeException(ex);
            }
        });
        passwordField.addActionListener(e -> {
            try {
                performLogin();
            } catch (DatabaseOperationException ex) {
                throw new RuntimeException(ex);
            } catch (RoleAccessException ex) {
                throw new RuntimeException(ex);
            }
        });

        add(panel);
    }

    private void performLogin() throws DatabaseOperationException, RoleAccessException {
        String email = emailTextField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if(email.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(this, "Email or password cannot be empty.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LoginManager lm = new LoginManager();
            User user = lm.loginUser(email, password);
            JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + user.getName(), "Login", JOptionPane.INFORMATION_MESSAGE);
            if(user.getRole().equalsIgnoreCase("Admin")){
                new AdminFrame(user).setVisible(true);
            } else {
                // Extend for other roles as needed.
                JOptionPane.showMessageDialog(this, "Only Admin GUI is implemented in this version.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();
        } catch(AuthenticationException ex){
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        } catch(Exception ex){
            JOptionPane.showMessageDialog(this, "Unexpected error: " + ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
