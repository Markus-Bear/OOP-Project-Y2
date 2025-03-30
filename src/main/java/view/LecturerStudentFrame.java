package view;

import controller.EquipmentController;
import controller.Refreshable;
import controller.ReservationController;
import model.Equipment;
import model.Reservation;
import model.User;
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
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import javax.swing.JFormattedTextField.AbstractFormatter;

/**
 * LecturerStudentFrame provides the main window for Lecturer/Student users.
 * It includes two tabs: one for displaying user information (Home) and
 * another for managing reservations (Reserve Equipment and View Reservations).
 * The look and feel is designed to be consistent with the AdminFrame.
 */
public class LecturerStudentFrame extends JFrame {
    private User loggedInUser;
    private JTabbedPane tabbedPane;
    private Font bigFont = new Font("SansSerif", Font.PLAIN, 16);
    private Font boldFont = new Font("SansSerif", Font.BOLD, 16);
    private ReservationController reservationController = new ReservationController();

    /**
     * Constructs a LecturerStudentFrame for the specified user.
     *
     * @param user the logged-in user.
     */
    public LecturerStudentFrame(User user) {
        this.loggedInUser = user;
        setTitle("Media Equipment Rental System - " + user.getRole() + " Menu - " + user.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/view/icons/college.png")).getImage());
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a tabbed pane with custom UI (similar to AdminFrame).
        tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createEmptyBorder());
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(Graphics graphic, int tabPlacement,
                                              int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                graphic.setColor(isSelected ? Color.DARK_GRAY : Color.LIGHT_GRAY);
                graphic.fillRect(x, y, w, h);
            }

            @Override
            protected void paintText(Graphics graphic, int tabPlacement, Font font,
                                     FontMetrics metrics, int tabIndex, String title,
                                     Rectangle textRect, boolean isSelected) {
                graphic.setFont(font);
                graphic.setColor(isSelected ? Color.WHITE : Color.BLACK);
                graphic.drawString(title, textRect.x, textRect.y + metrics.getAscent());
            }

            @Override
            protected void paintFocusIndicator(Graphics graphic, int tabPlacement, Rectangle[] rects,
                                               int tabIndex, Rectangle iconRect, Rectangle textRect,
                                               boolean isSelected) {
                // Remove focus indicator.
            }

            @Override
            protected void paintContentBorder(Graphics graphic, int tabPlacement, int selectedIndex) {
                // Remove content border.
            }

            @Override
            protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
                return 60; // Adjust as needed.
            }

