package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import controller.UserController;
import controller.EquipmentController;
import controller.ReservationController;
import controller.CheckoutController;
import model.User;
import model.Equipment;
import model.Reservation;
import exception.DatabaseOperationException;
import exception.RoleAccessException;
import javax.swing.plaf.basic.BasicButtonUI;


public class AdminFrame extends JFrame {
    private User loggedInUser;
    private JTabbedPane tabbedPane;

    public AdminFrame(User user) {
        this.loggedInUser = user;
        setTitle("Media Equipment Rental System - Admin Menu - " + user.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();

        // Create tabs
        tabbedPane.addTab("View Profile", new ViewProfilePanel(loggedInUser));
        tabbedPane.addTab("User Management", new UserManagementPanel(loggedInUser.getUserId()));
        tabbedPane.addTab("Equipment Management", new EquipmentManagementPanel(loggedInUser.getUserId()));
        tabbedPane.addTab("Reservations Management", new ReservationsManagementPanel(loggedInUser.getUserId()));

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Log out button at bottom right.
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Log out");
        logoutButton.setBackground(Color.DARK_GRAY);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setPreferredSize(new Dimension(100, 30));
        logoutButton.setMaximumSize(new Dimension(100, 30));
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        bottomPanel.add(logoutButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // --------------------------
    // View Profile Panel
    // --------------------------
    class ViewProfilePanel extends JPanel {
        public ViewProfilePanel(User user) {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel lblUserId = new JLabel("User ID:");
            JTextField tfUserId = new JTextField(user.getUserId(), 20);
            tfUserId.setEditable(false);

            JLabel lblName = new JLabel("Name:");
            JTextField tfName = new JTextField(user.getName(), 20);
            tfName.setEditable(false);

            JLabel lblEmail = new JLabel("Email:");
            JTextField tfEmail = new JTextField(user.getEmail(), 20);
            tfEmail.setEditable(false);

            JLabel lblRole = new JLabel("Role:");
            JTextField tfRole = new JTextField(user.getRole(), 20);
            tfRole.setEditable(false);

            gbc.gridx = 0; gbc.gridy = 0;
            add(lblUserId, gbc);
            gbc.gridx = 1;
            add(tfUserId, gbc);
            gbc.gridx = 0; gbc.gridy = 1;
            add(lblName, gbc);
            gbc.gridx = 1;
            add(tfName, gbc);
            gbc.gridx = 0; gbc.gridy = 2;
            add(lblEmail, gbc);
            gbc.gridx = 1;
            add(tfEmail, gbc);
            gbc.gridx = 0; gbc.gridy = 3;
            add(lblRole, gbc);
            gbc.gridx = 1;
            add(tfRole, gbc);
        }
    }


    private JButton createMenuItem(String text) {
        JButton button = new JButton(text);
        // Force a basic UI that respects our background color changes.
        button.setUI(new BasicButtonUI());

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(0, 50));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(true);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setRolloverEnabled(true);
        button.setBorderPainted(false);

        // Define colors for different states.
        final Color defaultColor = Color.DARK_GRAY;
        final Color hoverColor = new Color(64, 64, 64);    // slightly lighter grey
        final Color pressedColor = new Color(96, 96, 96);   // even lighter grey for pressed state

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultColor);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(pressedColor);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (button.contains(e.getPoint())) {
                    button.setBackground(hoverColor);
                } else {
                    button.setBackground(defaultColor);
                }
            }
        });
        return button;
    }


    // --------------------------
    // User Management Panel
    // --------------------------
    class UserManagementPanel extends JPanel {
        private String adminId;
        private JPanel contentPanel;

        public UserManagementPanel(String adminId) {
            this.adminId = adminId;


            // Sidebar with MenuItems
            JPanel sidebar = new JPanel();
            sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            sidebar.setBackground(Color.DARK_GRAY);


            JButton buttonViewAllUsers = createMenuItem("View All Users");
            JButton buttonAddUser = createMenuItem("Add New User");
            JButton buttonUpdateUser = createMenuItem("Update User");
            JButton buttonDeleteUser = createMenuItem("Delete User");

            sidebar.add(Box.createRigidArea(new Dimension(0, 50)));
            sidebar.add(buttonViewAllUsers);
            sidebar.add(Box.createRigidArea(new Dimension(0, 50)));
            sidebar.add(buttonAddUser);
            sidebar.add(Box.createRigidArea(new Dimension(0, 50)));
            sidebar.add(buttonUpdateUser);
            sidebar.add(Box.createRigidArea(new Dimension(0, 50)));
            sidebar.add(buttonDeleteUser);

            // Create content panel (initially with a placeholder)
            contentPanel = new JPanel(new BorderLayout());
            contentPanel.add(new JLabel("Select an option from the sidebar."), BorderLayout.CENTER);

            // Create JSplitPane and lock its divider.
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, contentPanel);
            splitPane.setDividerLocation(200);
            splitPane.setEnabled(false);
            splitPane.setDividerSize(0);

            setLayout(new BorderLayout());
            add(splitPane, BorderLayout.CENTER);

            // Action listeners
            buttonViewAllUsers.addActionListener(e -> loadViewAllUsers());
            buttonAddUser.addActionListener(e -> loadAddUser());
            buttonUpdateUser.addActionListener(e -> loadUpdateUser());
            buttonDeleteUser.addActionListener(e -> loadDeleteUser());
        }

        private void loadViewAllUsers() {
            final UserController uc = new UserController();
            try {
                final List<User> users = uc.getAllUsers("Admin");
                String[] colNames = {"User ID", "Email", "Name", "Role"};
                DefaultTableModel model = new DefaultTableModel(colNames, 0);
                for (User u : users) {
                    Object[] row = { u.getUserId(), u.getEmail(), u.getName(), u.getRole() };
                    model.addRow(row);
                }
                JTable table = new JTable(model);
                contentPanel.removeAll();
                contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
                contentPanel.revalidate();
                contentPanel.repaint();
            } catch (DatabaseOperationException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }

        private void loadAddUser() {
            // Main panel for the Add User form.
            JPanel addPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Row 0: Role selection
            JLabel lblRole = new JLabel("Role:");
            String[] roles = {"Student", "Lecturer", "Admin", "MediaStaff"};
            JComboBox<String> cbRole = new JComboBox<>(roles);
            gbc.gridx = 0; gbc.gridy = 0;
            addPanel.add(lblRole, gbc);
            gbc.gridx = 1;
            addPanel.add(cbRole, gbc);

            // Row 1: Email
            JLabel lblEmail = new JLabel("Email:");
            JTextField tfEmail = new JTextField(20);
            gbc.gridx = 0; gbc.gridy = 1;
            addPanel.add(lblEmail, gbc);
            gbc.gridx = 1;
            addPanel.add(tfEmail, gbc);

            // Row 2: Name
            JLabel lblName = new JLabel("Name:");
            JTextField tfName = new JTextField(20);
            gbc.gridx = 0; gbc.gridy = 2;
            addPanel.add(lblName, gbc);
            gbc.gridx = 1;
            addPanel.add(tfName, gbc);

            // Row 3: Password
            JLabel lblPassword = new JLabel("Password:");
            JPasswordField pfPassword = new JPasswordField(20);
            gbc.gridx = 0; gbc.gridy = 3;
            addPanel.add(lblPassword, gbc);
            gbc.gridx = 1;
            addPanel.add(pfPassword, gbc);

            // Row 4: Extra panel for additional fields (Department, Course, and Year for Student)
            JPanel extraPanel = new JPanel(new GridBagLayout());
            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
            addPanel.add(extraPanel, gbc);

            // Get the full list of departments from MethodsUtil.
            final String[] departments = controller.MethodsUtil.getDepartments();

            // Holder for the Year text field if needed.
            final JTextField[] yearFieldHolder = new JTextField[1];

            // Runnable to update extraPanel based on selected role.
            Runnable updateExtraPanel = () -> {
                extraPanel.removeAll();
                GridBagConstraints gbcExtra = new GridBagConstraints();
                gbcExtra.insets = new Insets(5, 5, 5, 5);
                gbcExtra.fill = GridBagConstraints.HORIZONTAL;
                gbcExtra.gridx = 0;
                gbcExtra.gridy = 0;
                String selectedRole = (String) cbRole.getSelectedItem();
                if ("Student".equalsIgnoreCase(selectedRole)) {
                    // For Student, add Department, Course, and Year.
                    JLabel lblDept = new JLabel("Department:");
                    JComboBox<String> cbDept = new JComboBox<>(departments);
                    gbcExtra.gridx = 0; gbcExtra.gridy = 0;
                    extraPanel.add(lblDept, gbcExtra);
                    gbcExtra.gridx = 1;
                    extraPanel.add(cbDept, gbcExtra);

                    JLabel lblCourse = new JLabel("Course:");
                    String selectedDept = (String) cbDept.getSelectedItem();
                    String[] courses = controller.MethodsUtil.getCoursesForDepartment(selectedDept);
                    JComboBox<String> cbCourse = new JComboBox<>(courses);
                    gbcExtra.gridx = 0; gbcExtra.gridy = 1;
                    extraPanel.add(lblCourse, gbcExtra);
                    gbcExtra.gridx = 1;
                    extraPanel.add(cbCourse, gbcExtra);

                    // Add listener to update courses when department changes.
                    cbDept.addActionListener(e -> {
                        String dept = (String) cbDept.getSelectedItem();
                        String[] newCourses = controller.MethodsUtil.getCoursesForDepartment(dept);
                        cbCourse.setModel(new DefaultComboBoxModel<>(newCourses));
                    });

                    // Add Year field.
                    JLabel lblYear = new JLabel("Year:");
                    JTextField tfYear = new JTextField(5);
                    yearFieldHolder[0] = tfYear;
                    gbcExtra.gridx = 0; gbcExtra.gridy = 2;
                    extraPanel.add(lblYear, gbcExtra);
                    gbcExtra.gridx = 1;
                    extraPanel.add(tfYear, gbcExtra);
                } else if ("Lecturer".equalsIgnoreCase(selectedRole)) {
                    // For Lecturer, add only Department.
                    JLabel lblDept = new JLabel("Department:");
                    JComboBox<String> cbDept = new JComboBox<>(departments);
                    gbcExtra.gridx = 0; gbcExtra.gridy = 0;
                    extraPanel.add(lblDept, gbcExtra);
                    gbcExtra.gridx = 1;
                    extraPanel.add(cbDept, gbcExtra);
                }
                extraPanel.revalidate();
                extraPanel.repaint();
            };

            // Initial update.
            updateExtraPanel.run();
            cbRole.addActionListener(e -> updateExtraPanel.run());

            // Row 5: Submit button.
            JButton addUserButton = new JButton("Add User");
            addUserButton.setBackground(Color.LIGHT_GRAY);
            addUserButton.setForeground(Color.DARK_GRAY);
            addUserButton.setPreferredSize(new Dimension(30, 30));
            addUserButton.setMaximumSize(new Dimension(30, 30));
            addUserButton.setFocusPainted(false);
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 2;
            addPanel.add(addUserButton, gbc);

            addUserButton.addActionListener(e -> {
                String email = tfEmail.getText().trim();
                String name = tfName.getText().trim();
                String role = (String) cbRole.getSelectedItem();
                String password = new String(pfPassword.getPassword()).trim();
                if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Email, Name, and Password must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setRole(role);
                // Encrypt the password using your back-end PasswordUtils.
                newUser.setPassword(controller.PasswordUtils.hashPassword(password));

                if ("Student".equalsIgnoreCase(role)) {
                    // Expect two JComboBox components in extraPanel: first for Department, second for Course.
                    Component[] comps = extraPanel.getComponents();
                    String department = "";
                    String course = "";
                    int comboCount = 0;
                    for (Component comp : comps) {
                        if (comp instanceof JComboBox) {
                            @SuppressWarnings("unchecked")
                            JComboBox<String> cb = (JComboBox<String>) comp;
                            if (comboCount == 0) {
                                department = (String) cb.getSelectedItem();
                            } else if (comboCount == 1) {
                                course = (String) cb.getSelectedItem();
                            }
                            comboCount++;
                        }
                    }
                    newUser.setDepartment(department);
                    newUser.setCourse(course);
                    // Retrieve the year.
                    if (yearFieldHolder[0] != null) {
                        String yearStr = yearFieldHolder[0].getText().trim();
                        if (!yearStr.isEmpty()) {
                            try {
                                int year = Integer.parseInt(yearStr);
                                newUser.setYear(year);
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(this, "Invalid year entered.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Year must be filled for a student.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                } else if ("Lecturer".equalsIgnoreCase(role)) {
                    // Expect one JComboBox for Department.
                    for (Component comp : extraPanel.getComponents()) {
                        if (comp instanceof JComboBox) {
                            @SuppressWarnings("unchecked")
                            JComboBox<String> cb = (JComboBox<String>) comp;
                            newUser.setDepartment((String) cb.getSelectedItem());
                        }
                    }
                }
                UserController uc = new UserController();
                boolean success = uc.addUser(newUser, adminId);
                if (success)
                    JOptionPane.showMessageDialog(this, "User added successfully.");
                else
                    JOptionPane.showMessageDialog(this, "Failed to add user.");
            });

            contentPanel.removeAll();
            contentPanel.add(addPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }





        private void loadUpdateUser() {
            final UserController uc = new UserController();
            try {
                final List<User> users = uc.getAllUsers("Admin");
                JPanel updatePanel = new JPanel(new BorderLayout());
                String[] colNames = {"User ID", "Email", "Name", "Role"};
                final DefaultTableModel model = new DefaultTableModel(colNames, 0);
                for (User u : users) {
                    Object[] row = { u.getUserId(), u.getEmail(), u.getName(), u.getRole() };
                    model.addRow(row);
                }
                final JTable table = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(table);
                updatePanel.add(scrollPane, BorderLayout.CENTER);
                JButton updateUserButton = new JButton("Update Selected User");
                updateUserButton.setBackground(Color.LIGHT_GRAY);
                updateUserButton.setForeground(Color.DARK_GRAY);
                updateUserButton.setPreferredSize(new Dimension(30, 30));
                updateUserButton.setMaximumSize(new Dimension(30, 30));
                updateUserButton.setFocusPainted(false);
                updatePanel.add(updateUserButton, BorderLayout.SOUTH);
                updateUserButton.addActionListener(e -> {
                    int selectedRow = table.getSelectedRow();
                    if(selectedRow < 0){
                        JOptionPane.showMessageDialog(this, "Please select a user to edit.");
                        return;
                    }
                    String userId = (String) model.getValueAt(selectedRow, 0);
                    try {
                        User selectedUser = uc.getUserById(userId);
                        if(selectedUser == null){
                            JOptionPane.showMessageDialog(this, "User not found.");
                            return;
                        }
                        JPanel editPanel = new JPanel(new GridBagLayout());
                        GridBagConstraints gbc = new GridBagConstraints();
                        gbc.insets = new Insets(5, 5, 5, 5);
                        gbc.fill = GridBagConstraints.HORIZONTAL;

                        JLabel lblEmail = new JLabel("Email:");
                        JTextField tfEmail = new JTextField(selectedUser.getEmail(), 20);
                        JLabel lblName = new JLabel("Name:");
                        JTextField tfName = new JTextField(selectedUser.getName(), 20);
                        JLabel lblRole = new JLabel("Role:");
                        JTextField tfRole = new JTextField(selectedUser.getRole(), 20);
                        tfRole.setEditable(false); // Role not editable in update
                        JButton updateUserSubmitButton = new JButton("Update User");
                        updateUserSubmitButton.setBackground(Color.LIGHT_GRAY);
                        updateUserSubmitButton.setForeground(Color.DARK_GRAY);
                        updateUserButton.setPreferredSize(new Dimension(30, 30));
                        updateUserButton.setMaximumSize(new Dimension(30, 30));
                        updateUserSubmitButton.setFocusPainted(false);

                        gbc.gridx = 0; gbc.gridy = 0;
                        editPanel.add(lblEmail, gbc);
                        gbc.gridx = 1;
                        editPanel.add(tfEmail, gbc);
                        gbc.gridx = 0; gbc.gridy = 1;
                        editPanel.add(lblName, gbc);
                        gbc.gridx = 1;
                        editPanel.add(tfName, gbc);
                        gbc.gridx = 0; gbc.gridy = 2;
                        editPanel.add(lblRole, gbc);
                        gbc.gridx = 1;
                        editPanel.add(tfRole, gbc);
                        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
                        editPanel.add(updateUserSubmitButton, gbc);

                        updateUserSubmitButton.addActionListener(ev -> {
                            selectedUser.setEmail(tfEmail.getText().trim());
                            selectedUser.setName(tfName.getText().trim());
                            boolean success = uc.updateUser(selectedUser, adminId);
                            if(success)
                                JOptionPane.showMessageDialog(this, "User updated successfully.");
                            else
                                JOptionPane.showMessageDialog(this, "Failed to update user.");
                        });

                        contentPanel.removeAll();
                        contentPanel.add(editPanel, BorderLayout.CENTER);
                        contentPanel.revalidate();
                        contentPanel.repaint();
                    } catch (DatabaseOperationException ex) {
                        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                    }
                });
                contentPanel.removeAll();
                contentPanel.add(updatePanel, BorderLayout.CENTER);
                contentPanel.revalidate();
                contentPanel.repaint();
            } catch (DatabaseOperationException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }

        private void loadDeleteUser() {
            final UserController uc = new UserController();
            try {
                final List<User> users = uc.getAllUsers("Admin");
                JPanel deletePanel = new JPanel(new BorderLayout());
                String[] colNames = {"User ID", "Email", "Name", "Role"};
                final DefaultTableModel model = new DefaultTableModel(colNames, 0);
                for (User u : users) {
                    Object[] row = { u.getUserId(), u.getEmail(), u.getName(), u.getRole() };
                    model.addRow(row);
                }
                final JTable table = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(table);
                deletePanel.add(scrollPane, BorderLayout.CENTER);
                JButton deleteUserButton = new JButton("Delete Selected User");
                deleteUserButton.setBackground(Color.LIGHT_GRAY);
                deleteUserButton.setForeground(Color.DARK_GRAY);
                deleteUserButton.setPreferredSize(new Dimension(30, 30));
                deleteUserButton.setMaximumSize(new Dimension(30, 30));
                deleteUserButton.setFocusPainted(false);
                deletePanel.add(deleteUserButton, BorderLayout.SOUTH);
                deleteUserButton.addActionListener(e -> {
                    int selectedRow = table.getSelectedRow();
                    if(selectedRow < 0){
                        JOptionPane.showMessageDialog(this, "Please select a user to delete.");
                        return;
                    }
                    String userId = (String) model.getValueAt(selectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete user " + userId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if(confirm == JOptionPane.YES_OPTION){
                        boolean success = uc.deleteUser(userId, adminId);
                        if(success){
                            JOptionPane.showMessageDialog(this, "User deleted successfully.");
                            model.removeRow(selectedRow);
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to delete user.");
                        }
                    }
                });
                contentPanel.removeAll();
                contentPanel.add(deletePanel, BorderLayout.CENTER);
                contentPanel.revalidate();
                contentPanel.repaint();
            } catch (DatabaseOperationException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    // --------------------------
    // Equipment Management Panel
    // --------------------------
    class EquipmentManagementPanel extends JPanel {
        private String adminId;
        private JPanel contentPanel;

        public EquipmentManagementPanel(String adminId) {
            this.adminId = adminId;

            JPanel sidebar = new JPanel();
            sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            sidebar.setBackground(Color.DARK_GRAY);

            JButton buttonViewAllEquipment = createMenuItem("View All Equipment");
            JButton buttonViewByType = createMenuItem("View Equipment by Type");
            JButton buttonAddEquipment = createMenuItem("Add New Equipment");
            JButton buttonUpdateEquipment = createMenuItem("Update Equipment");
            JButton buttonDeleteEquipment = createMenuItem("Delete Equipment");
            sidebar.add(buttonViewAllEquipment);
            sidebar.add(Box.createRigidArea(new Dimension(0, 50)));
            sidebar.add(buttonViewByType);
            sidebar.add(Box.createRigidArea(new Dimension(0, 50)));
            sidebar.add(buttonAddEquipment);
            sidebar.add(Box.createRigidArea(new Dimension(0, 50)));
            sidebar.add(buttonUpdateEquipment);
            sidebar.add(Box.createRigidArea(new Dimension(0, 50)));
            sidebar.add(buttonDeleteEquipment);

            contentPanel = new JPanel(new BorderLayout());
            contentPanel.add(new JLabel("Select an option from the sidebar."), BorderLayout.CENTER);

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, contentPanel);
            splitPane.setDividerLocation(200);
            splitPane.setEnabled(false);
            splitPane.setDividerSize(0);

            setLayout(new BorderLayout());
            add(splitPane, BorderLayout.CENTER);

            buttonViewAllEquipment.addActionListener(e -> {
                try {
                    loadViewAllEquipment();
                } catch (DatabaseOperationException ex) {
                    throw new RuntimeException(ex);
                }
            });
            buttonViewByType.addActionListener(e -> {
                try {
                    loadViewEquipmentByType();
                } catch (DatabaseOperationException ex) {
                    throw new RuntimeException(ex);
                }
            });
            buttonAddEquipment.addActionListener(e -> loadAddEquipment());
            buttonUpdateEquipment.addActionListener(e -> {
                try {
                    loadUpdateEquipment();
                } catch (DatabaseOperationException ex) {
                    throw new RuntimeException(ex);
                }
            });
            buttonDeleteEquipment.addActionListener(e -> {
                try {
                    loadDeleteEquipment();
                } catch (DatabaseOperationException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        private void loadViewAllEquipment() throws DatabaseOperationException {
            EquipmentController ec = new EquipmentController();
            List<Equipment> equipments = ec.getAllEquipment("Admin");
            String[] colNames = {"Equipment ID", "Name", "Type", "Description", "Status", "State"};
            DefaultTableModel model = new DefaultTableModel(colNames, 0);
            for (Equipment eq : equipments) {
                Object[] row = { eq.getEquipmentId(), eq.getName(), eq.getType(), eq.getDescription(), eq.getStatus(), eq.getState() };
                model.addRow(row);
            }
            JTable table = new JTable(model);
            contentPanel.removeAll();
            contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        private void loadViewEquipmentByType() throws DatabaseOperationException {
            String[] types = {"Audio Recorder", "Camera", "Drone", "Laptop", "Lighting", "Projector", "VR Headset", "Other"};
            String type = (String) JOptionPane.showInputDialog(this, "Select Equipment Type:", "Equipment Type", JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
            if(type == null) return;
            EquipmentController ec = new EquipmentController();
            List<Equipment> equipments = ec.getEquipmentByType(type, loggedInUser.getRole());
            String[] colNames = {"Equipment ID", "Name", "Type", "Description", "Status", "State"};
            DefaultTableModel model = new DefaultTableModel(colNames, 0);
            for (Equipment eq : equipments) {
                Object[] row = { eq.getEquipmentId(), eq.getName(), eq.getType(), eq.getDescription(), eq.getStatus(), eq.getState() };
                model.addRow(row);
            }
            JTable table = new JTable(model);
            contentPanel.removeAll();
            contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        private void loadAddEquipment() {
            JPanel addPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5,5,5,5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel lblName = new JLabel("Name:");
            JTextField tfName = new JTextField(20);
            JLabel lblType = new JLabel("Type:");
            String[] types = {"Audio Recorder", "Camera", "Drone", "Laptop", "Lighting", "Projector", "VR Headset", "Other"};
            JComboBox<String> cbType = new JComboBox<>(types);
            JLabel lblDescription = new JLabel("Description:");
            JTextField tfDescription = new JTextField(20);
            JLabel lblState = new JLabel("State:");
            String[] states = {"New", "Good", "Fair", "Poor"};
            JComboBox<String> cbState = new JComboBox<>(states);

            JButton btnSubmit = new JButton("Add Equipment");
            gbc.gridx = 0; gbc.gridy = 0;
            addPanel.add(lblName, gbc);
            gbc.gridx = 1;
            addPanel.add(tfName, gbc);
            gbc.gridx = 0; gbc.gridy = 1;
            addPanel.add(lblType, gbc);
            gbc.gridx = 1;
            addPanel.add(cbType, gbc);
            gbc.gridx = 0; gbc.gridy = 2;
            addPanel.add(lblDescription, gbc);
            gbc.gridx = 1;
            addPanel.add(tfDescription, gbc);
            gbc.gridx = 0; gbc.gridy = 3;
            addPanel.add(lblState, gbc);
            gbc.gridx = 1;
            addPanel.add(cbState, gbc);
            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
            addPanel.add(btnSubmit, gbc);

            btnSubmit.addActionListener(e -> {
                String name = tfName.getText().trim();
                String type = (String) cbType.getSelectedItem();
                String description = tfDescription.getText().trim();
                String state = (String) cbState.getSelectedItem();
                if(name.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Equipment eq = new Equipment();
                eq.setName(name);
                eq.setType(type);
                eq.setDescription(description);
                eq.setState(state);
                eq.setStatus("Available");
                EquipmentController ec = new EquipmentController();
                boolean success = ec.addEquipment(eq, adminId);
                if(success)
                    JOptionPane.showMessageDialog(this, "Equipment added successfully.");
                else
                    JOptionPane.showMessageDialog(this, "Failed to add equipment.");
            });

            contentPanel.removeAll();
            contentPanel.add(addPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        private void loadUpdateEquipment() throws DatabaseOperationException {
            EquipmentController ec = new EquipmentController();
            List<Equipment> equipments = ec.getAllEquipment("Admin");
            JPanel updatePanel = new JPanel(new BorderLayout());
            String[] colNames = {"Equipment ID", "Name", "Type", "Description", "Status", "State"};
            final DefaultTableModel model = new DefaultTableModel(colNames, 0);
            for (Equipment eq : equipments) {
                Object[] row = { eq.getEquipmentId(), eq.getName(), eq.getType(), eq.getDescription(), eq.getStatus(), eq.getState() };
                model.addRow(row);
            }
            final JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            updatePanel.add(scrollPane, BorderLayout.CENTER);
            JButton btnEdit = new JButton("Edit Selected Equipment");
            updatePanel.add(btnEdit, BorderLayout.SOUTH);
            btnEdit.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if(selectedRow < 0){
                    JOptionPane.showMessageDialog(this, "Please select equipment to edit.");
                    return;
                }
                String eqId = (String) model.getValueAt(selectedRow, 0);
                Equipment selectedEq = equipments.stream().filter(eq -> eq.getEquipmentId().equals(eqId)).findFirst().orElse(null);
                if(selectedEq == null){
                    JOptionPane.showMessageDialog(this, "Equipment not found.");
                    return;
                }
                JPanel editPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5,5,5,5);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                JLabel lblName = new JLabel("Name:");
                JTextField tfName = new JTextField(selectedEq.getName(), 20);
                JLabel lblType = new JLabel("Type:");
                String[] types = {"Audio Recorder", "Camera", "Drone", "Laptop", "Lighting", "Projector", "VR Headset", "Other"};
                JComboBox<String> cbType = new JComboBox<>(types);
                cbType.setSelectedItem(selectedEq.getType());
                JLabel lblDescription = new JLabel("Description:");
                JTextField tfDescription = new JTextField(selectedEq.getDescription(), 20);
                JLabel lblState = new JLabel("State:");
                String[] states = {"New", "Good", "Fair", "Poor"};
                JComboBox<String> cbState = new JComboBox<>(states);
                cbState.setSelectedItem(selectedEq.getState());

                JButton btnUpdateEquipment = new JButton("Update Equipment");

                gbc.gridx = 0; gbc.gridy = 0;
                editPanel.add(lblName, gbc);
                gbc.gridx = 1;
                editPanel.add(tfName, gbc);
                gbc.gridx = 0; gbc.gridy = 1;
                editPanel.add(lblType, gbc);
                gbc.gridx = 1;
                editPanel.add(cbType, gbc);
                gbc.gridx = 0; gbc.gridy = 2;
                editPanel.add(lblDescription, gbc);
                gbc.gridx = 1;
                editPanel.add(tfDescription, gbc);
                gbc.gridx = 0; gbc.gridy = 3;
                editPanel.add(lblState, gbc);
                gbc.gridx = 1;
                editPanel.add(cbState, gbc);
                gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
                editPanel.add(btnUpdateEquipment, gbc);

                btnUpdateEquipment.addActionListener(ev -> {
                    selectedEq.setName(tfName.getText().trim());
                    selectedEq.setType((String) cbType.getSelectedItem());
                    selectedEq.setDescription(tfDescription.getText().trim());
                    selectedEq.setState((String) cbState.getSelectedItem());
                    boolean success = ec.updateEquipment(selectedEq, adminId);
                    if(success)
                        JOptionPane.showMessageDialog(this, "Equipment updated successfully.");
                    else
                        JOptionPane.showMessageDialog(this, "Failed to update equipment.");
                });

                contentPanel.removeAll();
                contentPanel.add(editPanel, BorderLayout.CENTER);
                contentPanel.revalidate();
                contentPanel.repaint();
            });
            contentPanel.removeAll();
            contentPanel.add(updatePanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        private void loadDeleteEquipment() throws DatabaseOperationException {
            EquipmentController ec = new EquipmentController();
            List<Equipment> equipments = ec.getAllEquipment("Admin");
            JPanel deletePanel = new JPanel(new BorderLayout());
            String[] colNames = {"Equipment ID", "Name", "Type", "Description", "Status", "State"};
            final DefaultTableModel model = new DefaultTableModel(colNames, 0);
            for (Equipment eq : equipments) {
                Object[] row = { eq.getEquipmentId(), eq.getName(), eq.getType(), eq.getDescription(), eq.getStatus(), eq.getState() };
                model.addRow(row);
            }
            final JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            deletePanel.add(scrollPane, BorderLayout.CENTER);
            JButton btnDelete = new JButton("Delete Selected Equipment");
            deletePanel.add(btnDelete, BorderLayout.SOUTH);
            btnDelete.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if(selectedRow < 0){
                    JOptionPane.showMessageDialog(this, "Please select equipment to delete.");
                    return;
                }
                String eqId = (String) model.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete equipment " + eqId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if(confirm == JOptionPane.YES_OPTION){
                    boolean success = ec.deleteEquipment(eqId, adminId);
                    if(success){
                        JOptionPane.showMessageDialog(this, "Equipment deleted successfully.");
                        model.removeRow(selectedRow);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete equipment.");
                    }
                }
            });
            contentPanel.removeAll();
            contentPanel.add(deletePanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    // --------------------------
    // Reservations Management Panel
    // --------------------------
    class ReservationsManagementPanel extends JPanel {
        private String adminId;
        private JPanel contentPanel;

        public ReservationsManagementPanel(String adminId) {
            this.adminId = adminId;
            setLayout(new BorderLayout());
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setDividerLocation(200);

            JPanel sidebar = new JPanel();
            sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            JButton btnApproval = new JButton("Approve/Reject Reservations");
            JButton btnCheckOut = new JButton("Check-Out Equipment");
            JButton btnCheckIn = new JButton("Check-In Equipment");
            sidebar.add(btnApproval);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(btnCheckOut);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebar.add(btnCheckIn);

            contentPanel = new JPanel(new BorderLayout());
            contentPanel.add(new JLabel("Select an option from the sidebar."), BorderLayout.CENTER);

            splitPane.setLeftComponent(sidebar);
            splitPane.setRightComponent(contentPanel);
            add(splitPane, BorderLayout.CENTER);

            btnApproval.addActionListener(e -> {
                try {
                    loadApproval();
                } catch (DatabaseOperationException ex) {
                    throw new RuntimeException(ex);
                }
            });
            btnCheckOut.addActionListener(e -> loadCheckOut());
            btnCheckIn.addActionListener(e -> loadCheckIn());
        }

        private void loadApproval() throws DatabaseOperationException {
            ReservationController rc = new ReservationController();
            // The actual back-end method expects two arguments.
            List<Reservation> reservations = rc.getAllReservations(adminId);
            String[] colNames = {"Reservation ID", "User", "Equipment", "Reservation Date", "Return Date", "Status"};
            DefaultTableModel model = new DefaultTableModel(colNames, 0);
            for (Reservation res : reservations) {
                Object[] row = { res.getReservationId(), res.getUserId(), res.getEquipmentId(), res.getReservationDate(), res.getReturnDate(), res.getStatus() };
                model.addRow(row);
            }
            JTable table = new JTable(model);
            contentPanel.removeAll();
            contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        private void loadCheckOut() {
            String resIdStr = JOptionPane.showInputDialog(this, "Enter Reservation ID for check-out:");
            String staffId = JOptionPane.showInputDialog(this, "Enter Staff ID:");
            if(resIdStr == null || staffId == null) return;
            try {
                int resId = Integer.parseInt(resIdStr);
                CheckoutController cc = new CheckoutController();
                boolean success = cc.checkOutEquipment(resId, staffId);
                if(success)
                    JOptionPane.showMessageDialog(this, "Equipment checked out successfully.");
                else
                    JOptionPane.showMessageDialog(this, "Failed to check out equipment.");
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }

        private void loadCheckIn() {
            String resIdStr = JOptionPane.showInputDialog(this, "Enter Reservation ID for check-in:");
            String staffId = JOptionPane.showInputDialog(this, "Enter Staff ID:");
            String equipmentState = JOptionPane.showInputDialog(this, "Enter Equipment State (Good, Fair, Poor):");
            if(resIdStr == null || staffId == null || equipmentState == null) return;
            try {
                int resId = Integer.parseInt(resIdStr);
                CheckoutController cc = new CheckoutController();
                boolean success = cc.checkInEquipment(resId, staffId, equipmentState);
                if(success)
                    JOptionPane.showMessageDialog(this, "Equipment checked in successfully.");
                else
                    JOptionPane.showMessageDialog(this, "Failed to check in equipment.");
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
}
