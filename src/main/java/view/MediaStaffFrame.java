package view;


import controller.*;
import model.User;
import model.Equipment;
import model.Reservation;
import exception.DatabaseOperationException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JPasswordField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;


public class MediaStaffFrame extends JFrame {
    private User loggedInUser;
    private JTabbedPane tabbedPane;

    public MediaStaffFrame(User user) {
        this.loggedInUser = user;
        setTitle("Media Equipment Rental System - Admin Menu - " + user.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();

        // Create tabs (Home tab added first)
        tabbedPane.addTab("Home", new HomePanel(loggedInUser));
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
    // Home Panel (new)
    // --------------------------
    // Inside your AdminFrame class:
    // Inside your AdminFrame class:
    class HomePanel extends JPanel {
        private ChartPanel reservationsStatusChartPanel;
        private Timer timer;
        private User loggedInUser; // access to the logged in user

        public HomePanel(User user) {
            this.loggedInUser = user;
            // Use a GridLayout (2 rows x 2 columns) for four charts.
            setLayout(new GridLayout(2, 2, 10, 10)); // 10px horizontal & vertical gaps

            int chartSize = 300; // width and height (adjust as needed)

            // Equipment States Chart (Bar Chart)
            JFreeChart equipmentStateChart = createEquipmentStateChart();
            ChartPanel cp1 = new ChartPanel(equipmentStateChart);
            cp1.setPreferredSize(new Dimension(chartSize, chartSize));
            add(cp1);

            // Checked Out Equipment Chart (Pie Chart)
            JFreeChart checkedOutChart = createCheckedOutChart();
            ChartPanel cp2 = new ChartPanel(checkedOutChart);
            cp2.setPreferredSize(new Dimension(chartSize, chartSize));
            add(cp2);

            // Reservations Status Chart (Bar Chart: Pending vs Approved)
            JFreeChart reservationsStatusChart = createReservationsStatusChart();
            reservationsStatusChartPanel = new ChartPanel(reservationsStatusChart);
            reservationsStatusChartPanel.setPreferredSize(new Dimension(chartSize, chartSize));
            add(reservationsStatusChartPanel);

            // User Reservations Chart (Bar Chart)
            JFreeChart userReservationsChart = createUserReservationsOverTimeChart();
            ChartPanel cp4 = new ChartPanel(userReservationsChart);
            cp4.setPreferredSize(new Dimension(chartSize, chartSize));
            add(cp4);

            // Set up a Timer to update the Reservations Status Chart every 5 seconds.
            timer = new Timer(5000, e -> updateReservationsStatusChart());
            timer.start();
        }

        // This method updates the Reservations Status Chart dynamically.
        private void updateReservationsStatusChart() {
            JFreeChart updatedChart = createReservationsStatusChart();
            reservationsStatusChartPanel.setChart(updatedChart);
        }

        // Creates a bar chart for Equipment States.
        private JFreeChart createEquipmentStateChart() {
            EquipmentController ec = new EquipmentController();
            // Use the current user's role for filtering. (Assuming getAllEquipment() accepts a role.)
            List<Equipment> equipments = ec.getAllEquipment(loggedInUser.getRole());
            Map<String, Integer> stateCounts = new HashMap<>();
            for (Equipment eq : equipments) {
                String state = eq.getState();
                if (state != null) {
                    stateCounts.put(state, stateCounts.getOrDefault(state, 0) + 1);
                }
            }
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Map.Entry<String, Integer> entry : stateCounts.entrySet()) {
                dataset.addValue(entry.getValue(), "Equipment", entry.getKey());
            }
            JFreeChart chart = ChartFactory.createBarChart("Equipment States", "State", "Count", dataset);
            // Enable data labels on the bar chart:
            chart.getCategoryPlot().getRenderer().setDefaultItemLabelGenerator(new org.jfree.chart.labels.StandardCategoryItemLabelGenerator());
            chart.getCategoryPlot().getRenderer().setDefaultItemLabelsVisible(true);
            return chart;
        }

        // Creates a pie chart for Checked Out Equipment.
        private JFreeChart createCheckedOutChart() {
            CheckoutController cc = new CheckoutController();
            List<String> checkedOut = cc.getCheckedOutEquipment();
            int checkedOutCount = checkedOut.size();
            EquipmentController ec = new EquipmentController();
            List<Equipment> equipments = ec.getAllEquipment(loggedInUser.getRole());
            int total = equipments.size();
            int notCheckedOut = total - checkedOutCount;
            DefaultPieDataset dataset = new DefaultPieDataset();
            dataset.setValue("Checked Out", checkedOutCount);
            dataset.setValue("Not Checked Out", notCheckedOut);
            JFreeChart chart = ChartFactory.createPieChart("Equipment Checked Out", dataset, true, true, false);
            // (You can add a StandardPieSectionLabelGenerator here for more detailed labels.)
            return chart;
        }

        // Creates a bar chart comparing pending and approved reservations.
        private JFreeChart createReservationsStatusChart() {
            ReservationController rc = new ReservationController();
            // Use the current user's ID to filter reservations.
            List<Reservation> reservations = rc.getAllReservations(loggedInUser.getUserId());
            int pendingCount = 0;
            int approvedCount = 0;
            for (Reservation res : reservations) {
                String status = res.getStatus();
                if (status != null) {
                    if (status.equalsIgnoreCase("Pending")) {
                        pendingCount++;
                    } else if (status.equalsIgnoreCase("Approved")) {
                        approvedCount++;
                    }
                }
            }
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            dataset.addValue(pendingCount, "Pending", "Reservations");
            dataset.addValue(approvedCount, "Approved", "Reservations");
            JFreeChart chart = ChartFactory.createBarChart("Reservations Status", "Status", "Count", dataset);
            chart.getCategoryPlot().getRenderer().setDefaultItemLabelGenerator(new org.jfree.chart.labels.StandardCategoryItemLabelGenerator());
            chart.getCategoryPlot().getRenderer().setDefaultItemLabelsVisible(true);
            return chart;
        }

        // Creates a bar chart for User Reservations.
        private JFreeChart createUserReservationsOverTimeChart() {
            ReservationController rc = new ReservationController();
            List<Reservation> reservations = rc.getAllReservations(loggedInUser.getUserId());
            Map<String, Integer> userCounts = new HashMap<>();
            for (Reservation res : reservations) {
                String userId = res.getUserId();
                if (userId != null) {
                    userCounts.put(userId, userCounts.getOrDefault(userId, 0) + 1);
                }
            }
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Map.Entry<String, Integer> entry : userCounts.entrySet()) {
                dataset.addValue(entry.getValue(), "Reservations", entry.getKey());
            }
            JFreeChart chart = ChartFactory.createBarChart("User Reservations", "User", "Count", dataset);
            chart.getCategoryPlot().getRenderer().setDefaultItemLabelGenerator(new org.jfree.chart.labels.StandardCategoryItemLabelGenerator());
            chart.getCategoryPlot().getRenderer().setDefaultItemLabelsVisible(true);
            return chart;
        }
    }


