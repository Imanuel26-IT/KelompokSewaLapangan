package gui.admin;

import koneksi.Koneksi;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * =====================================================================
 * TUGAS: ANGGOTA 2
 * Deskripsi: Kelola data jadwal lapangan. Query SQL langsung di sini (Tanpa DAO).
 * =====================================================================
 */
public class FormJadwal extends JFrame {
    private JTable tabelJadwal;
    private DefaultTableModel modelTabel;
    private JTextField txtIdJadwal, txtTanggal, txtJamMulai, txtJamSelesai;
    private JComboBox<String> cbLapangan, cbStatus;
    private JButton btnSimpan, btnUbah, btnHapus, btnReset;

    public FormJadwal() {
        setTitle("Kelola Data Jadwal - Anggota 2");
        setSize(850, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(52, 73, 94));
        JLabel lblJudul = new JLabel("MANAJEMEN JADWAL LAPANGAN");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 18));
        lblJudul.setForeground(Color.WHITE);
        panelHeader.add(lblJudul);
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIdJadwal = new JTextField();
        txtIdJadwal.setEditable(false);
        cbLapangan = new JComboBox<>();
        txtTanggal = new JTextField(12); // Format: YYYY-MM-DD
        txtJamMulai = new JTextField(12); // Format: HH:MM:SS
        txtJamSelesai = new JTextField(12);
        cbStatus = new JComboBox<>(new String[]{"Tersedia", "Dipesan", "Selesai"});

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("ID Jadwal:"), gbc);
        gbc.gridx = 1; panelForm.add(txtIdJadwal, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Pilih Lapangan:"), gbc);
        gbc.gridx = 1; panelForm.add(cbLapangan, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Tanggal (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; panelForm.add(txtTanggal, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelForm.add(new JLabel("Jam Mulai (HH:MM:SS):"), gbc);
        gbc.gridx = 1; panelForm.add(txtJamMulai, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panelForm.add(new JLabel("Jam Selesai (HH:MM:SS):"), gbc);
        gbc.gridx = 1; panelForm.add(txtJamSelesai, gbc);

        gbc.gridx = 0; gbc.gridy = 5; panelForm.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; panelForm.add(cbStatus, gbc);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        btnSimpan = new JButton("Tambah");
        btnUbah = new JButton("Ubah");
        btnHapus = new JButton("Hapus");
        btnReset = new JButton("Reset");

        panelTombol.add(btnSimpan);
        panelTombol.add(btnUbah);
        panelTombol.add(btnHapus);
        panelTombol.add(btnReset);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panelForm.add(panelTombol, gbc);

        add(panelForm, BorderLayout.WEST);

        modelTabel = new DefaultTableModel(new String[]{"ID", "Lapangan", "Tanggal", "Jam Mulai", "Jam Selesai", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabelJadwal = new JTable(modelTabel);
        tabelJadwal.getSelectionModel().addListSelectionListener(e -> pilihDataTabel());
        add(new JScrollPane(tabelJadwal), BorderLayout.CENTER);

        btnSimpan.addActionListener(e -> simpanJadwal());
        btnUbah.addActionListener(e -> ubahJadwal());
        btnHapus.addActionListener(e -> hapusJadwal());
        btnReset.addActionListener(e -> resetForm());

        loadComboLapangan();
        loadDataJadwal();
    }

    /**
     * QUERY LANGSUNG: Mengambil opsi daftar lapangan untuk ComboBox.
     */
    private void loadComboLapangan() {
        cbLapangan.removeAllItems();
        String sql = "SELECT id_lapangan, nama_lapangan FROM lapangan ORDER BY id_lapangan";
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn != null ? conn.createStatement() : null;
             ResultSet rs = stmt != null ? stmt.executeQuery(sql) : null) {
            if (rs != null) {
                while (rs.next()) {
                    cbLapangan.addItem(rs.getInt("id_lapangan") + " - " + rs.getString("nama_lapangan"));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat daftar lapangan: " + ex.getMessage());
        }
    }

    /**
     * QUERY LANGSUNG: Menampilkan data jadwal lapangan dengan JOIN ke tabel lapangan.
     */
    private void loadDataJadwal() {
        modelTabel.setRowCount(0);
        String sql = "SELECT j.id_jadwal, l.nama_lapangan, j.tanggal, j.jam_mulai, j.jam_selesai, j.status " +
                     "FROM jadwal j JOIN lapangan l ON j.id_lapangan = l.id_lapangan " +
                     "ORDER BY j.tanggal DESC, j.jam_mulai ASC";
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn != null ? conn.createStatement() : null;
             ResultSet rs = stmt != null ? stmt.executeQuery(sql) : null) {
            if (rs != null) {
                while (rs.next()) {
                    modelTabel.addRow(new Object[]{
                        rs.getInt("id_jadwal"),
                        rs.getString("nama_lapangan"),
                        rs.getDate("tanggal"),
                        rs.getTime("jam_mulai"),
                        rs.getTime("jam_selesai"),
                        rs.getString("status")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat jadwal: " + ex.getMessage());
        }
    }

    private void simpanJadwal() {
        if (cbLapangan.getSelectedItem() == null || txtTanggal.getText().trim().isEmpty() ||
            txtJamMulai.getText().trim().isEmpty() || txtJamSelesai.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lengkapi semua data jadwal!");
            return;
        }
        int idLapangan = Integer.parseInt(cbLapangan.getSelectedItem().toString().split(" - ")[0]);
        String sql = "INSERT INTO jadwal (id_lapangan, tanggal, jam_mulai, jam_selesai, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLapangan);
            ps.setString(2, txtTanggal.getText().trim());
            ps.setString(3, txtJamMulai.getText().trim());
            ps.setString(4, txtJamSelesai.getText().trim());
            ps.setString(5, cbStatus.getSelectedItem().toString());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Jadwal berhasil ditambahkan!");
            resetForm();
            loadDataJadwal();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menambah jadwal: " + ex.getMessage());
        }
    }

    private void ubahJadwal() {
        if (txtIdJadwal.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih jadwal yang ingin diubah!");
            return;
        }
        int idLapangan = Integer.parseInt(cbLapangan.getSelectedItem().toString().split(" - ")[0]);
        String sql = "UPDATE jadwal SET id_lapangan=?, tanggal=?, jam_mulai=?, jam_selesai=?, status=? WHERE id_jadwal=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLapangan);
            ps.setString(2, txtTanggal.getText().trim());
            ps.setString(3, txtJamMulai.getText().trim());
            ps.setString(4, txtJamSelesai.getText().trim());
            ps.setString(5, cbStatus.getSelectedItem().toString());
            ps.setInt(6, Integer.parseInt(txtIdJadwal.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Jadwal berhasil diperbarui!");
            resetForm();
            loadDataJadwal();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal mengubah jadwal: " + ex.getMessage());
        }
    }

    private void hapusJadwal() {
        if (txtIdJadwal.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih jadwal yang ingin dihapus!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus jadwal ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM jadwal WHERE id_jadwal=?";
            try (Connection conn = Koneksi.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, Integer.parseInt(txtIdJadwal.getText().trim()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Jadwal berhasil dihapus!");
                resetForm();
                loadDataJadwal();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus jadwal: " + ex.getMessage());
            }
        }
    }

    private void pilihDataTabel() {
        int row = tabelJadwal.getSelectedRow();
        if (row != -1) {
            txtIdJadwal.setText(modelTabel.getValueAt(row, 0).toString());
            String namaLap = modelTabel.getValueAt(row, 1).toString();
            for (int i = 0; i < cbLapangan.getItemCount(); i++) {
                if (cbLapangan.getItemAt(i).contains(namaLap)) {
                    cbLapangan.setSelectedIndex(i);
                    break;
                }
            }
            txtTanggal.setText(modelTabel.getValueAt(row, 2).toString());
            txtJamMulai.setText(modelTabel.getValueAt(row, 3).toString());
            txtJamSelesai.setText(modelTabel.getValueAt(row, 4).toString());
            cbStatus.setSelectedItem(modelTabel.getValueAt(row, 5).toString());
        }
    }

    private void resetForm() {
        txtIdJadwal.setText("");
        txtTanggal.setText("");
        txtJamMulai.setText("");
        txtJamSelesai.setText("");
        cbStatus.setSelectedIndex(0);
        tabelJadwal.clearSelection();
    }
}
