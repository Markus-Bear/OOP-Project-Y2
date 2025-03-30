package view;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Color;
/**
 * Main entry point for the Media Equipment Rental System GUI application.
 * <p>
 * This class initializes and displays the LoginFrame on the Event Dispatch Thread.
 */
public class MainGUI {
    /**
     * Main method that starts the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set FlatDarkLaf look and feel
                UIManager.setLookAndFeel(new FlatDarkLaf());

                // Configure the title pane (header) to remain dark with white text.
                UIManager.put("TitlePane.background", new Color(0x435465));
                UIManager.put("TitlePane.foreground", Color.WHITE);
                UIManager.put("TitlePane.activeBackground", new Color(0x435465));
                UIManager.put("TitlePane.inactiveBackground", new Color(0x435465));

                // Set the defaults for content components to have a white background and black text.
                UIManager.put("Panel.background", Color.WHITE);
                UIManager.put("Label.foreground", Color.BLACK);
                UIManager.put("TextField.background", Color.WHITE);
                UIManager.put("TextField.foreground", Color.BLACK);
                UIManager.put("PasswordField.background", Color.WHITE);
                UIManager.put("PasswordField.foreground", Color.BLACK);
                UIManager.put("Table.background", Color.WHITE);
                UIManager.put("Table.foreground", Color.BLACK);
                UIManager.put("Table.selectionBackground", new Color(0xCCCCCC));
                UIManager.put("Table.selectionForeground", Color.BLACK);
                UIManager.put("TableHeader.background", Color.WHITE);
                UIManager.put("TableHeader.foreground", Color.BLACK);
                UIManager.put("OptionPane.background", Color.WHITE);
                UIManager.put("OptionPane.foreground", Color.BLACK);
                UIManager.put("OptionPane.messageForeground", Color.BLACK);
                UIManager.put("ComboBox.background", Color.WHITE);
                UIManager.put("ComboBox.foreground", Color.BLACK);
                UIManager.put("ComboBox.selectionBackground", new Color(0xDDDDDD));
                UIManager.put("ComboBox.selectionForeground", Color.BLACK);
            } catch (Exception ex) {
                System.err.println("Failed to initialize Look and Feel: " + ex.getMessage());
            }
            new LoginFrame().setVisible(true);
        });
    }
}
