package gui.admin;

import koneksi.Koneksi;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * =====================================================================
 * TUGAS: ANGGOTA 2
 * Deskripsi: Kelola data lapangan (CRUD). Query SQL langsung di sini (Tanpa DAO).
 * =====================================================================
 */
public class FormLapangan extends JFrame {
    private JTable tabelLapangan;
    private DefaultTableModel modelTabel;
    private JTextField txtId, txtNama, txtJenis, txtHarga;
    private JComboBox<String> cbStatus;
    private JButton btnSimpan, btnUbah, btnHapus, btnReset;

    public FormLapangan() {
        setTitle("Kelola Data Lapangan - Anggota 2");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(41, 128, 185));
        JLabel lblJudul = new JLabel("MANAJEMEN DATA LAPANGAN");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 18));
        lblJudul.setForeground(Color.WHITE);
        panelHeader.add(lblJudul);
        add(panelHeader, BorderLayout.NORTH);

        // Form Input Panel
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField();
        txtId.setEditable(false);
        txtNama = new JTextField(15);
        txtJenis = new JTextField(15);
        txtHarga = new JTextField(15);
        cbStatus = new JComboBox<>(new String[]{"Tersedia", "Maintenance", "Renovasi"});

        // Baris 1: ID Lapangan
        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("ID Lapangan:"), gbc);
        gbc.gridx = 1; panelForm.add(txtId, gbc);

        // Baris 2: Nama Lapangan
        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Nama Lapangan:"), gbc);
        gbc.gridx = 1; panelForm.add(txtNama, gbc);

        // Baris 3: Jenis Lapangan
        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Jenis:"), gbc);
        gbc.gridx = 1; panelForm.add(txtJenis, gbc);

        // Baris 4: Harga Per Jam
        gbc.gridx = 0; gbc.gridy = 3; panelForm.add(new JLabel("Harga / Jam (Rp):"), gbc);
        gbc.gridx = 1; panelForm.add(txtHarga, gbc);

        // Baris 5: Status
        gbc.gridx = 0; gbc.gridy = 4; panelForm.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; panelForm.add(cbStatus, gbc);

        // Tombol Aksi
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        btnSimpan = new JButton("Tambah");
        btnUbah = new JButton("Ubah");
        btnHapus = new JButton("Hapus");
        btnReset = new JButton("Reset");

        panelTombol.add(btnSimpan);
        panelTombol.add(btnUbah);
        panelTombol.add(btnHapus);
        panelTombol.add(btnReset);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panelForm.add(panelTombol, gbc);

        add(panelForm, BorderLayout.WEST);

        // Tabel Data
        modelTabel = new DefaultTableModel(new String[]{"ID", "Nama Lapangan", "Jenis", "Harga/Jam", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelLapangan = new JTable(modelTabel);
        tabelLapangan.getSelectionModel().addListSelectionListener(e -> pilihDataTabel());
        add(new JScrollPane(tabelLapangan), BorderLayout.CENTER);

        // Event Listeners
        btnSimpan.addActionListener(e -> simpanData());
        btnUbah.addActionListener(e -> ubahData());
        btnHapus.addActionListener(e -> hapusData());
        btnReset.addActionListener(e -> resetForm());

        loadDataLapangan();
    }

    /**
     * QUERY LANGSUNG: Membaca seluruh data lapangan dari database.
     */
    private void loadDataLapangan() {
        modelTabel.setRowCount(0);
        String sql = "SELECT * FROM lapangan ORDER BY id_lapangan ASC";
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn != null ? conn.createStatement() : null;
             ResultSet rs = stmt != null ? stmt.executeQuery(sql) : null) {
            if (rs != null) {
                while (rs.next()) {
                    modelTabel.addRow(new Object[]{
                        rs.getInt("id_lapangan"),
                        rs.getString("nama_lapangan"),
                        rs.getString("jenis"),
                        rs.getInt("harga_per_jam"),
                        rs.getString("status")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage());
        }
    }

    /**
     * QUERY LANGSUNG: Menambahkan data lapangan baru.
     */
    private void simpanData() {
        if (txtNama.getText().trim().isEmpty() || txtHarga.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan Harga harus diisi!");
            return;
        }
        String sql = "INSERT INTO lapangan (nama_lapangan, jenis, harga_per_jam, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtNama.getText().trim());
            ps.setString(2, txtJenis.getText().trim());
            ps.setInt(3, Integer.parseInt(txtHarga.getText().trim()));
            ps.setString(4, cbStatus.getSelectedItem().toString());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data lapangan berhasil ditambahkan!");
            resetForm();
            loadDataLapangan();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal menambah data: " + ex.getMessage());
        }
    }

    /**
     * QUERY LANGSUNG: Mengubah data lapangan yang dipilih.
     */
    private void ubahData() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data lapangan yang ingin diubah terlebih dahulu!");
            return;
        }
        String sql = "UPDATE lapangan SET nama_lapangan=?, jenis=?, harga_per_jam=?, status=? WHERE id_lapangan=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtNama.getText().trim());
            ps.setString(2, txtJenis.getText().trim());
            ps.setInt(3, Integer.parseInt(txtHarga.getText().trim()));
            ps.setString(4, cbStatus.getSelectedItem().toString());
            ps.setInt(5, Integer.parseInt(txtId.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data lapangan berhasil diperbarui!");
            resetForm();
            loadDataLapangan();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal mengubah data: " + ex.getMessage());
        }
    }

    /**
     * QUERY LANGSUNG: Menghapus data lapangan dari database.
     */
    private void hapusData() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data lapangan yang ingin dihapus terlebih dahulu!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus lapangan ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM lapangan WHERE id_lapangan=?";
            try (Connection conn = Koneksi.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, Integer.parseInt(txtId.getText().trim()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data lapangan berhasil dihapus!");
                resetForm();
                loadDataLapangan();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + ex.getMessage());
            }
        }
    }

    private void pilihDataTabel() {
        int row = tabelLapangan.getSelectedRow();
        if (row != -1) {
            txtId.setText(modelTabel.getValueAt(row, 0).toString());
            txtNama.setText(modelTabel.getValueAt(row, 1).toString());
            txtJenis.setText(modelTabel.getValueAt(row, 2).toString());
            txtHarga.setText(modelTabel.getValueAt(row, 3).toString());
            cbStatus.setSelectedItem(modelTabel.getValueAt(row, 4).toString());
        }
    }

    private void resetForm() {
        txtId.setText("");
        txtNama.setText("");
        txtJenis.setText("");
        txtHarga.setText("");
        cbStatus.setSelectedIndex(0);
        tabelLapangan.clearSelection();
    }
}
