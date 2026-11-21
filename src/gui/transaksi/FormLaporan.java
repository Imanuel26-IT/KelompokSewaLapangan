package gui.transaksi;

import koneksi.Koneksi;
import java.awt.*;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * =====================================================================
 * TUGAS: KAMU (VALEN)
 * Deskripsi: Form Laporan Rekapitulasi Transaksi & Pembatalan.
 * Query SQL langsung di sini (Tanpa DAO).
 * =====================================================================
 */
public class FormLaporan extends JFrame {
    private JTable tabelLaporan;
    private DefaultTableModel modelTabel;
    private JComboBox<String> cbFilter;
    private JLabel lblTotalLunas, lblTotalValid;

    public FormLaporan() {
        setTitle("Laporan Transaksi & Rekapitulasi - Bagian Kamu");
        setSize(1100, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelAtas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelAtas.setBackground(new Color(236, 240, 241));
        panelAtas.add(new JLabel("Filter Status:"));

        cbFilter = new JComboBox<>(new String[]{"Semua", "Lunas", "Belum Lunas", "Dibatalkan"});
        panelAtas.add(cbFilter);

        JButton btnTampil = new JButton("Tampilkan Data");
        JButton btnBatal = new JButton("Batalkan Transaksi");
        btnBatal.setBackground(new Color(231, 76, 60));
        btnBatal.setForeground(Color.WHITE);

        panelAtas.add(btnTampil);
        panelAtas.add(btnBatal);
        add(panelAtas, BorderLayout.NORTH);

        String[] kolom = {
            "ID Transaksi", "ID Booking", "Member", "Lapangan", "Tanggal", 
            "Jam Main", "Harga/Jam", "Durasi", "Total Harga", "Metode", "Status", "Validasi", "Waktu Transaksi"
        };
        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabelLaporan = new JTable(modelTabel);
        tabelLaporan.setAutoCreateRowSorter(true);
        add(new JScrollPane(tabelLaporan), BorderLayout.CENTER);

        JPanel panelBawah = new JPanel(new GridLayout(2, 1, 5, 5));
        panelBawah.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        lblTotalLunas = new JLabel("Total Pendapatan (Status Lunas): Rp 0");
        lblTotalLunas.setFont(new Font("Arial", Font.BOLD, 14));

        lblTotalValid = new JLabel("Total Pendapatan VALID: Rp 0");
        lblTotalValid.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalValid.setForeground(new Color(39, 174, 96));

        panelBawah.add(lblTotalLunas);
        panelBawah.add(lblTotalValid);
        add(panelBawah, BorderLayout.SOUTH);

        btnTampil.addActionListener(e -> muatDataLaporan());
        cbFilter.addActionListener(e -> muatDataLaporan());
        btnBatal.addActionListener(e -> batalkanTransaksi());

        muatDataLaporan();
    }

    /**
     * QUERY LANGSUNG: Mengambil data rekapitulasi transaksi dengan multi-table JOIN (Tanpa DAO).
     */
    private void muatDataLaporan() {
        modelTabel.setRowCount(0);
        String filter = cbFilter.getSelectedItem().toString();

        String sql = "SELECT t.id_transaksi, t.id_booking, m.nama, l.nama_lapangan, j.tanggal, " +
                     "CONCAT(j.jam_mulai, ' - ', j.jam_selesai) AS jam, " +
                     "l.harga_per_jam, t.total_harga, t.metode_pembayaran, t.status_pembayaran, t.tanggal_transaksi " +
                     "FROM transaksi t " +
                     "JOIN booking b ON t.id_booking = b.id_booking " +
                     "JOIN member m ON b.id_member = m.id_member " +
                     "JOIN jadwal j ON b.id_jadwal = j.id_jadwal " +
                     "JOIN lapangan l ON j.id_lapangan = l.id_lapangan ";

        if (!filter.equals("Semua")) {
            sql += " WHERE t.status_pembayaran = '" + filter + "' ";
        }
        sql += " ORDER BY t.tanggal_transaksi DESC";

        int totalLunas = 0;
        int totalValid = 0;

        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn != null ? conn.createStatement() : null;
             ResultSet rs = stmt != null ? stmt.executeQuery(sql) : null) {
            if (rs != null) {
                while (rs.next()) {
                    int harga = rs.getInt("harga_per_jam");
                    int total = rs.getInt("total_harga");
                    int durasi = (harga > 0 && total > 0) ? (total / harga) : 0;
                    String status = rs.getString("status_pembayaran");

                    String validasi;
                    if ("Dibatalkan".equalsIgnoreCase(status)) {
                        validasi = "Dibatalkan";
                    } else if (total <= 0) {
                        validasi = "Data Error (0)";
                    } else if (harga > 0 && total % harga != 0) {
                        validasi = "Data Mencurigakan";
                    } else {
                        validasi = "Valid";
                    }

                    if ("Lunas".equalsIgnoreCase(status)) {
                        totalLunas += total;
                        if ("Valid".equals(validasi)) {
                            totalValid += total;
                        }
                    }

                    modelTabel.addRow(new Object[]{
                        rs.getInt("id_transaksi"),
                        rs.getInt("id_booking"),
                        rs.getString("nama"),
                        rs.getString("nama_lapangan"),
                        rs.getDate("tanggal"),
                        rs.getString("jam"),
                        harga,
                        durasi + " jam",
                        total,
                        rs.getString("metode_pembayaran"),
                        status,
                        validasi,
                        rs.getTimestamp("tanggal_transaksi")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat laporan: " + ex.getMessage());
        }

        NumberFormat nf = NumberFormat.getInstance(new Locale("id", "ID"));
        lblTotalLunas.setText("Total Pendapatan (Status Lunas): Rp " + nf.format(totalLunas));
        lblTotalValid.setText("Total Pendapatan VALID: Rp " + nf.format(totalValid));
    }

    /**
     * QUERY LANGSUNG: Membatalkan transaksi dan mengembalikan jadwal ke 'Tersedia'.
     */
    private void batalkanTransaksi() {
        int row = tabelLaporan.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin dibatalkan terlebih dahulu!");
            return;
        }

        int modelRow = tabelLaporan.convertRowIndexToModel(row);
        int idTransaksi = (int) modelTabel.getValueAt(modelRow, 0);
        int idBooking = (int) modelTabel.getValueAt(modelRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin batalkan Transaksi #" + idTransaksi + " (Booking #" + idBooking + ")?\nJadwal lapangan akan kembali Tersedia.", 
            "Konfirmasi Pembatalan", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = Koneksi.getConnection()) {
                if (conn == null) return;
                conn.setAutoCommit(false);
                try (PreparedStatement ps1 = conn.prepareStatement("UPDATE transaksi SET status_pembayaran='Dibatalkan' WHERE id_transaksi=?");
                     PreparedStatement ps2 = conn.prepareStatement("UPDATE booking SET status_booking='Dibatalkan' WHERE id_booking=?");
                     PreparedStatement ps3 = conn.prepareStatement("UPDATE jadwal j JOIN booking b ON j.id_jadwal = b.id_jadwal SET j.status='Tersedia' WHERE b.id_booking=?")) {

                    ps1.setInt(1, idTransaksi);
                    ps1.executeUpdate();

                    ps2.setInt(1, idBooking);
                    ps2.executeUpdate();

                    ps3.setInt(1, idBooking);
                    ps3.executeUpdate();

                    conn.commit();
                    JOptionPane.showMessageDialog(this, "Transaksi berhasil dibatalkan dan jadwal kembali Tersedia!");
                    muatDataLaporan();
                } catch (SQLException ex) {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "Gagal membatalkan transaksi: " + ex.getMessage());
                } finally {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Koneksi database error: " + ex.getMessage());
            }
        }
    }
}