    // --------------------------
    // View Profile Panel
    // --------------------------
    class ViewProfilePanel extends JPanel {
        public ViewProfilePanel(User user) {
            setLayout(new GridBagLayout());
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new Insets(10, 10, 10, 10);
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

            JLabel labelUserId = new JLabel("User ID:");
            JTextField textFieldUserId = new JTextField(user.getUserId(), 20);
            textFieldUserId.setEditable(false);

            JLabel labelName = new JLabel("Name:");
            JTextField textFieldName = new JTextField(user.getName(), 20);
            textFieldName.setEditable(false);

            JLabel labelEmail = new JLabel("Email:");
            JTextField textFieldEmail = new JTextField(user.getEmail(), 20);
            textFieldEmail.setEditable(false);

            JLabel labelRole = new JLabel("Role:");
            JTextField textFieldRole = new JTextField(user.getRole(), 20);
            textFieldRole.setEditable(false);

            gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 0;
            add(labelUserId, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            add(textFieldUserId, gridBagConstraints);
            gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 1;
            add(labelName, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            add(textFieldName, gridBagConstraints);
            gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 2;
            add(labelEmail, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            add(textFieldEmail, gridBagConstraints);
            gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 3;
            add(labelRole, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            add(textFieldRole, gridBagConstraints);
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
            JButton buttonViewLecturers = createMenuItem("View Lecturers");
            JButton buttonViewStudents = createMenuItem("View Students");

            sidebar.add(Box.createRigidArea(new Dimension(0, 50)));
            sidebar.add(buttonViewAllUsers);
            sidebar.add(Box.createRigidArea(new Dimension(0, 50)));
            sidebar.add(buttonViewLecturers);
            sidebar.add(Box.createRigidArea(new Dimension(0, 50)));
            sidebar.add(buttonViewStudents);

            // Create content panel (initially with a placeholder)
            contentPanel = new JPanel(new BorderLayout());
            JLabel placeholderLabel = new JLabel("Select an option from the sidebar.");
            placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
            placeholderLabel.setVerticalAlignment(SwingConstants.CENTER);
            contentPanel.add(placeholderLabel, BorderLayout.CENTER);


            // Create JSplitPane and lock its divider.
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, contentPanel);
            splitPane.setDividerLocation(200);
            splitPane.setEnabled(false);
            splitPane.setDividerSize(0);

            setLayout(new BorderLayout());
            add(splitPane, BorderLayout.CENTER);

            // Action listeners
            buttonViewAllUsers.addActionListener(e -> loadViewAllUsers());
            buttonViewLecturers.addActionListener(e -> loadViewLecturers());
            buttonViewStudents.addActionListener(e -> loadViewStudents());
        }

        private void loadViewAllUsers() {
            final UserController uc = new UserController();
            try {
                final List<User> users = uc.getAllUsers("Admin");
                String[] columnNames = {"User ID", "Email", "Name", "Role"};
                DefaultTableModel model = new DefaultTableModel(columnNames, 0);
                for (User user : users) {
                    if(!user.getRole().equals("Admin")) {
                        Object[] row = { user.getUserId(), user.getEmail(), user.getName(), user.getRole() };
                        model.addRow(row);
                    }
                }
                JTable table = new JTable(model);

                // Center text in some columns
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

                contentPanel.removeAll();
                contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
                contentPanel.revalidate();
                contentPanel.repaint();
            } catch (DatabaseOperationException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }

        private void loadViewLecturers() {
            final UserController uc = new UserController();
            try {
                final List<User> users = uc.getAllUsers("Admin");
                String[] columnNames = {"User ID", "Email", "Name", "Role", "Department"};
                DefaultTableModel model = new DefaultTableModel(columnNames, 0);
                for (User user : users) {
                    if(user.getRole().equals("Lecturer")) {
                        Object[] row = { user.getUserId(), user.getEmail(), user.getName(), user.getRole(), user.getDepartment() };
                        model.addRow(row);
                    }

                }
                JTable table = new JTable(model);

                // Center text in some columns
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

                contentPanel.removeAll();
                contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
                contentPanel.revalidate();
                contentPanel.repaint();
            } catch (DatabaseOperationException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }

        private void loadViewStudents() {
            final UserController uc = new UserController();
            try {
                final List<User> users = uc.getAllUsers("Admin");
                String[] columnNames = {"Student ID", "Email", "Name", "Role", "Department", "Course", "Year"};
                DefaultTableModel model = new DefaultTableModel(columnNames, 0);
                for (User user : users) {
                    if(user.getRole().equals("Student")) {
                        Object[] row = { user.getUserId(), user.getEmail(), user.getName(), user.getRole(), user.getDepartment(), user.getCourse(), user.getYear() };
                        model.addRow(row);
                    }

                }
                JTable table = new JTable(model);

                // Center text in some columns
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

                contentPanel.removeAll();
                contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
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
            JButton buttonUpdateEquipmentState = createMenuItem("Update Equipment State");
            sidebar.add(buttonViewAllEquipment);
            sidebar.add(Box.createRigidArea(new Dimension(0, 50)));
            sidebar.add(buttonViewByType);
            sidebar.add(Box.createRigidArea(new Dimension(0, 50)));
            sidebar.add(buttonUpdateEquipmentState);


            contentPanel = new JPanel(new BorderLayout());
            JLabel placeholderLabel = new JLabel("Select an option from the sidebar.");
            placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
            placeholderLabel.setVerticalAlignment(SwingConstants.CENTER);
            contentPanel.add(placeholderLabel, BorderLayout.CENTER);


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
            buttonUpdateEquipmentState.addActionListener(e -> {
                try {
                    loadUpdateEquipmentState();
                } catch (DatabaseOperationException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        private void loadViewAllEquipment() throws DatabaseOperationException {
            EquipmentController equipmentController = new EquipmentController();
            List<Equipment> equipments = equipmentController.getAllEquipment("Admin");
            String[] colNames = {"Equipment ID", "Name", "Type", "Description", "Status", "State"};
            DefaultTableModel model = new DefaultTableModel(colNames, 0);
            for (Equipment equipment : equipments) {
                Object[] row = { equipment.getEquipmentId(), equipment.getName(), equipment.getType(), equipment.getDescription(), equipment.getStatus(), equipment.getState() };
                model.addRow(row);
            }
            JTable table = new JTable(model);

            // Center text in some columns
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

            contentPanel.removeAll();
            contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        private void loadViewEquipmentByType() throws DatabaseOperationException {
            String[] types = {"Audio Recorder", "Camera", "Drone", "Laptop", "Lighting", "Projector", "VR Headset", "Other"};
            String type = (String) JOptionPane.showInputDialog(this, "Select Equipment Type:", "Equipment Type", JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
            if(type == null) return;
            EquipmentController equipmentController = new EquipmentController();
            List<Equipment> equipments = equipmentController.getEquipmentByType(type, loggedInUser.getRole());
            String[] columnNames = {"Equipment ID", "Name", "Type", "Description", "Status", "State"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            for (Equipment equipment : equipments) {
                Object[] row = { equipment.getEquipmentId(), equipment.getName(), equipment.getType(), equipment.getDescription(), equipment.getStatus(), equipment.getState() };
                model.addRow(row);
            }
            JTable table = new JTable(model);

            // Center text in some columns
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

            contentPanel.removeAll();
            contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        private void loadUpdateEquipmentState() throws DatabaseOperationException {
            EquipmentController equipmentController = new EquipmentController();
            List<Equipment> equipments = equipmentController.getAllEquipment("Admin");
            JPanel updatePanel = new JPanel(new BorderLayout());
            String[] columnNames = {"Equipment ID", "Name", "Type", "Description", "Status", "State"};
            final DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            for (Equipment equipment : equipments) {
                if ("Available".equalsIgnoreCase(equipment.getStatus())) {  // Only include available equipment
                    Object[] row = { equipment.getEquipmentId(), equipment.getName(), equipment.getType(),
                            equipment.getDescription(), equipment.getStatus(), equipment.getState() };
                    model.addRow(row);
                }
            }
            final JTable table = new JTable(model);

            // Center text in some columns
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

            JScrollPane scrollPane = new JScrollPane(table);
            updatePanel.add(scrollPane, BorderLayout.CENTER);
            JButton updateEquipmentButton = new JButton("Update Selected Equipment");
            updateEquipmentButton.setBackground(Color.LIGHT_GRAY);
            updateEquipmentButton.setForeground(Color.DARK_GRAY);
            updateEquipmentButton.setPreferredSize(new Dimension(30, 30));
            updateEquipmentButton.setMaximumSize(new Dimension(30, 30));
            updatePanel.add(updateEquipmentButton, BorderLayout.SOUTH);
            updateEquipmentButton.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if(selectedRow < 0){
                    JOptionPane.showMessageDialog(this, "Please select equipment to edit.");
                    return;
                }
                String equipmentId = (String) model.getValueAt(selectedRow, 0);
                Equipment selectedEquipment = equipments.stream().filter(equipment -> equipment.getEquipmentId().equals(equipmentId)).findFirst().orElse(null);
                if(selectedEquipment == null){
                    JOptionPane.showMessageDialog(this, "Equipment not found.");
                    return;
                }
                JPanel editPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.insets = new Insets(5,5,5,5);
                gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

                JLabel labelName = new JLabel("Name:");
                JTextField textFieldName = new JTextField(selectedEquipment.getName(), 20);
                textFieldName.setEditable(false);
                JLabel labelType = new JLabel("Type:");
                String[] types = {"Audio Recorder", "Camera", "Drone", "Laptop", "Lighting", "Projector", "VR Headset", "Other"};
                JComboBox<String> comboBoxType = new JComboBox<>(types);
                comboBoxType.setSelectedItem(selectedEquipment.getType());
                comboBoxType.setEditable(false);
                JLabel labelDescription = new JLabel("Description:");
                JTextField textFieldDescription = new JTextField(selectedEquipment.getDescription(), 20);
                textFieldDescription.setEditable(false);
                JLabel labelState = new JLabel("State:");
                String[] states = {"New", "Good", "Fair", "Poor"};
                JComboBox<String> comboBoxState = new JComboBox<>(states);
                comboBoxState.setSelectedItem(selectedEquipment.getState());

                JButton updateEquipmentSubmitButton = new JButton("Update Equipment");
                updateEquipmentSubmitButton.setBackground(Color.LIGHT_GRAY);
                updateEquipmentSubmitButton.setForeground(Color.DARK_GRAY);
                updateEquipmentSubmitButton.setPreferredSize(new Dimension(30, 30));
                updateEquipmentSubmitButton.setMaximumSize(new Dimension(30, 30));
                gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 0;
                editPanel.add(labelName, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(textFieldName, gridBagConstraints);
                gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 1;
                editPanel.add(labelType, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(comboBoxType, gridBagConstraints);
                gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 2;
                editPanel.add(labelDescription, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(textFieldDescription, gridBagConstraints);
                gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 3;
                editPanel.add(labelState, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(comboBoxState, gridBagConstraints);
                gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 4; gridBagConstraints.gridwidth = 2;
                editPanel.add(updateEquipmentSubmitButton, gridBagConstraints);

                updateEquipmentSubmitButton.addActionListener(ev -> {
                    try{
                        String name = InputValidator.validateEquipmentName(textFieldName.getText());
                        String type = (String) comboBoxType.getSelectedItem();
                        String description = InputValidator.validateEquipmentDescription(textFieldDescription.getText());
                        String state = (String) comboBoxState.getSelectedItem();
                        selectedEquipment.setName(name);
                        selectedEquipment.setType(type);
                        selectedEquipment.setDescription(description);
                        selectedEquipment.setState(state);
                        boolean success = equipmentController.updateEquipment(selectedEquipment, adminId);
                        if(success)
                            JOptionPane.showMessageDialog(this, "Equipment updated successfully.");
                        else
                            JOptionPane.showMessageDialog(this, "Failed to update equipment.");

                    }catch (exception.InvalidInputException ex){
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);

                    }

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
    }

    // --------------------------
    // Reservations Management Panel
    // --------------------------
    class ReservationsManagementPanel extends JPanel {
        private String adminId;
        private JPanel contentPanel;

        public ReservationsManagementPanel(String adminId) {
            this.adminId = adminId;

            JPanel sidebar = new JPanel();
            sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            sidebar.setBackground(Color.DARK_GRAY);

            JButton buttonApproval = createMenuItem("Approve/Reject Reservations");
            JButton buttonCheckOut = createMenuItem("Check-Out Equipment");
            JButton buttonCheckIn = createMenuItem("Check-In Equipment");

            sidebar.add(buttonApproval);
            sidebar.add(Box.createRigidArea(new Dimension(0, 50)));
            sidebar.add(buttonCheckOut);
            sidebar.add(Box.createRigidArea(new Dimension(0, 50)));
            sidebar.add(buttonCheckIn);

            contentPanel = new JPanel(new BorderLayout());
            JLabel placeholderLabel = new JLabel("Select an option from the sidebar.");
            placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
            placeholderLabel.setVerticalAlignment(SwingConstants.CENTER);
            contentPanel.add(placeholderLabel, BorderLayout.CENTER);



            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, contentPanel);
            splitPane.setDividerLocation(200);
            splitPane.setEnabled(false);
            splitPane.setDividerSize(0);


            setLayout(new BorderLayout());
            add(splitPane, BorderLayout.CENTER);

            buttonApproval.addActionListener(e -> {
                try {
                    loadApproval();
                } catch (DatabaseOperationException ex) {
                    throw new RuntimeException(ex);
                }
            });
            buttonCheckOut.addActionListener(e -> loadCheckOut());
            buttonCheckIn.addActionListener(e -> loadCheckIn());
        }

        private void loadApproval() throws DatabaseOperationException {
            ReservationController reservationController = new ReservationController();
            List<Reservation> reservations = reservationController.getAllReservations(adminId);
            JPanel updatePanel = new JPanel(new BorderLayout());
            String[] columnNames = {"Reservation ID", "User", "Equipment", "Reservation Date", "Return Date", "Status"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            for (Reservation reservation : reservations) {
                Object[] row = { reservation.getReservationId(), reservation.getUserId(), reservation.getEquipmentId(), reservation.getReservationDate(), reservation.getReturnDate(), reservation.getStatus() };
                model.addRow(row);
            }
            final JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            updatePanel.add(scrollPane, BorderLayout.CENTER);
            JButton updateRequestButton = new JButton("Update Selected Request");
            updateRequestButton.setBackground(Color.LIGHT_GRAY);
            updateRequestButton.setForeground(Color.DARK_GRAY);
            updateRequestButton.setPreferredSize(new Dimension(30, 30));
            updateRequestButton.setMaximumSize(new Dimension(30, 30));
            updatePanel.add(updateRequestButton, BorderLayout.SOUTH);
            updateRequestButton.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if(selectedRow < 0){
                    JOptionPane.showMessageDialog(this, "Please select request to update.");
                    return;
                }
                int reservationId = (int) model.getValueAt(selectedRow, 0);
                Reservation selectedReservation = reservations.stream().filter(reservation -> reservation.getReservationId() == reservationId).findFirst().orElse(null);
                if(selectedReservation == null){
                    JOptionPane.showMessageDialog(this, "Reservation not found.");
                    return;
                }
                JPanel editPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.insets = new Insets(5, 5, 5, 5);
                gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

                JLabel labelReservationId = new JLabel("Reservation ID:");
                JTextField textFieldReservationId = new JTextField(String.valueOf(selectedReservation.getReservationId()));
                textFieldReservationId.setEditable(false);
                JLabel labelUserName = new JLabel("Requesting Users Name:");
                JTextField textFieldUserName = new JTextField(selectedReservation.getUserId());
                textFieldUserName.setEditable(false);
                JLabel labelEquipmentName = new JLabel("Equipment Name:");
                JTextField textFieldEquipmentName = new JTextField(selectedReservation.getEquipmentId());
                textFieldEquipmentName.setEditable(false);
                JLabel labelReservationDate = new JLabel("Reservation Date:");
                Date reservationDate = selectedReservation.getReservationDate();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String reservationDateString = formatter.format(reservationDate);
                JTextField textFieldReservationDate = new JTextField(reservationDateString);
                textFieldReservationDate.setEditable(false);
                JLabel labelReturnDate = new JLabel("Return Date:");
                Date reservationReturnDate = selectedReservation.getReturnDate();
                String reservationReturnDateString;
                if (reservationReturnDate != null) {
                    reservationReturnDateString = formatter.format(reservationReturnDate);
                }
                else {
                    reservationReturnDateString = ""; // Or use an empty string ""
                }
                JTextField textFieldReturnDate = new JTextField(reservationReturnDateString);
                textFieldReturnDate.setEditable(false);
                JLabel labelStatus = new JLabel("Status:");
                String[] status = {"Approved", "Rejected"};
                JComboBox<String> comboBoxStatus = new JComboBox(status);
                comboBoxStatus.setSelectedItem(selectedReservation.getStatus());

                JButton updateRequestSubmitButton = new JButton("Update Request");
                updateRequestSubmitButton.setBackground(Color.LIGHT_GRAY);
                updateRequestSubmitButton.setForeground(Color.DARK_GRAY);
                updateRequestSubmitButton.setPreferredSize(new Dimension(30, 30));
                updateRequestSubmitButton.setMaximumSize(new Dimension(30, 30));
                gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 0;
                editPanel.add(labelReservationId, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(textFieldReservationId, gridBagConstraints);
                gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 1;
                editPanel.add(labelUserName, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(textFieldUserName, gridBagConstraints);
                gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 2;
                editPanel.add(labelEquipmentName, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(textFieldEquipmentName, gridBagConstraints);
                gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 3;
                editPanel.add(labelReservationDate, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(textFieldReservationDate, gridBagConstraints);
                gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 4;
                editPanel.add(labelReturnDate, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(textFieldReturnDate, gridBagConstraints);
                gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 5;
                editPanel.add(labelStatus, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(comboBoxStatus, gridBagConstraints);
                gridBagConstraints.gridx = 0; gridBagConstraints.gridy = 6; gridBagConstraints.gridwidth = 2;
                editPanel.add(updateRequestSubmitButton, gridBagConstraints);

                updateRequestSubmitButton.addActionListener(ev ->{
                    selectedReservation.setReservationId(Integer.parseInt(textFieldReservationId.getText()));
                    selectedReservation.setStatus((String)comboBoxStatus.getSelectedItem());

                    int reservationID = Integer.parseInt(textFieldReservationId.getText());
                    String reservationStatus = (String)comboBoxStatus.getSelectedItem();
                    String adminID = loggedInUser.getUserId();
                    boolean success = reservationController.updateReservationStatus(reservationID,reservationStatus,adminID);
                    if(success)
                        JOptionPane.showMessageDialog(this, "Successfully updated the reservation.");
                    else
                        JOptionPane.showMessageDialog(this, "Failed to update the reservation.");
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

        private void loadCheckOut() {
            // Retrieve pending checkouts from the back end.
            CheckoutController checkoutController = new CheckoutController();
            List<String> pendingCheckouts = checkoutController.getPendingCheckouts();
            if (pendingCheckouts.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No equipment is pending check-out.");
                return;
            }

            // Create a panel with a table to display pending checkouts.
            JPanel panel = new JPanel(new BorderLayout());
            // Define columns corresponding to the expected string format.
            String[] columnNames = {"Reservation ID", "Requester's Name", "Requested Equipment", "Reservation Date"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            // Split each string by " | " and add as a row.
            for (String detail : pendingCheckouts) {
                String[] parts = detail.split(" \\| ");
                if (parts.length == 4) {
                    model.addRow(parts);
                } else {
                    // Fallback if the format is unexpected.
                    model.addRow(new Object[]{detail, "", "", ""});
                }
            }

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Create a Check Out button.
            JButton checkOutButton = new JButton("Check Out Selected");
            checkOutButton.setBackground(Color.LIGHT_GRAY);
            checkOutButton.setForeground(Color.DARK_GRAY);
            checkOutButton.setPreferredSize(new Dimension(100, 30));
            panel.add(checkOutButton, BorderLayout.SOUTH);

            // When the button is clicked, process the selected row.
            checkOutButton.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(this, "Please select a pending checkout.");
                    return;
                }
                try {
                    // Extract reservation ID from the first column.
                    String resIdStr = (String) model.getValueAt(selectedRow, 0);
                    int resId = Integer.parseInt(resIdStr.trim());
                    String staffId = loggedInUser.getUserId();
                    if (staffId == null || staffId.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Error: Invalid staff ID.");
                        return;
                    }
                    boolean success = checkoutController.checkOutEquipment(resId, staffId);
                    if (success)
                        JOptionPane.showMessageDialog(this, "Equipment checked out successfully.");
                    else
                        JOptionPane.showMessageDialog(this, "Failed to check out equipment.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Error parsing reservation ID: " + ex.getMessage());
                }
            });

            contentPanel.removeAll();
            contentPanel.add(panel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }



        private void loadCheckIn() {
            // Retrieve checked-out equipment from the back end.
            CheckoutController checkoutController = new CheckoutController();
            List<String> checkedOutList = checkoutController.getCheckedOutEquipment();
            if (checkedOutList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No equipment is currently checked out.");
                return;
            }

            // Create a panel with a table to display checked-out equipment.
            JPanel panel = new JPanel(new BorderLayout());
            String[] columnNames = {"Reservation ID", "Requester's Name", "Requested Equipment", "Checked Out Date"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            for (String detail : checkedOutList) {
                String[] parts = detail.split(" \\| ");
                if (parts.length == 4) {
                    model.addRow(parts);
                } else {
                    model.addRow(new Object[]{detail, "", "", ""});
                }
            }

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Create a Check In button.
            JButton checkInButton = new JButton("Check In Selected");
            checkInButton.setBackground(Color.LIGHT_GRAY);
            checkInButton.setForeground(Color.DARK_GRAY);
            checkInButton.setPreferredSize(new Dimension(100, 30));
            panel.add(checkInButton, BorderLayout.SOUTH);

            // When the button is clicked, process the selected row.
            checkInButton.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(this, "Please select equipment to check in.");
                    return;
                }
                try {
                    // Extract reservation ID from the first column.
                    String resIdStr = (String) model.getValueAt(selectedRow, 0);
                    int resId = Integer.parseInt(resIdStr.trim());
                    String staffId = loggedInUser.getUserId();
                    if (staffId == null || staffId.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Error: Invalid staff ID.");
                        return;
                    }
                    // Prompt for the equipment state.
                    String equipmentState = JOptionPane.showInputDialog(this, "Enter Equipment State (Good, Fair, Poor):");
                    if (equipmentState == null || equipmentState.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Equipment state is required.");
                        return;
                    }
                    boolean success = checkoutController.checkInEquipment(resId, staffId, equipmentState);
                    if (success)
                        JOptionPane.showMessageDialog(this, "Equipment checked in successfully.");
                    else
                        JOptionPane.showMessageDialog(this, "Failed to check in equipment.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Error parsing reservation ID: " + ex.getMessage());
                }
            });

            contentPanel.removeAll();
            contentPanel.add(panel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }


    }
}
