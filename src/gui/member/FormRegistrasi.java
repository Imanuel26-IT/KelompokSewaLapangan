package gui.member;

import koneksi.Koneksi;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

/**
 * =====================================================================
 * TUGAS: ANGGOTA 3
 * Deskripsi: Form Registrasi Akun Member Baru. Query SQL langsung di sini.
 * =====================================================================
 */
public class FormRegistrasi extends JFrame {
    private JTextField txtNama, txtUsername, txtNoHp;
    private JPasswordField txtPassword;
    private JTextArea txtAlamat;
    private JButton btnDaftar, btnBatal;

    public FormRegistrasi() {
        setTitle("Registrasi Member Baru - Anggota 3");
        setSize(450, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(39, 174, 96));
        JLabel lblJudul = new JLabel("PENDAFTARAN MEMBER BARU");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 16));
        lblJudul.setForeground(Color.WHITE);
        panelHeader.add(lblJudul);
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNama = new JTextField(15);
        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);
        txtNoHp = new JTextField(15);
        txtAlamat = new JTextArea(3, 15);
        txtAlamat.setLineWrap(true);

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Nama Lengkap:"), gbc);
        gbc.gridx = 1; panelForm.add(txtNama, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; panelForm.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; panelForm.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelForm.add(new JLabel("No. HP / WhatsApp:"), gbc);
        gbc.gridx = 1; panelForm.add(txtNoHp, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panelForm.add(new JLabel("Alamat:"), gbc);
        gbc.gridx = 1; panelForm.add(new JScrollPane(txtAlamat), gbc);

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnDaftar = new JButton("Daftar Sekarang");
        btnBatal = new JButton("Batal");
        panelBtn.add(btnDaftar);
        panelBtn.add(btnBatal);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panelForm.add(panelBtn, gbc);

        add(panelForm, BorderLayout.CENTER);

        btnDaftar.addActionListener(e -> simpanRegistrasi());
        btnBatal.addActionListener(e -> dispose());
    }

    /**
     * QUERY LANGSUNG: Menyimpan member baru ke database (Tanpa DAO).
     */
    private void simpanRegistrasi() {
        String nama = txtNama.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String hp = txtNoHp.getText().trim();
        String alamat = txtAlamat.getText().trim();

        if (nama.isEmpty() || username.isEmpty() || password.isEmpty() || hp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap lengkapi seluruh formulir registrasi!");
            return;
        }

        String sql = "INSERT INTO member (nama, username, password, no_hp, alamat) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return;
            ps.setString(1, nama);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, hp);
            ps.setString(5, alamat);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registrasi berhasil! Silakan login dengan akun Anda.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal mendaftar: " + ex.getMessage());
        }
    }
}
