package koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 * =====================================================================
 * TUGAS: ANGGOTA 1
 * Deskripsi: Mengelola koneksi database MySQL melalui JDBC.
 * =====================================================================
 */
public class Koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/db_booking_lapangan?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = ""; // Default XAMPP kosong

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Gagal terhubung ke Database!\n" + e.getMessage() + "\n\nPastikan MySQL di XAMPP sudah berjalan.", 
                "Error Koneksi Database", 
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
