package gui.transaksi;

import koneksi.Koneksi;
import model.Transaksi;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * =====================================================================
 * TUGAS: KAMU (VALEN)
 * Deskripsi: Form Transaksi & Pembayaran.
 * Query SQL langsung di sini (Tanpa DAO), menggunakan rumus Transaksi.hitungTotal.
 * =====================================================================
 */
public class FormTransaksi extends JFrame {
    private JComboBox<String> cbBooking;
    private JTextField txtTotal;
    private JComboBox<String> cbMetode, cbStatus;
    private List<String> dataBooking = new ArrayList<>();

    public FormTransaksi() {
        setTitle("Form Transaksi / Pembayaran - Bagian Kamu");
        setSize(650, 440);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(46, 204, 113));
        JLabel lblJudul = new JLabel("FORM TRANSAKSI & PEMBAYARAN BOOKING");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 18));
        lblJudul.setForeground(Color.WHITE);
        panelHeader.add(lblJudul);
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbBooking = new JComboBox<>();
        txtTotal = new JTextField();
        txtTotal.setEditable(false);
        txtTotal.setFont(new Font("Arial", Font.BOLD, 15));
        txtTotal.setForeground(new Color(39, 174, 96));

        cbMetode = new JComboBox<>(new String[]{"Cash", "Transfer", "QRIS"});
        cbStatus = new JComboBox<>(new String[]{"Lunas", "Belum Lunas"});

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Pilih Booking (Menunggu Pembayaran):"), gbc);
        gbc.gridx = 1; panelForm.add(cbBooking, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Total Harga (Dihitung Otomatis):"), gbc);
        gbc.gridx = 1; panelForm.add(txtTotal, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Metode Pembayaran:"), gbc);
        gbc.gridx = 1; panelForm.add(cbMetode, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelForm.add(new JLabel("Status Pembayaran:"), gbc);
        gbc.gridx = 1; panelForm.add(cbStatus, gbc);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnSimpan = new JButton("Simpan Transaksi");
        btnSimpan.setBackground(new Color(46, 204, 113));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnRefresh = new JButton("Refresh Data Booking");
        panelTombol.add(btnSimpan);
        panelTombol.add(btnRefresh);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panelForm.add(panelTombol, gbc);

        JLabel lblInfo = new JLabel("<html><i>* Total harga dihitung otomatis dari rumus: <b>Harga/Jam x Durasi Jam</b></i></html>");
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panelForm.add(lblInfo, gbc);

        add(panelForm, BorderLayout.CENTER);

        cbBooking.addActionListener(e -> updateTotal());
        btnRefresh.addActionListener(e -> loadBookingMenunggu());
        btnSimpan.addActionListener(e -> simpanTransaksi());

        loadBookingMenunggu();
    }

    /**
     * QUERY LANGSUNG: Mengambil data booking yang berstatus 'Menunggu Pembayaran'
     * dan menghitung total harga dengan memanggil model.Transaksi.hitungTotal().
     */
    private void loadBookingMenunggu() {
        dataBooking.clear();
        cbBooking.removeAllItems();

        String sql = "SELECT b.id_booking, m.nama, l.nama_lapangan, j.tanggal, j.jam_mulai, j.jam_selesai, l.harga_per_jam " +
                     "FROM booking b " +
                     "JOIN member m ON b.id_member = m.id_member " +
                     "JOIN jadwal j ON b.id_jadwal = j.id_jadwal " +
                     "JOIN lapangan l ON j.id_lapangan = l.id_lapangan " +
                     "WHERE b.status_booking = 'Menunggu Pembayaran' " +
                     "ORDER BY b.id_booking ASC";

        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn != null ? conn.createStatement() : null;
             ResultSet rs = stmt != null ? stmt.executeQuery(sql) : null) {
            if (rs != null) {
                while (rs.next()) {
                    Time mulai = rs.getTime("jam_mulai");
                    Time selesai = rs.getTime("jam_selesai");
                    int durasi = 1;
                    if (mulai != null && selesai != null) {
                        durasi = (int) ((selesai.getTime() - mulai.getTime()) / (1000 * 60 * 60));
                        if (durasi <= 0) durasi = 1;
                    }
                    int hargaPerJam = rs.getInt("harga_per_jam");
                    // Memanggil rumus perhitungan
                    int total = Transaksi.hitungTotal(hargaPerJam, durasi);

                    String label = rs.getInt("id_booking") + " | " + rs.getString("nama") + " | " + 
                                   rs.getString("nama_lapangan") + " | " + rs.getDate("tanggal") + " (" + 
                                   rs.getTime("jam_mulai") + " - " + rs.getTime("jam_selesai") + ") | TOTAL: " + total;
                    dataBooking.add(label);
                    cbBooking.addItem(label);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat booking: " + ex.getMessage());
        }

        if (dataBooking.isEmpty()) {
            cbBooking.addItem("Tidak ada booking menunggu pembayaran");
        }
        updateTotal();
    }

    private void updateTotal() {
        if (dataBooking.isEmpty() || cbBooking.getSelectedItem() == null) {
            txtTotal.setText("0");
            return;
        }
        try {
            String selected = cbBooking.getSelectedItem().toString();
            if (selected.contains("TOTAL:")) {
                String totalPart = selected.substring(selected.lastIndexOf("TOTAL:") + 6).trim();
                txtTotal.setText(totalPart);
            } else {
                txtTotal.setText("0");
            }
        } catch (Exception ex) {
            txtTotal.setText("0");
        }
    }

    /**
     * QUERY LANGSUNG: Menyimpan data transaksi ke tabel transaksi dan update status booking.
     */
    private void simpanTransaksi() {
        if (dataBooking.isEmpty() || cbBooking.getSelectedItem() == null ||
            cbBooking.getSelectedItem().toString().startsWith("Tidak ada")) {
            JOptionPane.showMessageDialog(this, "Pilih booking yang valid terlebih dahulu!");
            return;
        }

        try {
            String selected = cbBooking.getSelectedItem().toString();
            int idBooking = Integer.parseInt(selected.split(" \\| ")[0].trim());
            int total = Integer.parseInt(txtTotal.getText().trim());
            String metode = cbMetode.getSelectedItem().toString();
            String status = cbStatus.getSelectedItem().toString();

            String insertSql = "INSERT INTO transaksi (id_booking, total_harga, metode_pembayaran, status_pembayaran) VALUES (?, ?, ?, ?)";
            String updateBookingSql = "UPDATE booking SET status_booking = ? WHERE id_booking = ?";
            String updateJadwalSql = "UPDATE jadwal j JOIN booking b ON j.id_jadwal = b.id_jadwal SET j.status = 'Dipesan' WHERE b.id_booking = ?";

            try (Connection conn = Koneksi.getConnection()) {
                if (conn == null) return;
                conn.setAutoCommit(false);
                try (PreparedStatement psInsert = conn.prepareStatement(insertSql);
                     PreparedStatement psUpdateBooking = conn.prepareStatement(updateBookingSql)) {

                    psInsert.setInt(1, idBooking);
                    psInsert.setInt(2, total);
                    psInsert.setString(3, metode);
                    psInsert.setString(4, status);
                    psInsert.executeUpdate();

                    psUpdateBooking.setString(1, status.equals("Lunas") ? "Terkonfirmasi" : "Menunggu Pembayaran");
                    psUpdateBooking.setInt(2, idBooking);
                    psUpdateBooking.executeUpdate();

                    if (status.equals("Lunas")) {
                        try (PreparedStatement psUpdateJadwal = conn.prepareStatement(updateJadwalSql)) {
                            psUpdateJadwal.setInt(1, idBooking);
                            psUpdateJadwal.executeUpdate();
                        }
                    }

                    conn.commit();
                    JOptionPane.showMessageDialog(this, "Transaksi Berhasil Disimpan!\nID Booking: " + idBooking + "\nTotal: Rp " + total + "\nStatus: " + status);
                    loadBookingMenunggu();
                } catch (SQLException ex) {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi: " + ex.getMessage());
                } finally {
                    conn.setAutoCommit(true);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error input: " + ex.getMessage());
        }
    }
}
