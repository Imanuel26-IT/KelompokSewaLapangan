package gui.member;

import koneksi.Koneksi;
import gui.MainMenu;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

/**
 * =====================================================================
 * TUGAS: ANGGOTA 3
 * Deskripsi: Form Login autentikasi Member & Admin. Query SQL langsung di sini.
 * =====================================================================
 */
public class FormLogin extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRole;
    private JButton btnLogin, btnBukaRegistrasi;

    public FormLogin() {
        setTitle("Login Pengguna - Anggota 3");
        setSize(420, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(142, 68, 173));
        JLabel lblJudul = new JLabel("LOGIN SISTEM SEWA LAPANGAN");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 16));
        lblJudul.setForeground(Color.WHITE);
        panelHeader.add(lblJudul);
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);
        cbRole = new JComboBox<>(new String[]{"Member", "Admin"});

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; panelForm.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; panelForm.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Masuk Sebagai:"), gbc);
        gbc.gridx = 1; panelForm.add(cbRole, gbc);

        btnLogin = new JButton("Masuk / Login");
        btnLogin.setBackground(new Color(142, 68, 173));
        btnLogin.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panelForm.add(btnLogin, gbc);

        btnBukaRegistrasi = new JButton("Belum punya akun? Registrasi Member");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panelForm.add(btnBukaRegistrasi, gbc);

        add(panelForm, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> prosesLogin());
        btnBukaRegistrasi.addActionListener(e -> {
            new FormRegistrasi().setVisible(true);
        });
    }

    /**
     * QUERY LANGSUNG: Validasi autentikasi pengguna ke database (Tanpa DAO).
     */
    private void prosesLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String role = cbRole.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password tidak boleh kosong!");
            return;
        }

        String table = role.equalsIgnoreCase("Admin") ? "admin" : "member";
        String sql = "SELECT * FROM " + table + " WHERE username = ? AND password = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return;
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nama = role.equalsIgnoreCase("Admin") ? rs.getString("nama_admin") : rs.getString("nama");
                    JOptionPane.showMessageDialog(this, "Login Berhasil! Selamat Datang, " + nama + " (" + role + ")", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Username atau password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error Login: " + ex.getMessage());
        }
    }
}
