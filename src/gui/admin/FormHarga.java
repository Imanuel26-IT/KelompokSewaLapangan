package gui.admin;

import koneksi.Koneksi;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * =====================================================================
 * TUGAS: ANGGOTA 2
 * Deskripsi: Kelola tarif/harga sewa per jam lapangan. Query SQL langsung di sini.
 * =====================================================================
 */
public class FormHarga extends JFrame {
    private JTable tabelHarga;
    private DefaultTableModel modelTabel;
    private JComboBox<String> cbLapangan;
    private JTextField txtHargaLama, txtHargaBaru;
    private JButton btnUpdateHarga, btnRefresh;

    public FormHarga() {
        setTitle("Kelola Tarif & Harga Lapangan - Anggota 2");
        setSize(650, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(39, 174, 96));
        JLabel lblJudul = new JLabel("PENGATURAN TARIF / HARGA SEWA LAPANGAN");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 16));
        lblJudul.setForeground(Color.WHITE);
        panelHeader.add(lblJudul);
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelTop = new JPanel(new GridLayout(3, 2, 8, 8));
        panelTop.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        cbLapangan = new JComboBox<>();
        txtHargaLama = new JTextField();
        txtHargaLama.setEditable(false);
        txtHargaBaru = new JTextField();

        panelTop.add(new JLabel("Pilih Lapangan:"));
        panelTop.add(cbLapangan);
        panelTop.add(new JLabel("Harga Saat Ini (Rp/jam):"));
        panelTop.add(txtHargaLama);
        panelTop.add(new JLabel("Tarif Baru (Rp/jam):"));
        panelTop.add(txtHargaBaru);

        add(panelTop, BorderLayout.NORTH);

        JPanel panelCenter = new JPanel(new BorderLayout());
        modelTabel = new DefaultTableModel(new String[]{"ID", "Nama Lapangan", "Jenis", "Harga / Jam (Rp)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabelHarga = new JTable(modelTabel);
        panelCenter.add(new JScrollPane(tabelHarga), BorderLayout.CENTER);
        add(panelCenter, BorderLayout.CENTER);

        JPanel panelBawah = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnUpdateHarga = new JButton("Simpan Perubahan Tarif");
        btnRefresh = new JButton("Refresh");
        panelBawah.add(btnRefresh);
        panelBawah.add(btnUpdateHarga);
        add(panelBawah, BorderLayout.SOUTH);

        cbLapangan.addActionListener(e -> updateFieldHargaLama());
        btnUpdateHarga.addActionListener(e -> updateHarga());
        btnRefresh.addActionListener(e -> {
            loadComboLapangan();
            loadDataTabel();
        });

        loadComboLapangan();
        loadDataTabel();
    }

    private void loadComboLapangan() {
        cbLapangan.removeAllItems();
        String sql = "SELECT id_lapangan, nama_lapangan, harga_per_jam FROM lapangan ORDER BY id_lapangan";
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn != null ? conn.createStatement() : null;
             ResultSet rs = stmt != null ? stmt.executeQuery(sql) : null) {
            if (rs != null) {
                while (rs.next()) {
                    cbLapangan.addItem(rs.getInt("id_lapangan") + " - " + rs.getString("nama_lapangan") + " (" + rs.getInt("harga_per_jam") + ")");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat lapangan: " + ex.getMessage());
        }
    }

    private void updateFieldHargaLama() {
        if (cbLapangan.getSelectedItem() != null) {
            String item = cbLapangan.getSelectedItem().toString();
            int start = item.lastIndexOf('(');
            int end = item.lastIndexOf(')');
            if (start != -1 && end != -1) {
                txtHargaLama.setText(item.substring(start + 1, end));
            }
        }
    }

    private void loadDataTabel() {
        modelTabel.setRowCount(0);
        String sql = "SELECT id_lapangan, nama_lapangan, jenis, harga_per_jam FROM lapangan";
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn != null ? conn.createStatement() : null;
             ResultSet rs = stmt != null ? stmt.executeQuery(sql) : null) {
            if (rs != null) {
                while (rs.next()) {
                    modelTabel.addRow(new Object[]{
                        rs.getInt("id_lapangan"),
                        rs.getString("nama_lapangan"),
                        rs.getString("jenis"),
                        rs.getInt("harga_per_jam")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat tabel harga: " + ex.getMessage());
        }
    }

    /**
     * QUERY LANGSUNG: Memperbarui tarif lapangan.
     */
    private void updateHarga() {
        if (cbLapangan.getSelectedItem() == null || txtHargaBaru.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan tarif harga baru!");
            return;
        }
        int idLapangan = Integer.parseInt(cbLapangan.getSelectedItem().toString().split(" - ")[0]);
        int hargaBaru;
        try {
            hargaBaru = Integer.parseInt(txtHargaBaru.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Format tarif harga tidak valid!");
            return;
        }

        String sql = "UPDATE lapangan SET harga_per_jam=? WHERE id_lapangan=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hargaBaru);
            ps.setInt(2, idLapangan);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Tarif harga berhasil diperbarui!");
            txtHargaBaru.setText("");
            loadComboLapangan();
            loadDataTabel();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal update tarif: " + ex.getMessage());
        }
    }
}