            @Override
            protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
                int totalWidth = tabPane.getWidth();
                int tabCount = tabPane.getTabCount();
                if (tabCount > 0 && totalWidth > 0) {
                    return totalWidth / tabCount;
                }
                return super.calculateTabWidth(tabPlacement, tabIndex, metrics);
            }
        });

        // Add two tabs: Home and Reservations.
        tabbedPane.addTab("Home",
                new ImageIcon(getClass().getResource("/view/icons/home.png")),
                new HomePanel());
        tabbedPane.addTab("Reservations",
                new ImageIcon(getClass().getResource("/view/icons/reservation.png")),
                new ReservationsPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Bottom panel with a logout button.
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

        // Add a change listener to refresh tabs that implement Refreshable.
        tabbedPane.addChangeListener(e -> {
            Component selected = tabbedPane.getSelectedComponent();
            if (selected instanceof Refreshable) {
                try {
                    ((Refreshable) selected).refresh();
                } catch (DatabaseOperationException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    /**
     * HomePanel displays the logged-in user's information.
     * It is similar to the ViewProfilePanel in the AdminFrame.
     */
    class HomePanel extends JPanel {
        public HomePanel() {
            setLayout(new GridBagLayout());
            setBackground(Color.WHITE);
            GridBagConstraints gridBagContraint = new GridBagConstraints();
            gridBagContraint.insets = new Insets(10, 10, 10, 10);
            gridBagContraint.fill = GridBagConstraints.HORIZONTAL;

            // Optional profile icon.
            JLabel iconLabel = new JLabel(new ImageIcon(getClass().getResource("/view/icons/user-profile.png")));
            gridBagContraint.gridx = 0;
            gridBagContraint.gridy = 0;
            gridBagContraint.gridwidth = 2;
            gridBagContraint.anchor = GridBagConstraints.CENTER;
            add(iconLabel, gridBagContraint);
            gridBagContraint.gridwidth = 1; // Reset

            // Basic user details.
            JLabel labelUserId = new JLabel("User ID:");
            JTextField textFieldUserId = new JTextField(loggedInUser.getUserId(), 20);
            textFieldUserId.setEditable(false);

            JLabel labelName = new JLabel("Name:");
            JTextField textFieldName = new JTextField(loggedInUser.getName(), 20);
            textFieldName.setEditable(false);

            JLabel labelEmail = new JLabel("Email:");
            JTextField textFieldEmail = new JTextField(loggedInUser.getEmail(), 20);
            textFieldEmail.setEditable(false);

            JLabel labelRole = new JLabel("Role:");
            JTextField textFieldRole = new JTextField(loggedInUser.getRole(), 20);
            textFieldRole.setEditable(false);

            // Set fonts and colours.
            Font bigFont = new Font("SansSerif", Font.PLAIN, 16);
            Font boldFont = new Font("SansSerif", Font.BOLD, 16);
            labelUserId.setFont(boldFont);
            labelName.setFont(boldFont);
            labelEmail.setFont(boldFont);
            labelRole.setFont(boldFont);
            textFieldUserId.setFont(bigFont);
            textFieldName.setFont(bigFont);
            textFieldEmail.setFont(bigFont);
            textFieldRole.setFont(bigFont);
            Color labelColor = new Color(0x435465);
            labelUserId.setForeground(labelColor);
            labelName.setForeground(labelColor);
            labelEmail.setForeground(labelColor);
            labelRole.setForeground(labelColor);

            gridBagContraint.gridx = 0;
            gridBagContraint.gridy = 1;
            add(labelUserId, gridBagContraint);
            gridBagContraint.gridx = 1;
            add(textFieldUserId, gridBagContraint);

            gridBagContraint.gridx = 0;
            gridBagContraint.gridy = 2;
            add(labelName, gridBagContraint);
            gridBagContraint.gridx = 1;
            add(textFieldName, gridBagContraint);

            gridBagContraint.gridx = 0;
            gridBagContraint.gridy = 3;
            add(labelEmail, gridBagContraint);
            gridBagContraint.gridx = 1;
            add(textFieldEmail, gridBagContraint);

            gridBagContraint.gridx = 0;
            gridBagContraint.gridy = 4;
            add(labelRole, gridBagContraint);
            gridBagContraint.gridx = 1;
            add(textFieldRole, gridBagContraint);

            // Conditionally display additional fields based on role.
            if (loggedInUser.getRole().equalsIgnoreCase("Student")) {
                JLabel labelDept = new JLabel("Department:");
                JTextField textFieldDept = new JTextField(loggedInUser.getDepartment(), 20);
                textFieldDept.setEditable(false);

                JLabel labelCourse = new JLabel("Course:");
                JTextField textFieldCourse = new JTextField(loggedInUser.getCourse(), 20);
                textFieldCourse.setEditable(false);

                JLabel labelYear = new JLabel("Year:");
                JTextField textFieldYear = new JTextField(String.valueOf(loggedInUser.getYear()), 20);
                textFieldYear.setEditable(false);

                labelDept.setFont(boldFont);
                labelCourse.setFont(boldFont);
                labelYear.setFont(boldFont);
                textFieldDept.setFont(bigFont);
                textFieldCourse.setFont(bigFont);
                textFieldYear.setFont(bigFont);
                labelDept.setForeground(labelColor);
                labelCourse.setForeground(labelColor);
                labelYear.setForeground(labelColor);

                gridBagContraint.gridx = 0;
                gridBagContraint.gridy = 5;
                add(labelDept, gridBagContraint);
                gridBagContraint.gridx = 1;
                add(textFieldDept, gridBagContraint);

                gridBagContraint.gridx = 0;
                gridBagContraint.gridy = 6;
                add(labelCourse, gridBagContraint);
                gridBagContraint.gridx = 1;
                add(textFieldCourse, gridBagContraint);

                gridBagContraint.gridx = 0;
                gridBagContraint.gridy = 7;
                add(labelYear, gridBagContraint);
                gridBagContraint.gridx = 1;
                add(textFieldYear, gridBagContraint);
            } else if (loggedInUser.getRole().equalsIgnoreCase("Lecturer")) {
                JLabel labelDept = new JLabel("Department:");
                JTextField textFieldDept = new JTextField(loggedInUser.getDepartment(), 20);
                textFieldDept.setEditable(false);

                labelDept.setFont(boldFont);
                textFieldDept.setFont(bigFont);
                labelDept.setForeground(labelColor);

                gridBagContraint.gridx = 0;
                gridBagContraint.gridy = 5;
                add(labelDept, gridBagContraint);
                gridBagContraint.gridx = 1;
                add(textFieldDept, gridBagContraint);
            }
        }
    }

    /**
     * ReservationsPanel allows the user to reserve equipment and view their reservations.
     * It contains a sidebar with two options: Reserve Equipment and View Reservations.
     */
    class ReservationsPanel extends JPanel implements Refreshable {
        private JPanel contentPanel;

        /**
         * Constructs a ReservationsPanel and sets up the sidebar and content area.
         */
        public ReservationsPanel() {
            setLayout(new BorderLayout());

            // Sidebar with menu buttons.
            JPanel sidebar = new JPanel();
            sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            sidebar.setBackground(Color.DARK_GRAY);
            sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

            JButton reserveEquipButton = createMenuItem("Reserve Equipment", "/view/icons/equipment.png");
            JButton viewReservationsButton = createMenuItem("View Reservations", "/view/icons/reservation.png");

            sidebar.add(Box.createVerticalStrut(50));
            sidebar.add(reserveEquipButton);
            sidebar.add(Box.createVerticalStrut(30));
            sidebar.add(viewReservationsButton);

            contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(Color.WHITE);
            // Load Reserve Equipment panel by default.
            loadReserveEquipmentPanel();

            // Create split pane for sidebar and content.
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, contentPanel);
            splitPane.setDividerLocation(200);
            splitPane.setEnabled(false);
            splitPane.setDividerSize(0);
            add(splitPane, BorderLayout.CENTER);

            reserveEquipButton.addActionListener(e -> loadReserveEquipmentPanel());
            viewReservationsButton.addActionListener(e -> loadViewReservationsPanel());
        }

        /**
         * Refreshes the ReservationsPanel.
         *
         * @throws DatabaseOperationException if an error occurs during data retrieval.
         */
        @Override
        public void refresh() throws DatabaseOperationException {
            loadReserveEquipmentPanel();
        }

        /**
         * Loads the panel that displays available equipment for reservation.
         */
        private void loadReserveEquipmentPanel() {
            contentPanel.removeAll();
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.WHITE);

            EquipmentController equipmentController = new EquipmentController();
            List<Equipment> availableEquipment = equipmentController.getEquipmentByStatus("Available");

            String[] columnNames = {"Equipment ID", "Name", "Type", "Description"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            for (Equipment eq : availableEquipment) {
                model.addRow(new Object[]{eq.getEquipmentId(), eq.getName(), eq.getType(), eq.getDescription()});
            }
            JTable table = new JTable(model);
            table.setFont(bigFont);
            table.setRowHeight(20);
            table.getTableHeader().setFont(boldFont);
            table.getColumnModel().getColumn(0).setPreferredWidth(120);
            table.getColumnModel().getColumn(1).setPreferredWidth(250);
            table.getColumnModel().getColumn(2).setPreferredWidth(100);
            table.getColumnModel().getColumn(3).setPreferredWidth(350);

            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Reserve Equipment button.
            JButton reserveButton = new JButton("Reserve Equipment");
            reserveButton.setBackground(Color.LIGHT_GRAY);
            reserveButton.setForeground(Color.DARK_GRAY);
            reserveButton.setFont(boldFont);
            reserveButton.setPreferredSize(new Dimension(0, 40));
            reserveButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            panel.add(reserveButton, BorderLayout.SOUTH);

            reserveButton.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(this, "Please select an equipment to reserve.");
                    return;
                }
                String equipmentId = (String) model.getValueAt(selectedRow, 0);
                loadReservationForm(equipmentId);
            });

            contentPanel.add(panel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        /**
         * Loads the reservation form using JDatePicker for the specified equipment.
         *
         * @param equipmentId the ID of the equipment to reserve.
         */
        private void loadReservationForm(String equipmentId) {
            contentPanel.removeAll();
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(Color.WHITE);
            GridBagConstraints gridBagContraint = new GridBagConstraints();
            gridBagContraint.insets = new Insets(10, 10, 10, 10);
            gridBagContraint.fill = GridBagConstraints.HORIZONTAL;

            JLabel dateLabel = new JLabel("Select Reservation Date:");
            gridBagContraint.gridx = 0;
            gridBagContraint.gridy = 0;
            formPanel.add(dateLabel, gridBagContraint);

            // Set up JDatePicker.
            UtilDateModel dateModel = new UtilDateModel();
            dateModel.setSelected(true);
            Properties datePickerProperty = new Properties();
            datePickerProperty.put("text.today", "Today");
            datePickerProperty.put("text.month", "Month");
            datePickerProperty.put("text.year", "Year");
            JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, datePickerProperty);
            JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
            gridBagContraint.gridx = 1;
            gridBagContraint.gridy = 0;
            formPanel.add(datePicker, gridBagContraint);

            // Reserve button spans full width.
            JButton reserveButton = new JButton("Reserve Equipment");
            reserveButton.setBackground(Color.LIGHT_GRAY);
            reserveButton.setForeground(Color.DARK_GRAY);
            reserveButton.setFont(boldFont);
            gridBagContraint.gridx = 0;
            gridBagContraint.gridy = 1;
            gridBagContraint.gridwidth = 2;
            formPanel.add(reserveButton, gridBagContraint);

            reserveButton.addActionListener(e -> {
                Date selectedDate = (Date) datePicker.getModel().getValue();
                if (selectedDate == null) {
                    JOptionPane.showMessageDialog(this, "Please select a reservation date.", "Date Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Ensure the selected date is today or in the future.
                Date today = new Date();
                if (selectedDate.before(today)) {
                    JOptionPane.showMessageDialog(this, "Reservation date cannot be in the past.", "Date Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String userId = loggedInUser.getUserId();
                boolean success = reservationController.requestReservation(userId, equipmentId, new java.sql.Date(selectedDate.getTime()));
                if (success) {
                    JOptionPane.showMessageDialog(this, "Reservation request submitted successfully!");
                    loadViewReservationsPanel();
                } else {
                    JOptionPane.showMessageDialog(this, "Reservation request failed.");
                }
            });

            contentPanel.add(formPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        /**
         * Loads the panel that displays the user's reservations.
         */
        private void loadViewReservationsPanel() {
            contentPanel.removeAll();
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.WHITE);

            ReservationController reservationController = new ReservationController();
            List<Reservation> reservations = reservationController.getUserReservations(loggedInUser.getUserId());

            String[] columnNames = {"Reservation ID", "Equipment", "Reservation Date", "Status"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            for (Reservation res : reservations) {
                model.addRow(new Object[]{
                        res.getReservationId(),
                        res.getEquipmentId(),
                        res.getReservationDate(),
                        res.getStatus()
                });
            }
            JTable table = new JTable(model);
            table.setFont(bigFont);
            table.setRowHeight(20);
            table.getTableHeader().setFont(boldFont);

            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);

            contentPanel.add(panel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    /**
     * Creates a JButton styled as a menu item with rollover and pressed effects.
     *
     * @param text the button label.
     * @param iconPath the path to the icon.
     * @return the styled JButton.
     */
    private JButton createMenuItem(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setUI(new BasicButtonUI());
        if (iconPath != null) {
            button.setIcon(new ImageIcon(getClass().getResource(iconPath)));
        }
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(0, 100));
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(true);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setRolloverEnabled(true);
        button.setBorderPainted(false);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        final Color defaultColor = Color.DARK_GRAY;
        final Color hoverColor = new Color(64, 64, 64);
        final Color pressedColor = new Color(96, 96, 96);
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

    /**
     * DateLabelFormatter formats the date for the JDatePicker.
     */
    private static class DateLabelFormatter extends AbstractFormatter {
        private final String datePattern = "dd-MM-yyyy";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parse(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }
}
