package view;

import controller.*;
import model.User;
import model.Equipment;
import model.Reservation;
import exception.DatabaseOperationException;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.UIManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 * AdminFrame is the main GUI window for Admin users in the Media Equipment Rental System.
 * It provides multiple tabs to manage various aspects of the system including Home (dashboard),
 * Profile view, User management, Equipment management, and Reservations management.
 */
public class AdminFrame extends JFrame {
    private final User loggedInUser;
    private final JTabbedPane tabbedPane;
    private final Font bigFont = new Font("SansSerif", Font.PLAIN, 16);
    private final Font boldFont = new Font("SansSerif", Font.BOLD, 16);

    /**
     * Constructs an AdminFrame for the given user.
     *
     * @param user the authenticated User object representing an Admin.
     */
    public AdminFrame(User user) {
        this.loggedInUser = user;
        setTitle("Media Equipment Rental System - Admin Menu - " + user.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/view/icons/college.png"))).getImage());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a tabbed pane with custom UI
        tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createEmptyBorder());
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
                                              int x, int y, int w, int h, boolean isSelected) {
                Color selectedBg = UIManager.getColor("TabbedPane.selectedBackground");
                Color unselectedBg = UIManager.getColor("TabbedPane.unselectedBackground");
                if (selectedBg == null) selectedBg = new Color(0x435465); // fallback
                if (unselectedBg == null) unselectedBg = Color.DARK_GRAY;

                // Optionally, check for hover state if you add mouse listeners.
                // For now, simply paint based on selection.
                g.setColor(isSelected ? selectedBg : unselectedBg);
                g.fillRect(x, y, w, h);
            }

            @Override
            protected void paintText(Graphics g, int tabPlacement, Font font,
                                     FontMetrics metrics, int tabIndex, String title,
                                     Rectangle textRect, boolean isSelected) {
                g.setFont(font);
                Color selectedFg = UIManager.getColor("TabbedPane.selectedForeground");
                Color unselectedFg = UIManager.getColor("TabbedPane.unselectedForeground");
                if (selectedFg == null) selectedFg = Color.WHITE;
                if (unselectedFg == null) unselectedFg = Color.BLACK;
                g.setColor(isSelected ? selectedFg : unselectedFg);
                g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
            }

