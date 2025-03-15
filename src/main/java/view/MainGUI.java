package view;

import javax.swing.SwingUtilities;

/**
 * Main entry point for the Media Equipment Rental System GUI application.
 *
 * This class initializes and displays the LoginFrame on the Event Dispatch Thread.
 */
public class MainGUI {
    /**
     * Main method that starts the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
