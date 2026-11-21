package main;

import gui.MainMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * =====================================================================
 * ENTRY POINT APLIKASI
 * Menjalankan GUI Dashboard Utama (MainMenu).
 * =====================================================================
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Menggunakan tampilan native OS (Windows / Linux / Mac)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            new MainMenu().setVisible(true);
        });
    }
}
