package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import controller.LoginManager;
import model.User;
import exception.AuthenticationException;
import exception.DatabaseOperationException;
import exception.RoleAccessException;

/**
 * The LoginFrame class creates a login window for the Media Equipment Rental System.
 * It collects the user's email and password, then attempts to authenticate the user.
 * If authentication is successful, the corresponding main GUI is launched.
 */
public class LoginFrame extends JFrame {
    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JButton loginButton;

    /**
     * Constructs a new LoginFrame, sets up the GUI components and layout.
     */
    public LoginFrame() {
        setTitle("Log in");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Build panel with GridBagLayout to center components.
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraint = new GridBagConstraints();
        gridBagConstraint.insets = new Insets(10, 10, 10, 10);
        gridBagConstraint.fill = GridBagConstraints.HORIZONTAL;

        // Title label.
        JLabel titleLabel = new JLabel("Log in");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        gridBagConstraint.gridx = 0;
        gridBagConstraint.gridy = 0;
        gridBagConstraint.gridwidth = 2;
        gridBagConstraint.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gridBagConstraint);

        // Reset for fields.
        gridBagConstraint.gridwidth = 1;
        gridBagConstraint.anchor = GridBagConstraints.LINE_END;

        // Email label and field.
        JLabel emailLabel = new JLabel("Email: ");
        gridBagConstraint.gridx = 0;
        gridBagConstraint.gridy = 1;
        panel.add(emailLabel, gridBagConstraint);

        emailTextField = new JTextField(20);
        gridBagConstraint.gridx = 1;
        gridBagConstraint.gridy = 1;
        gridBagConstraint.anchor = GridBagConstraints.LINE_START;
        panel.add(emailTextField, gridBagConstraint);

        // Password label and field.
        JLabel passwordLabel = new JLabel("Password: ");
        gridBagConstraint.gridx = 0;
        gridBagConstraint.gridy = 2;
        gridBagConstraint.anchor = GridBagConstraints.LINE_END;
        panel.add(passwordLabel, gridBagConstraint);

        passwordField = new JPasswordField(20);
        gridBagConstraint.gridx = 1;
        gridBagConstraint.gridy = 2;
        gridBagConstraint.anchor = GridBagConstraints.LINE_START;
        panel.add(passwordField, gridBagConstraint);

        // Log in button.
        loginButton = new JButton("Log in");
        loginButton.setBackground(Color.DARK_GRAY);
        loginButton.setForeground(Color.WHITE);
        gridBagConstraint.gridx = 0;
        gridBagConstraint.gridy = 3;
        gridBagConstraint.gridwidth = 2;
        gridBagConstraint.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gridBagConstraint);

        // Action listeners for the login button and password field.
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

    /**
     * Attempts to authenticate the user using the entered email and password.
     * Displays error messages via dialogs if authentication fails or if inputs are invalid.
     *
     * @throws DatabaseOperationException if there is a database error during authentication.
     * @throws RoleAccessException        if the user's role is not authorized.
     */
    private void performLogin() throws DatabaseOperationException, RoleAccessException {
        String email = emailTextField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email or password cannot be empty.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LoginManager loginManager = new LoginManager();
            User user = loginManager.loginUser(email, password);
            JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + user.getName(), "Login", JOptionPane.INFORMATION_MESSAGE);
            // Launch appropriate main GUI based on the user's role.
            if (user.getRole().equalsIgnoreCase("Admin")) {
                new AdminFrame(user).setVisible(true);
            } else if (user.getRole().equalsIgnoreCase("MediaStaff")) {
                new MediaStaffFrame(user).setVisible(true);
            } else {
                // Extend for other roles as needed.
                JOptionPane.showMessageDialog(this, "Only Admin and Media Staff GUI is implemented in this version.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();
        } catch (AuthenticationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unexpected error: " + ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