            @Override
            protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects,
                                               int tabIndex, Rectangle iconRect, Rectangle textRect,
                                               boolean isSelected) {
                // Do nothing to remove the dotted focus outline
            }

            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                // Do nothing to remove the “blue border” around the content area
            }

            @Override
            protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
                // Increase tab height to accommodate a 64px icon plus text.
                return 60; // adjust as needed
            }

            @Override
            protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
                int totalWidth = tabPane.getWidth();
                int tabCount = tabPane.getTabCount();
                if (tabCount > 0 && totalWidth > 0) {
                    // Divide the total available width equally among all tabs.
                    return totalWidth / tabCount;
                }
                // Fallback if width is not available yet.
                return super.calculateTabWidth(tabPlacement, tabIndex, metrics);
            }
        });

        // Add tabs with icons.
        tabbedPane.addTab("Home",
                new ImageIcon(Objects.requireNonNull(getClass().getResource("/view/icons/home.png"))),
                new HomePanel(loggedInUser));
        tabbedPane.addTab("View Profile",
                new ImageIcon(Objects.requireNonNull(getClass().getResource("/view/icons/profile.png"))),
                new ViewProfilePanel(loggedInUser));
        tabbedPane.addTab("User Management",
                new ImageIcon(Objects.requireNonNull(getClass().getResource("/view/icons/user.png"))),
                new UserManagementPanel(loggedInUser.getUserId()));
        tabbedPane.addTab("Equipment Management",
                new ImageIcon(Objects.requireNonNull(getClass().getResource("/view/icons/equipment.png"))),
                new EquipmentManagementPanel(loggedInUser.getUserId()));
        tabbedPane.addTab("Reservations Management",
                new ImageIcon(Objects.requireNonNull(getClass().getResource("/view/icons/reservation.png"))),
                new ReservationsManagementPanel(loggedInUser.getUserId()));

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Bottom panel with a log-out button.
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Log out");
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

        // Add a change listener to refresh tabs that implement Refreshable.
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Component selected = tabbedPane.getSelectedComponent();
                if (selected instanceof Refreshable) {
                    try {
                        ((Refreshable) selected).refresh();
                    } catch (DatabaseOperationException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }
    // =========================
    // Inner Classes for Panels
    // =========================

    /**
     * HomePanel displays an overview of system statistics using charts.
     * It includes charts for equipment states, checked-out equipment, reservation status, and user reservations.
     * The panel automatically refreshes its charts every 3 seconds.
     */
    class HomePanel extends JPanel {
        private final ChartPanel equipmentStateChartPanel;
        private final ChartPanel checkedOutChartPanel;
        private final ChartPanel reservationsStatusChartPanel;
        private final ChartPanel userReservationsChartPanel;
        private final User loggedInUser; // Logged-in user for context

        /**
         * Constructs a HomePanel for the specified user.
         *
         * @param user the logged-in User.
         */
        public HomePanel(User user) {
            this.loggedInUser = user;
            // Use a (2 rows x 2 columns) GridLayout with 10px gaps.
            setLayout(new GridLayout(2, 2, 2, 2));
            int chartSize = 300;

            // Equipment States Chart (Bar Chart)
            equipmentStateChartPanel = new ChartPanel(createEquipmentStateChart());
            equipmentStateChartPanel.setPreferredSize(new Dimension(chartSize, chartSize));
            add(equipmentStateChartPanel);

            // Checked Out Equipment Chart (Pie Chart)
            checkedOutChartPanel = new ChartPanel(createCheckedOutChart());
            checkedOutChartPanel.setPreferredSize(new Dimension(chartSize, chartSize));
            add(checkedOutChartPanel);

            // Reservations Status Chart (Bar Chart: Pending vs Approved)
            reservationsStatusChartPanel = new ChartPanel(createReservationsStatusChart());
            reservationsStatusChartPanel.setPreferredSize(new Dimension(chartSize, chartSize));
            add(reservationsStatusChartPanel);

            // User Reservations Chart (Bar Chart)
            userReservationsChartPanel = new ChartPanel(createUserReservationsOverTimeChart());
            userReservationsChartPanel.setPreferredSize(new Dimension(chartSize, chartSize));
            add(userReservationsChartPanel);

            // Set up a Timer to update all Charts every 3 seconds.
            Timer timer = new Timer(3000, e -> updateAllCharts());
            timer.start();
        }

        /**
         * Updates all charts by recreating them.
         */
        private void updateAllCharts() {
            equipmentStateChartPanel.setChart(createEquipmentStateChart());
            checkedOutChartPanel.setChart(createCheckedOutChart());
            reservationsStatusChartPanel.setChart(createReservationsStatusChart());
            userReservationsChartPanel.setChart(createUserReservationsOverTimeChart());
            // Optionally, force a repaint:
            equipmentStateChartPanel.repaint();
            checkedOutChartPanel.repaint();
            reservationsStatusChartPanel.repaint();
            userReservationsChartPanel.repaint();
        }

        /**
         * Creates a bar chart displaying the counts of equipment in different states.
         *
         * @return a JFreeChart object representing the equipment states.
         */
        private JFreeChart createEquipmentStateChart() {
            EquipmentController equipmentController= new EquipmentController();
            // Retrieve all equipment filtered by the user's role.
            List<Equipment> equipments = equipmentController.getAllEquipment(loggedInUser.getRole());
            // Count each state.
            Map<String, Integer> stateCounts = new HashMap<>();
            for (Equipment eq : equipments) {
                String state = eq.getState();
                if (state != null) {
                    stateCounts.put(state, stateCounts.getOrDefault(state, 0) + 1);
                }
            }
            // Create a dataset with one common category "Equipment" and four series for each state.
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            dataset.addValue(stateCounts.getOrDefault("New", 0), "New", "Equipment");
            dataset.addValue(stateCounts.getOrDefault("Good", 0), "Good", "Equipment");
            dataset.addValue(stateCounts.getOrDefault("Fair", 0), "Fair", "Equipment");
            dataset.addValue(stateCounts.getOrDefault("Poor", 0), "Poor", "Equipment");

            // Create the bar chart.
            JFreeChart chart = ChartFactory.createBarChart("Equipment States", "State", "Count", dataset);

            // Enable and display data labels.
            chart.getCategoryPlot().getRenderer().setDefaultItemLabelGenerator(new org.jfree.chart.labels.StandardCategoryItemLabelGenerator());
            chart.getCategoryPlot().getRenderer().setDefaultItemLabelsVisible(true);

            // Set chart and plot background.
            chart.setBackgroundPaint(Color.WHITE);
            chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
            chart.getCategoryPlot().setOutlineVisible(false);

            // Set different colors for each series.
            chart.getCategoryPlot().getRenderer().setSeriesPaint(0, Color.BLUE);    // "New"
            chart.getCategoryPlot().getRenderer().setSeriesPaint(1, Color.GREEN);   // "Good"
            chart.getCategoryPlot().getRenderer().setSeriesPaint(2, Color.ORANGE);  // "Fair"
            chart.getCategoryPlot().getRenderer().setSeriesPaint(3, Color.RED);     // "Poor"

            return chart;
        }


        /**
         * Creates a pie chart comparing the count of checked-out equipment against available equipment.
         *
         * @return a JFreeChart object representing checked-out equipment.
         */
        private JFreeChart createCheckedOutChart() {
            CheckoutController cc = new CheckoutController();
            List<String> checkedOut = cc.getCheckedOutEquipment();
            int checkedOutCount = checkedOut.size();
            EquipmentController equipmentController= new EquipmentController();
            List<Equipment> equipments = equipmentController.getAllEquipment(loggedInUser.getRole());
            int total = equipments.size();
            int notCheckedOut = total - checkedOutCount;
            DefaultPieDataset dataset = new DefaultPieDataset();
            dataset.setValue("Checked Out", checkedOutCount);
            dataset.setValue("Not Checked Out", notCheckedOut);
            JFreeChart chart = ChartFactory.createPieChart("Equipment Checked Out", dataset, true, true, false);
            // (You can add a StandardPieSectionLabelGenerator here for more detailed labels.)

            // Set chart and plot background to white.
            chart.setBackgroundPaint(Color.WHITE);
            // Get the plot as a PiePlot and set its properties.
            org.jfree.chart.plot.PiePlot plot = (org.jfree.chart.plot.PiePlot) chart.getPlot();
            plot.setBackgroundPaint(Color.WHITE);
            plot.setOutlineVisible(false);

            // To set section colors
            plot.setSectionPaint("Checked Out", Color.GREEN);
            plot.setSectionPaint("Not Checked Out", Color.YELLOW);
            return chart;
        }

        /**
         * Creates a bar chart comparing pending and approved reservations.
         *
         * @return a JFreeChart object representing reservation statuses.
         */
        private JFreeChart createReservationsStatusChart() {
            ReservationController resController= new ReservationController();
            // Use the current user's ID to filter reservations.
            List<Reservation> reservations = resController.getAllReservations(loggedInUser.getUserId());
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

            // Set chart and plot background to white.
            chart.setBackgroundPaint(Color.WHITE);
            chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
            chart.getCategoryPlot().setOutlineVisible(false);

            // Change the bar color for series 0 to orange.
            chart.getCategoryPlot().getRenderer().setSeriesPaint(0, Color.YELLOW);
            chart.getCategoryPlot().getRenderer().setSeriesPaint(1, Color.GREEN);
            return chart;
        }

        /**
         * Creates a bar chart showing the number of reservations per user.
         *
         * @return a JFreeChart object representing user reservations.
         */
        private JFreeChart createUserReservationsOverTimeChart() {
            ReservationController resController= new ReservationController();
            List<Reservation> reservations = resController.getAllReservations(loggedInUser.getUserId());
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

            // Set chart and plot background to white.
            chart.setBackgroundPaint(Color.WHITE);
            chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
            chart.getCategoryPlot().setOutlineVisible(false);

            // Change the bar color for series 0 to orange.
            chart.getCategoryPlot().getRenderer().setSeriesPaint(0, Color.GREEN);
            return chart;
        }
    }

    /**
     * ViewProfilePanel displays the logged-in user's profile information.
     */
    class ViewProfilePanel extends JPanel {
        /**
         * Constructs a ViewProfilePanel for the specified user.
         *
         * @param user the logged-in User.
         */
        public ViewProfilePanel(User user) {
            setLayout(new GridBagLayout());
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new Insets(10, 10, 10, 10);
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

            JLabel iconLabel = new JLabel(new ImageIcon(getClass().getResource("/view/icons/user-profile.png")));
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.anchor = GridBagConstraints.CENTER;
            add(iconLabel, gridBagConstraints);

            //Resets gridwidth for subsequent components
            gridBagConstraints.gridwidth = 1;


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

            // For each label and text field:
            labelUserId.setFont(boldFont);
            textFieldUserId.setFont(bigFont);
            labelName.setFont(boldFont);
            textFieldName.setFont(bigFont);
            labelEmail.setFont(boldFont);
            textFieldEmail.setFont(bigFont);
            labelRole.setFont(boldFont);
            textFieldRole.setFont(bigFont);

            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            add(labelUserId, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            add(textFieldUserId, gridBagConstraints);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            add(labelName, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            add(textFieldName, gridBagConstraints);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 3;
            add(labelEmail, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            add(textFieldEmail, gridBagConstraints);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 4;
            add(labelRole, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            add(textFieldRole, gridBagConstraints);
        }
    }

    /**
     * Creates a JButton styled as a menu item with rollover and pressed effects.
     *
     * @param text the button label.
     * @return the styled JButton.
     */
    private JButton createMenuItem(String text, String iconPath) {
        JButton button = new JButton(text);
        // Let the button use the default Look and Feel ButtonUI.
        // button.setUI(new BasicButtonUI());  // Ensure this line is removed.

        if (iconPath != null) {
            button.setIcon(new ImageIcon(getClass().getResource(iconPath)));
        }

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(150, 100));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        button.setFocusPainted(true);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setRolloverEnabled(true);
        button.setBorderPainted(false);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        return button;
    }

    /**
     * UserManagementPanel provides an interface for managing users, including viewing,
     * adding, updating, and deleting users.
     */
    class UserManagementPanel extends JPanel implements Refreshable {
        private final String adminId;
        private final JPanel contentPanel;

        /**
         * Constructs a UserManagementPanel for the given admin.
         *
         * @param adminId the ID of the admin.
         */
        public UserManagementPanel(String adminId) {
            this.adminId = adminId;


            // Create sidebar with menu buttons.
            JPanel sidebar = new JPanel();
            sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            sidebar.setBackground(Color.DARK_GRAY);


            JButton buttonViewAllUsers = createMenuItem("View All Users", "/view/icons/view-user.png");
            JButton buttonAddUser = createMenuItem("Add New User", "/view/icons/add-user.png");
            JButton buttonUpdateUser = createMenuItem("Update User","/view/icons/update-user.png");
            JButton buttonDeleteUser = createMenuItem("Delete User","/view/icons/delete-user.png");

            sidebar.add(Box.createRigidArea(new Dimension(0,100)));
            sidebar.add(buttonViewAllUsers);
            sidebar.add(Box.createRigidArea(new Dimension(0,100)));
            sidebar.add(buttonAddUser);
            sidebar.add(Box.createRigidArea(new Dimension(0,100)));
            sidebar.add(buttonUpdateUser);
            sidebar.add(Box.createRigidArea(new Dimension(0,100)));
            sidebar.add(buttonDeleteUser);

            // Create content panel with a placeholder.
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

            // Set up action listeners.
            buttonViewAllUsers.addActionListener(e -> {
                try {
                    loadViewAllUsers();
                } catch (DatabaseOperationException ex) {
                    throw new RuntimeException(ex);
                }
            });
            buttonAddUser.addActionListener(e -> loadAddUser());
            buttonUpdateUser.addActionListener(e -> {
                try {
                    loadUpdateUser();
                } catch (DatabaseOperationException ex) {
                    throw new RuntimeException(ex);
                }
            });
            buttonDeleteUser.addActionListener(e -> {
                try {
                    loadDeleteUser();
                } catch (DatabaseOperationException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        /**
         * Refreshes the panel by reloading the view all users table.
         *
         * @throws DatabaseOperationException if an error occurs during data retrieval.
         */
        public void refresh() throws DatabaseOperationException {
            loadViewAllUsers();
        }

        /**
         * Loads all users from the database and displays them in a table.
         *
         * @throws DatabaseOperationException if a database error occurs.
         */
        private void loadViewAllUsers() throws DatabaseOperationException {
            final UserController uc = new UserController();
            final List<User> users = uc.getAllUsers("Admin");
            String[] columnNames = {"User ID", "Email", "Name", "Role"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0){
                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
            };

            for (User user : users) {
                Object[] row = { user.getUserId(), user.getEmail(), user.getName(), user.getRole() };
                model.addRow(row);
            }

            JTable table = new JTable(model);
            table.setFont(bigFont);
            table.setRowHeight(20);
            table.getTableHeader().setFont(boldFont);

            JScrollPane scrollPane = new JScrollPane(table);

            // Center text in some columns
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

            contentPanel.removeAll();
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        /**
         * Loads the "Add User" form into the panel.
         */
        private void loadAddUser() {
            // Main panel for the Add User form.
            JPanel addPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gridBagConstraint = new GridBagConstraints();
            gridBagConstraint.insets = new Insets(5, 5, 5, 5);
            gridBagConstraint.fill = GridBagConstraints.HORIZONTAL;

            // Row 0: Role selection
            JLabel labelRole = new JLabel("Role:");
            String[] roles = {"Student", "Lecturer", "Admin", "MediaStaff"};
            JComboBox<String> comboBoxRole = new JComboBox<>(roles);
            gridBagConstraint.gridx = 0;
            gridBagConstraint.gridy = 0;
            addPanel.add(labelRole, gridBagConstraint);
            gridBagConstraint.gridx = 1;
            addPanel.add(comboBoxRole, gridBagConstraint);

            // Row 1: Email
            JLabel labelEmail = new JLabel("Email:");
            JTextField textFieldEmail = new JTextField(20);
            gridBagConstraint.gridx = 0;
            gridBagConstraint.gridy = 1;
            addPanel.add(labelEmail, gridBagConstraint);
            gridBagConstraint.gridx = 1;
            addPanel.add(textFieldEmail, gridBagConstraint);

            // Row 2: Name
            JLabel labelName = new JLabel("Name:");
            JTextField textFieldName = new JTextField(20);
            gridBagConstraint.gridx = 0;
            gridBagConstraint.gridy = 2;
            addPanel.add(labelName, gridBagConstraint);
            gridBagConstraint.gridx = 1;
            addPanel.add(textFieldName, gridBagConstraint);

            // Row 3: Password
            JLabel labelPassword = new JLabel("Password:");
            JPasswordField pfPassword = new JPasswordField(20);
            gridBagConstraint.gridx = 0;
            gridBagConstraint.gridy = 3;
            addPanel.add(labelPassword, gridBagConstraint);
            gridBagConstraint.gridx = 1;
            addPanel.add(pfPassword, gridBagConstraint);

            // Row 4: Extra panel for additional fields (Department, Course, and Year for Student)
            JPanel extraPanel = new JPanel(new GridBagLayout());
            gridBagConstraint.gridx = 0;
            gridBagConstraint.gridy = 4;
            gridBagConstraint.gridwidth = 2;
            addPanel.add(extraPanel, gridBagConstraint);

            // Get full list of departments from MethodsUtil.
            final String[] departments = controller.MethodsUtil.getDepartments();
            // Holder for the Year text field (if needed).
            final JTextField[] yearFieldHolder = new JTextField[1];

            // Runnable to update extraPanel based on selected role.
            Runnable updateExtraPanel = () -> {
                extraPanel.removeAll();
                GridBagConstraints gridBagConstraintExtra = new GridBagConstraints();
                gridBagConstraintExtra.insets = new Insets(5, 5, 5, 5);
                gridBagConstraintExtra.fill = GridBagConstraints.HORIZONTAL;
                gridBagConstraintExtra.gridx = 0;
                gridBagConstraintExtra.gridy = 0;
                String selectedRole = (String) comboBoxRole.getSelectedItem();
                if ("Student".equalsIgnoreCase(selectedRole)) {
                    // For Student, add Department, Course, and Year.
                    JLabel labelDept = new JLabel("Department:");
                    JComboBox<String> comboBoxDept = new JComboBox<>(departments);
                    gridBagConstraintExtra.gridx = 0;
                    gridBagConstraintExtra.gridy = 0;
                    extraPanel.add(labelDept, gridBagConstraintExtra);
                    gridBagConstraintExtra.gridx = 1;
                    extraPanel.add(comboBoxDept, gridBagConstraintExtra);

                    JLabel labelCourse = new JLabel("Course:");
                    String selectedDept = (String) comboBoxDept.getSelectedItem();
                    String[] courses = controller.MethodsUtil.getCoursesForDepartment(selectedDept);
                    JComboBox<String> cbCourse = new JComboBox<>(courses);
                    gridBagConstraintExtra.gridx = 0;
                    gridBagConstraintExtra.gridy = 1;
                    extraPanel.add(labelCourse, gridBagConstraintExtra);
                    gridBagConstraintExtra.gridx = 1;
                    extraPanel.add(cbCourse, gridBagConstraintExtra);

                    // Add listener to update courses when department changes.
                    comboBoxDept.addActionListener(e -> {
                        String dept = (String) comboBoxDept.getSelectedItem();
                        String[] newCourses = controller.MethodsUtil.getCoursesForDepartment(dept);
                        cbCourse.setModel(new DefaultComboBoxModel<>(newCourses));
                    });

                    // Add Year field.
                    JLabel labelYear = new JLabel("Year:");
                    String[] years = {"1", "2", "3", "4"};
                    JComboBox<String> comboBoxYear = new JComboBox<>(years);
                    comboBoxYear.setSelectedItem("1"); // default to "1" or adjust as needed
                    gridBagConstraintExtra.gridx = 0;
                    gridBagConstraintExtra.gridy = 2;
                    extraPanel.add(labelYear, gridBagConstraintExtra);
                    gridBagConstraintExtra.gridx = 1;
                    extraPanel.add(comboBoxYear, gridBagConstraintExtra);
                } else if ("Lecturer".equalsIgnoreCase(selectedRole)) {
                    // For Lecturer, add only Department.
                    JLabel labelDept = new JLabel("Department:");
                    JComboBox<String> comboBoxDept = new JComboBox<>(departments);
                    gridBagConstraintExtra.gridx = 0;
                    gridBagConstraintExtra.gridy = 0;
                    extraPanel.add(labelDept, gridBagConstraintExtra);
                    gridBagConstraintExtra.gridx = 1;
                    extraPanel.add(comboBoxDept, gridBagConstraintExtra);
                }
                extraPanel.revalidate();
                extraPanel.repaint();
            };

            // Initial update.
            updateExtraPanel.run();
            comboBoxRole.addActionListener(e -> updateExtraPanel.run());

            // Row 5: Submit button.
            JButton addUserButton = new JButton("Add User");
            addUserButton.setPreferredSize(new Dimension(30, 30));
            addUserButton.setMaximumSize(new Dimension(30, 30));
            addUserButton.setFocusPainted(false);
            gridBagConstraint.gridx = 0; gridBagConstraint.gridy = 5; gridBagConstraint.gridwidth = 2;
            addPanel.add(addUserButton, gridBagConstraint);

            addUserButton.addActionListener(e -> {
                try {
                    // Validate email and name using the input validator.
                    String email = InputValidator.validateEmail(textFieldEmail.getText(), (String) comboBoxRole.getSelectedItem());
                    String name = InputValidator.validateName(textFieldName.getText());
                    String role = (String) comboBoxRole.getSelectedItem();
                    String password = new String(pfPassword.getPassword()).trim();
                    if (password.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Password must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // Create the new user.
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setRole(role);
                    newUser.setPassword(controller.PasswordUtils.hashPassword(password));

                    // Process extra fields for Student and Lecturer.
                    if ("Student".equalsIgnoreCase(role)) {
                        // Expect two JComboBox components in extraPanel: first for Department, second for Course.
                        Component[] components = extraPanel.getComponents();
                        String department = "";
                        String course = "";
                        int comboCount = 0;
                        for (Component comp : components) {
                            if (comp instanceof JComboBox) {
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
                        // Retrieve and validate the year.
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
                        // For Lecturer, expect one JComboBox for Department.
                        for (Component comp : extraPanel.getComponents()) {
                            if (comp instanceof JComboBox) {
                                JComboBox<String> cb = (JComboBox<String>) comp;
                                newUser.setDepartment((String) cb.getSelectedItem());
                            }
                        }
                    }

                    // Call the UserController to add the user.
                    UserController userController = new UserController();
                    boolean success = userController.addUser(newUser, adminId);
                    if (success)
                        JOptionPane.showMessageDialog(this, "User added successfully.");
                    else
                        JOptionPane.showMessageDialog(this, "Failed to add user.");
                } catch (exception.InvalidInputException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            contentPanel.removeAll();
            contentPanel.add(addPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        /**
         * Loads the "Update User" form panel.
         *
         * @throws DatabaseOperationException if a database error occurs.
         */
        private void loadUpdateUser() throws DatabaseOperationException {
            final UserController userController = new UserController();
            final List<User> users = userController.getAllUsers("Admin");
            JPanel updatePanel = new JPanel(new BorderLayout());
            String[] columnNames = {"User ID", "Email", "Name", "Role"};
            final DefaultTableModel model = new DefaultTableModel(columnNames, 0){
                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
            };
            for (User user : users) {
                Object[] row = { user.getUserId(), user.getEmail(), user.getName(), user.getRole() };
                model.addRow(row);
            }
            final JTable table = new JTable(model);
            table.setFont(bigFont);
            table.setRowHeight(20);
            table.getTableHeader().setFont(boldFont);

            // Center text in some columns
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

            JScrollPane scrollPane = new JScrollPane(table);
            
            updatePanel.add(scrollPane, BorderLayout.CENTER);
            JButton updateUserButton = new JButton("Update Selected User");
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
                User selectedUser = userController.getUserById(userId);
                if(selectedUser == null){
                    JOptionPane.showMessageDialog(this, "User not found.");
                    return;
                }
                JPanel editPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.insets = new Insets(5, 5, 5, 5);
                gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

                JLabel labelEmail = new JLabel("Email:");
                JTextField textFieldEmail = new JTextField(selectedUser.getEmail(), 20);
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                editPanel.add(labelEmail, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(textFieldEmail, gridBagConstraints);


                JLabel labelName = new JLabel("Name:");
                JTextField textFieldName = new JTextField(selectedUser.getName(), 20);
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                editPanel.add(labelName, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(textFieldName, gridBagConstraints);

                JLabel labelRole = new JLabel("Role:");
                JTextField textFieldRole = new JTextField(selectedUser.getRole(), 20);
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 2;
                editPanel.add(labelRole, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(textFieldRole, gridBagConstraints);
                textFieldRole.setEditable(false); // Role not editable in update

                final String PASSWORD_PLACEHOLDER = "********";
                JLabel labelPassword = new JLabel("Password:");
                JPasswordField passwordField = new JPasswordField( PASSWORD_PLACEHOLDER,20);
                passwordField.setEchoChar('*');
                // A focus listener so that when the field gains focus, if it contains
                // the placeholder it's cleared, and when it loses focus and is empty, the placeholder is restored.
                passwordField.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        String current = new String(passwordField.getPassword());
                        if (current.equals(PASSWORD_PLACEHOLDER)) {
                            passwordField.setText("");
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        String current = new String(passwordField.getPassword());
                        if (current.isEmpty()) {
                            passwordField.setText(PASSWORD_PLACEHOLDER);
                        }
                    }
                });
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 3;
                editPanel.add(labelPassword, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(passwordField, gridBagConstraints);

                JPanel extraPanel = new JPanel(new GridBagLayout());
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 4;
                gridBagConstraints.gridwidth = 2;
                editPanel.add(extraPanel, gridBagConstraints);

                JButton updateUserSubmitButton = new JButton("Update User");
                updateUserButton.setPreferredSize(new Dimension(30, 30));
                updateUserButton.setMaximumSize(new Dimension(30, 30));
                updateUserSubmitButton.setFocusPainted(false);
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 5;
                gridBagConstraints.gridwidth = 2;
                editPanel.add(updateUserSubmitButton, gridBagConstraints);


                // Get full list of departments from MethodsUtil.
                final String[] departments = MethodsUtil.getDepartments();
                // Holder for the Year text field (if needed).
                final JTextField[] yearFieldHolder = new JTextField[1];

                // Runnable to update extraPanel based on selected role.
                Runnable updateExtraPanel = () -> {
                    extraPanel.removeAll();
                    GridBagConstraints gridBagConstraintsExtra = new GridBagConstraints();
                    gridBagConstraintsExtra.insets = new Insets(5, 5, 5, 5);
                    gridBagConstraintsExtra.fill = GridBagConstraints.HORIZONTAL;
                    gridBagConstraintsExtra.gridx = 0;
                    gridBagConstraintsExtra.gridy = 0;

                    String usersRole = textFieldRole.getText();
                    if ("Student".equalsIgnoreCase(usersRole)) {
                        // For Student, add Department, Course, and Year.
                        JLabel labelDept = new JLabel("Department:");
                        JComboBox<String> comboBoxDept = new JComboBox<>(departments);
                        gridBagConstraintsExtra.gridx = 0;
                        gridBagConstraintsExtra.gridy = 0;
                        extraPanel.add(labelDept, gridBagConstraintsExtra);
                        gridBagConstraintsExtra.gridx = 1;
                        extraPanel.add(comboBoxDept, gridBagConstraintsExtra);

                        JLabel labelCourse = new JLabel("Course:");
                        String selectedDept = (String) comboBoxDept.getSelectedItem();
                        String[] courses = MethodsUtil.getCoursesForDepartment(selectedDept);
                        JComboBox<String> cbCourse = new JComboBox<>(courses);
                        gridBagConstraintsExtra.gridx = 0;
                        gridBagConstraintsExtra.gridy = 1;
                        extraPanel.add(labelCourse, gridBagConstraintsExtra);
                        gridBagConstraintsExtra.gridx = 1;
                        extraPanel.add(cbCourse, gridBagConstraintsExtra);

                        // Add listener to update courses when department changes.
                        comboBoxDept.addActionListener(ec -> {
                            String dept = (String) comboBoxDept.getSelectedItem();
                            String[] newCourses = MethodsUtil.getCoursesForDepartment(dept);
                            cbCourse.setModel(new DefaultComboBoxModel<>(newCourses));
                        });

                        // Add Year field.
                        JLabel labelYear = new JLabel("Year:");
                        String[] years = {"1","2","3","4"};
                        JComboBox<String> comboBoxYear = new JComboBox<>(years);
                        String currentYear;
                        if(selectedUser.getYear() != null){
                            currentYear = selectedUser.getYear().toString();
                        } else {
                            currentYear = "1";
                        }
                        comboBoxYear.setSelectedItem(currentYear);
                        gridBagConstraintsExtra.gridx = 0;
                        gridBagConstraintsExtra.gridy = 2;
                        extraPanel.add(labelYear, gridBagConstraintsExtra);
                        gridBagConstraintsExtra.gridx = 1;
                        extraPanel.add(comboBoxYear, gridBagConstraintsExtra);
                    } else if ("Lecturer".equalsIgnoreCase(usersRole)) {
                        // For Lecturer, add only Department.
                        JLabel labelDept = new JLabel("Department:");
                        JComboBox<String> comboBoxDept = new JComboBox<>(departments);
                        gridBagConstraintsExtra.gridx = 0;
                        gridBagConstraintsExtra.gridy = 0;
                        extraPanel.add(labelDept, gridBagConstraintsExtra);
                        gridBagConstraintsExtra.gridx = 1;
                        extraPanel.add(comboBoxDept, gridBagConstraintsExtra);
                    }
                    extraPanel.revalidate();
                    extraPanel.repaint();
                };

                // Initial update.
                updateExtraPanel.run();

                updateUserSubmitButton.addActionListener(ev -> {

                    try{
                        String email = InputValidator.validateEmail(textFieldEmail.getText(), textFieldRole.getText());
                        String name = InputValidator.validateName(textFieldName.getText());
                        String updatedPassword = new String(passwordField.getPassword()).trim();
                        if (!updatedPassword.equals(PASSWORD_PLACEHOLDER) && !updatedPassword.isEmpty()) {
                            selectedUser.setPassword(PasswordUtils.hashPassword(updatedPassword));
                        }


                        selectedUser.setEmail(email);
                        selectedUser.setName(name);

                        if("Student".equalsIgnoreCase(selectedUser.getRole())) {
                            Component[] components = extraPanel.getComponents();
                            String department = "";
                            String course = "";
                            String yearString = "";
                            int comboCount = 0;
                            for(Component component : components) {
                                if(component instanceof JComboBox) {
                                    JComboBox<String> comboBox = (JComboBox<String>) component;
                                    if(comboCount == 0){
                                        department = (String) comboBox.getSelectedItem();
                                    } else if (comboCount == 1){
                                        course = (String) comboBox.getSelectedItem();
                                    } else if (comboCount == 2){
                                        yearString = (String) comboBox.getSelectedItem();
                                    }
                                    comboCount++;
                                }
                            }
                            selectedUser.setDepartment(department);
                            selectedUser.setCourse(course);
                            try {
                                int year = Integer.parseInt(yearString);
                                selectedUser.setYear(year);
                            } catch (NumberFormatException numberException) {
                                JOptionPane.showMessageDialog(this, "Invalid year selected", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            }else if("Lecturer".equalsIgnoreCase(selectedUser.getRole())){
                                for(Component component : extraPanel.getComponents()) {
                                    if(component instanceof JComboBox) {
                                        JComboBox<String> comboBox = (JComboBox<String>) component;
                                        selectedUser.setDepartment((String) comboBox.getSelectedItem());
                                    }
                                }
                            }

                        boolean success = userController.updateUser(selectedUser, adminId);
                        if(success)
                            JOptionPane.showMessageDialog(this, "User updated successfully.");
                        else
                            JOptionPane.showMessageDialog(this, "Failed to update user.");
                    }catch(exception.InvalidInputException ex){
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

        /**
         * Loads the "Delete User" panel.
         *
         * @throws DatabaseOperationException if a database error occurs.
         */
        private void loadDeleteUser() throws DatabaseOperationException {
            final UserController userController = new UserController();
            final List<User> users = userController.getAllUsers("Admin");
            JPanel deletePanel = new JPanel(new BorderLayout());
            String[] columnNames = {"User ID", "Email", "Name", "Role"};
            final DefaultTableModel model = new DefaultTableModel(columnNames, 0){
                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
            };
            for (User user : users) {
                Object[] row = { user.getUserId(), user.getEmail(), user.getName(), user.getRole() };
                model.addRow(row);
            }
            final JTable table = new JTable(model);
            table.setFont(bigFont);
            table.setRowHeight(20);
            table.getTableHeader().setFont(boldFont);

            // Center text in some columns
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

            JScrollPane scrollPane = new JScrollPane(table);
            
            deletePanel.add(scrollPane, BorderLayout.CENTER);
            JButton deleteUserButton = new JButton("Delete Selected User");
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
                    boolean success = userController.deleteUser(userId, adminId);
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
        }
    }

    /**
     * EquipmentManagementPanel provides an interface for managing equipment records,
     * including viewing, adding, updating, and deleting equipment.
     */
    class EquipmentManagementPanel extends JPanel implements Refreshable {
        private final String adminId;
        private final JPanel contentPanel;

        /**
         * Constructs an EquipmentManagementPanel for the given admin.
         *
         * @param adminId the ID of the admin.
         */
        public EquipmentManagementPanel(String adminId) {
            this.adminId = adminId;

            JPanel sidebar = new JPanel();
            sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            sidebar.setBackground(Color.DARK_GRAY);

            JButton buttonViewAllEquipment = createMenuItem("View All Equipment", "/view/icons/view.png");
            JButton buttonViewByType = createMenuItem("View Equipment by Type", "/view/icons/view.png");
            JButton buttonAddEquipment = createMenuItem("Add New Equipment", "/view/icons/add.png");
            JButton buttonUpdateEquipment = createMenuItem("Update Equipment","/view/icons/update.png");
            JButton buttonDeleteEquipment = createMenuItem("Delete Equipment","/view/icons/delete.png");
            sidebar.add(Box.createRigidArea(new Dimension(0,50)));
            sidebar.add(buttonViewAllEquipment);
            sidebar.add(Box.createRigidArea(new Dimension(0,50)));
            sidebar.add(buttonViewByType);
            sidebar.add(Box.createRigidArea(new Dimension(0,50)));
            sidebar.add(buttonAddEquipment);
            sidebar.add(Box.createRigidArea(new Dimension(0,50)));
            sidebar.add(buttonUpdateEquipment);
            sidebar.add(Box.createRigidArea(new Dimension(0,50)));
            sidebar.add(buttonDeleteEquipment);

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

        /**
         * Refreshes the equipment management panel by reloading all equipment.
         *
         * @throws DatabaseOperationException if a database error occurs.
         */
        public void refresh() throws DatabaseOperationException {
            loadViewAllEquipment();
        }

        /**
         * Loads all equipment records into a table view.
         *
         * @throws DatabaseOperationException if a database error occurs.
         */
        private void loadViewAllEquipment() throws DatabaseOperationException {
            EquipmentController equipmentController = new EquipmentController();
            List<Equipment> equipments = equipmentController.getAllEquipment("Admin");
            String[] columnNames = {"Equipment ID", "Name", "Type", "Description", "Status", "State"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0){
                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
            };
            for (Equipment equipment : equipments) {
                Object[] row = { equipment.getEquipmentId(), equipment.getName(), equipment.getType(), equipment.getDescription(), equipment.getStatus(), equipment.getState() };
                model.addRow(row);
            }
            JTable table = new JTable(model);


            // Adjust column widths as needed (in pixels). For example:
            table.getColumnModel().getColumn(0).setPreferredWidth(120); // Equipment ID
            table.getColumnModel().getColumn(1).setPreferredWidth(250); // Name
            table.getColumnModel().getColumn(2).setPreferredWidth(100); // Type
            table.getColumnModel().getColumn(3).setPreferredWidth(350); // Description
            table.getColumnModel().getColumn(4).setPreferredWidth(100); // Status
            table.getColumnModel().getColumn(5).setPreferredWidth(80);  // State

            table.setFont(bigFont);
            table.setRowHeight(20);
            table.getTableHeader().setFont(boldFont);

            JScrollPane scrollPane = new JScrollPane(table);
            

            // Center text in some columns
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

            contentPanel.removeAll();
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        /**
         * Loads equipment records filtered by type.
         *
         * @throws DatabaseOperationException if a database error occurs.
         */
        private void loadViewEquipmentByType() throws DatabaseOperationException {
            String[] types = {"Audio Recorder", "Camera", "Drone", "Laptop", "Lighting", "Projector", "VR Headset", "Other"};
            String type = (String) JOptionPane.showInputDialog(this, "Select Equipment Type:", "Equipment Type", JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
            if(type == null) return;
            EquipmentController equipmentController = new EquipmentController();
            List<Equipment> equipments = equipmentController.getEquipmentByType(type, loggedInUser.getRole());
            String[] columnNames = {"Equipment ID", "Name", "Type", "Description", "Status", "State"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0){
                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
            };
            for (Equipment equipment : equipments) {
                Object[] row = { equipment.getEquipmentId(), equipment.getName(), equipment.getType(), equipment.getDescription(), equipment.getStatus(), equipment.getState() };
                model.addRow(row);
            }
            JTable table = new JTable(model);
            table.setFont(bigFont);
            table.setRowHeight(20);
            table.getTableHeader().setFont(boldFont);

            JScrollPane scrollPane = new JScrollPane(table);
            

            // Center text in some columns
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

            contentPanel.removeAll();
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        /**
         * Loads the "Add Equipment" form panel.
         * (Implementation should follow similar patterns as in UserManagementPanel.)
         */
        private void loadAddEquipment() {
            JPanel addPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.insets = new Insets(5,5,5,5);
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

            JLabel labelName = new JLabel("Name:");
            JTextField textFieldName = new JTextField(20);
            JLabel labelType = new JLabel("Type:");
            String[] types = {"Audio Recorder", "Camera", "Drone", "Laptop", "Lighting", "Projector", "VR Headset", "Other"};
            JComboBox<String> comboBoxType = new JComboBox<>(types);
            JLabel labelDescription = new JLabel("Description:");
            JTextField textFieldDescription = new JTextField(20);
            JLabel labelState = new JLabel("State:");
            String[] states = {"New", "Good", "Fair", "Poor"};
            JComboBox<String> comboBoxState = new JComboBox<>(states);

            JButton addEquipmentButton = new JButton("Add Equipment");
            addEquipmentButton.setPreferredSize(new Dimension(30, 30));
            addEquipmentButton.setMaximumSize(new Dimension(30, 30));
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            addPanel.add(labelName, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            addPanel.add(textFieldName, gridBagConstraints);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            addPanel.add(labelType, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            addPanel.add(comboBoxType, gridBagConstraints);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            addPanel.add(labelDescription, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            addPanel.add(textFieldDescription, gridBagConstraints);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 3;
            addPanel.add(labelState, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            addPanel.add(comboBoxState, gridBagConstraints);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 4;
            gridBagConstraints.gridwidth = 2;
            addPanel.add(addEquipmentButton, gridBagConstraints);

            addEquipmentButton.addActionListener(e -> {
                try{
                    String name = InputValidator.validateEquipmentName(textFieldName.getText());
                    String type = (String) comboBoxType.getSelectedItem();
                    String description = InputValidator.validateEquipmentDescription(textFieldDescription.getText());
                    String state = (String) comboBoxState.getSelectedItem();
                    if(name.isEmpty()){
                        JOptionPane.showMessageDialog(this, "Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Equipment equipment = new Equipment();
                    equipment.setName(name);
                    equipment.setType(type);
                    equipment.setDescription(description);
                    equipment.setState(state);
                    equipment.setStatus("Available");
                    EquipmentController equipmentController = new EquipmentController();
                    boolean success = equipmentController.addEquipment(equipment, adminId);
                    if(success)
                        JOptionPane.showMessageDialog(this, "Equipment added successfully.");
                    else
                        JOptionPane.showMessageDialog(this, "Failed to add equipment.");
                }catch(exception.InvalidInputException ex){
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
                }

            });

            contentPanel.removeAll();
            contentPanel.add(addPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        /**
         * Loads the "Update Equipment" form panel.
         *
         * @throws DatabaseOperationException if a database error occurs.
         */
        private void loadUpdateEquipment() throws DatabaseOperationException {
            EquipmentController equipmentController = new EquipmentController();
            List<Equipment> equipments = equipmentController.getAllEquipment("Admin");
            JPanel updatePanel = new JPanel(new BorderLayout());
            String[] columnNames = {"Equipment ID", "Name", "Type", "Description", "Status", "State"};
            final DefaultTableModel model = new DefaultTableModel(columnNames, 0){
                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
            };
            for (Equipment equipment : equipments) {
                if ("Available".equalsIgnoreCase(equipment.getStatus())) {  // Only include available equipment
                    Object[] row = { equipment.getEquipmentId(), equipment.getName(), equipment.getType(),
                            equipment.getDescription(), equipment.getStatus(), equipment.getState() };
                    model.addRow(row);
                }
            }
            final JTable table = new JTable(model);
            table.setFont(bigFont);
            table.setRowHeight(20);
            table.getTableHeader().setFont(boldFont);

            // Center text in some columns
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

            JScrollPane scrollPane = new JScrollPane(table);
            
            updatePanel.add(scrollPane, BorderLayout.CENTER);
            JButton updateEquipmentButton = new JButton("Update Selected Equipment");
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
                JLabel labelType = new JLabel("Type:");
                String[] types = {"Audio Recorder", "Camera", "Drone", "Laptop", "Lighting", "Projector", "VR Headset", "Other"};
                JComboBox<String> comboBoxType = new JComboBox<>(types);
                comboBoxType.setSelectedItem(selectedEquipment.getType());
                JLabel labelDescription = new JLabel("Description:");
                JTextField textFieldDescription = new JTextField(selectedEquipment.getDescription(), 20);
                JLabel labelState = new JLabel("State:");
                String[] states = {"New", "Good", "Fair", "Poor"};
                JComboBox<String> comboBoxState = new JComboBox<>(states);
                comboBoxState.setSelectedItem(selectedEquipment.getState());

                JButton updateEquipmentSubmitButton = new JButton("Update Equipment");
                updateEquipmentSubmitButton.setPreferredSize(new Dimension(30, 30));
                updateEquipmentSubmitButton.setMaximumSize(new Dimension(30, 30));
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                editPanel.add(labelName, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(textFieldName, gridBagConstraints);
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                editPanel.add(labelType, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(comboBoxType, gridBagConstraints);
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 2;
                editPanel.add(labelDescription, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(textFieldDescription, gridBagConstraints);
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 3;
                editPanel.add(labelState, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(comboBoxState, gridBagConstraints);
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 4;
                gridBagConstraints.gridwidth = 2;
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

        /**
         * Loads the "Delete Equipment" panel.
         *
         * @throws DatabaseOperationException if a database error occurs.
         */
        private void loadDeleteEquipment() throws DatabaseOperationException {
            EquipmentController equipmentController = new EquipmentController();
            List<Equipment> equipments = equipmentController.getAllEquipment("Admin");
            JPanel deletePanel = new JPanel(new BorderLayout());
            String[] columnNames = {"Equipment ID", "Name", "Type", "Description", "Status", "State"};
            final DefaultTableModel model = new DefaultTableModel(columnNames, 0){
                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
            };
            for (Equipment equipment : equipments) {
                if("Available".equalsIgnoreCase(equipment.getStatus())){
                Object[] row = { equipment.getEquipmentId(), equipment.getName(), equipment.getType(), equipment.getDescription(), equipment.getStatus(), equipment.getState() };
                model.addRow(row);
                }
            }
            final JTable table = new JTable(model);
            table.setFont(bigFont);
            table.setRowHeight(20);
            table.getTableHeader().setFont(boldFont);

            // Center text in some columns
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

            JScrollPane scrollPane = new JScrollPane(table);
            
            deletePanel.add(scrollPane, BorderLayout.CENTER);
            JButton deleteEquipmentButton = new JButton("Delete Selected Equipment");
            deleteEquipmentButton.setPreferredSize(new Dimension(30, 30));
            deleteEquipmentButton.setMaximumSize(new Dimension(30, 30));
            deletePanel.add(deleteEquipmentButton, BorderLayout.SOUTH);
            deleteEquipmentButton.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if(selectedRow < 0){
                    JOptionPane.showMessageDialog(this, "Please select equipment to delete.");
                    return;
                }
                String equipmentId = (String) model.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete equipment " + equipmentId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if(confirm == JOptionPane.YES_OPTION){
                    boolean success = equipmentController.deleteEquipment(equipmentId, adminId);
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

    /**
     * ReservationsManagementPanel provides an interface for managing reservations.
     * It allows the admin to approve or reject reservations as well as process equipment check-out and check-in.
     */
    class ReservationsManagementPanel extends JPanel implements Refreshable {
        private final String adminId;
        private final JPanel contentPanel;

        /**
         * Constructs a ReservationsManagementPanel for the given admin.
         *
         * @param adminId the ID of the admin.
         */
        public ReservationsManagementPanel(String adminId) {
            this.adminId = adminId;

            JPanel sidebar = new JPanel();
            sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            sidebar.setBackground(Color.DARK_GRAY);

            JButton buttonApproval = createMenuItem("Approve/Reject Reservations","/view/icons/reservations.png");
            JButton buttonCheckOut = createMenuItem("Check-Out Equipment","/view/icons/check-out.png");
            JButton buttonCheckIn = createMenuItem("Check-In Equipment","/view/icons/check-in.png");

            sidebar.add(Box.createRigidArea(new Dimension(0,150)));
            sidebar.add(buttonApproval);
            sidebar.add(Box.createRigidArea(new Dimension(0,100)));
            sidebar.add(buttonCheckOut);
            sidebar.add(Box.createRigidArea(new Dimension(0,100)));
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

        /**
         * Refreshes the reservations management panel by reloading the approval tab.
         *
         * @throws DatabaseOperationException if a database error occurs.
         */
        public void refresh() throws DatabaseOperationException {
            loadApproval();
        }

        /**
         * Loads the reservation approval panel where the admin can update reservation statuses.
         *
         * @throws DatabaseOperationException if a database error occurs.
         */
        private void loadApproval() throws DatabaseOperationException {
            ReservationController reservationController = new ReservationController();
            List<Reservation> reservations = reservationController.getAllReservations(adminId);
            JPanel updatePanel = new JPanel(new BorderLayout());
            String[] columnNames = {"Reservation ID", "User", "Equipment", "Reservation Date", "Return Date", "Status"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0){
                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
            };
            for (Reservation reservation : reservations) {
                Object[] row = { reservation.getReservationId(), reservation.getUserId(), reservation.getEquipmentId(), reservation.getReservationDate(), reservation.getReturnDate(), reservation.getStatus() };
                model.addRow(row);
            }
            final JTable table = new JTable(model);
            table.setFont(bigFont);
            table.setRowHeight(20);
            table.getTableHeader().setFont(boldFont);
            JScrollPane scrollPane = new JScrollPane(table);
            
            // Center text in some columns
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

            updatePanel.add(scrollPane, BorderLayout.CENTER);
            JButton updateRequestButton = new JButton("Update Selected Request");
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
                updateRequestSubmitButton.setPreferredSize(new Dimension(30, 30));
                updateRequestSubmitButton.setMaximumSize(new Dimension(30, 30));
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                editPanel.add(labelReservationId, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(textFieldReservationId, gridBagConstraints);
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                editPanel.add(labelUserName, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(textFieldUserName, gridBagConstraints);
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 2;
                editPanel.add(labelEquipmentName, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(textFieldEquipmentName, gridBagConstraints);
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 3;
                editPanel.add(labelReservationDate, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(textFieldReservationDate, gridBagConstraints);
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 4;
                editPanel.add(labelReturnDate, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(textFieldReturnDate, gridBagConstraints);
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 5;
                editPanel.add(labelStatus, gridBagConstraints);
                gridBagConstraints.gridx = 1;
                editPanel.add(comboBoxStatus, gridBagConstraints);
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 6;
                gridBagConstraints.gridwidth = 2;
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

        /**
         * Loads the check-out panel for processing equipment check-outs.
         */
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
            DefaultTableModel model = new DefaultTableModel(columnNames, 0){
                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
            };

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
            table.setFont(bigFont);
            table.setRowHeight(20);
            table.getTableHeader().setFont(boldFont);
            JScrollPane scrollPane = new JScrollPane(table);
            
            panel.add(scrollPane, BorderLayout.CENTER);

            // Create a Check-Out button.
            JButton checkOutButton = new JButton("Check Out Selected");
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

        /**
         * Loads the check-in panel for processing equipment check-ins.
         */
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
            DefaultTableModel model = new DefaultTableModel(columnNames, 0){
                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
            };

            for (String detail : checkedOutList) {
                String[] parts = detail.split(" \\| ");
                if (parts.length == 4) {
                    model.addRow(parts);
                } else {
                    model.addRow(new Object[]{detail, "", "", ""});
                }
            }

            JTable table = new JTable(model);
            table.setFont(bigFont);
            table.setRowHeight(20);
            table.getTableHeader().setFont(boldFont);
            JScrollPane scrollPane = new JScrollPane(table);
            
            panel.add(scrollPane, BorderLayout.CENTER);

            // Create a Check In button.
            JButton checkInButton = new JButton("Check In Selected");
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
                    String[] states = {"Good", "Fair", "Poor"};
                    String equipmentState = (String) JOptionPane.showInputDialog(
                            this,
                            "Select Equipment State:",
                            "Equipment State",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            states,
                            states[0]
                    );
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
