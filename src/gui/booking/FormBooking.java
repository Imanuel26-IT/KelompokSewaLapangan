package gui.booking;

import koneksi.Koneksi;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * =====================================================================
 * TUGAS: ANGGOTA 4
 * Deskripsi: Form Pemesanan / Booking Lapangan. Query SQL langsung di sini (Tanpa DAO).
 * =====================================================================
 */
public class FormBooking extends JFrame {
    private JTable tabelBooking;
    private DefaultTableModel modelTabel;
    private JComboBox<String> cbMember, cbJadwal;
    private JTextField txtTanggalBooking, txtStatusBooking;
    private JButton btnBooking, btnBatalkanBooking, btnRefresh;

    public FormBooking() {
        setTitle("Pemesanan / Booking Lapangan - Anggota 4");
        setSize(900, 530);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(230, 126, 34));
        JLabel lblJudul = new JLabel("FORM PEMESANAN (BOOKING) LAPANGAN");
        lblJudul.setFont(new Font("Arial", Font.BOLD, 18));
        lblJudul.setForeground(Color.WHITE);
        panelHeader.add(lblJudul);
        add(panelHeader, BorderLayout.NORTH);

        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbMember = new JComboBox<>();
        cbJadwal = new JComboBox<>();
        txtTanggalBooking = new JTextField(LocalDate.now().toString());
        txtStatusBooking = new JTextField("Menunggu Pembayaran");
        txtStatusBooking.setEditable(false);

        gbc.gridx = 0; gbc.gridy = 0; panelInput.add(new JLabel("Pilih Member:"), gbc);
        gbc.gridx = 1; panelInput.add(cbMember, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelInput.add(new JLabel("Pilih Jadwal Tersedia:"), gbc);
        gbc.gridx = 1; panelInput.add(cbJadwal, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelInput.add(new JLabel("Tanggal Booking:"), gbc);
        gbc.gridx = 1; panelInput.add(txtTanggalBooking, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelInput.add(new JLabel("Status Awal:"), gbc);
        gbc.gridx = 1; panelInput.add(txtStatusBooking, gbc);

        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        btnBooking = new JButton("Simpan Booking");
        btnBooking.setBackground(new Color(230, 126, 34));
        btnBooking.setForeground(Color.WHITE);
        btnBatalkanBooking = new JButton("Batalkan Booking");
        btnRefresh = new JButton("Refresh Data");

        panelAksi.add(btnBooking);
        panelAksi.add(btnBatalkanBooking);
        panelAksi.add(btnRefresh);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panelInput.add(panelAksi, gbc);

        add(panelInput, BorderLayout.WEST);

        modelTabel = new DefaultTableModel(new String[]{
            "ID Booking", "Nama Member", "Lapangan", "Tgl Jadwal", "Jam", "Tarif/Jam", "Status Booking"
        }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelBooking = new JTable(modelTabel);
        add(new JScrollPane(tabelBooking), BorderLayout.CENTER);

        btnBooking.addActionListener(e -> simpanBooking());
        btnBatalkanBooking.addActionListener(e -> batalkanBooking());
        btnRefresh.addActionListener(e -> {
            loadComboMember();
            loadComboJadwal();
            loadDataBooking();
        });

        loadComboMember();
        loadComboJadwal();
        loadDataBooking();
    }

    /**
     * QUERY LANGSUNG: Mengambil daftar member.
     */
    private void loadComboMember() {
        cbMember.removeAllItems();
        String sql = "SELECT id_member, nama FROM member ORDER BY nama";
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn != null ? conn.createStatement() : null;
             ResultSet rs = stmt != null ? stmt.executeQuery(sql) : null) {
            if (rs != null) {
                while (rs.next()) {
                    cbMember.addItem(rs.getInt("id_member") + " - " + rs.getString("nama"));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat member: " + ex.getMessage());
        }
    }

    /**
     * QUERY LANGSUNG: Mengambil daftar jadwal yang statusnya masih 'Tersedia'.
     */
    private void loadComboJadwal() {
        cbJadwal.removeAllItems();
        String sql = "SELECT j.id_jadwal, l.nama_lapangan, j.tanggal, j.jam_mulai, j.jam_selesai, l.harga_per_jam " +
                     "FROM jadwal j JOIN lapangan l ON j.id_lapangan = l.id_lapangan " +
                     "WHERE j.status = 'Tersedia' ORDER BY j.tanggal, j.jam_mulai";
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn != null ? conn.createStatement() : null;
             ResultSet rs = stmt != null ? stmt.executeQuery(sql) : null) {
            if (rs != null) {
                while (rs.next()) {
                    cbJadwal.addItem(rs.getInt("id_jadwal") + " - " + rs.getString("nama_lapangan") + 
                        " (" + rs.getDate("tanggal") + " " + rs.getTime("jam_mulai") + " - " + rs.getTime("jam_selesai") + ")");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat jadwal tersedia: " + ex.getMessage());
        }
    }

    /**
     * QUERY LANGSUNG: Menampilkan daftar booking aktif dengan JOIN.
     */
    private void loadDataBooking() {
        modelTabel.setRowCount(0);
        String sql = "SELECT b.id_booking, m.nama, l.nama_lapangan, j.tanggal, " +
                     "CONCAT(j.jam_mulai, ' - ', j.jam_selesai) AS jam, l.harga_per_jam, b.status_booking " +
                     "FROM booking b " +
                     "JOIN member m ON b.id_member = m.id_member " +
                     "JOIN jadwal j ON b.id_jadwal = j.id_jadwal " +
                     "JOIN lapangan l ON j.id_lapangan = l.id_lapangan " +
                     "ORDER BY b.id_booking DESC";
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn != null ? conn.createStatement() : null;
             ResultSet rs = stmt != null ? stmt.executeQuery(sql) : null) {
            if (rs != null) {
                while (rs.next()) {
                    modelTabel.addRow(new Object[]{
                        rs.getInt("id_booking"),
                        rs.getString("nama"),
                        rs.getString("nama_lapangan"),
                        rs.getDate("tanggal"),
                        rs.getString("jam"),
                        rs.getInt("harga_per_jam"),
                        rs.getString("status_booking")
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat booking: " + ex.getMessage());
        }
    }

    /**
     * QUERY LANGSUNG: Menyimpan booking baru dan update status jadwal menjadi 'Dipesan'.
     */
    private void simpanBooking() {
        if (cbMember.getSelectedItem() == null || cbJadwal.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih member dan jadwal yang tersedia!");
            return;
        }

        int idMember = Integer.parseInt(cbMember.getSelectedItem().toString().split(" - ")[0]);
        int idJadwal = Integer.parseInt(cbJadwal.getSelectedItem().toString().split(" - ")[0]);
        String tglBooking = txtTanggalBooking.getText().trim();

        String sqlBooking = "INSERT INTO booking (id_member, id_jadwal, tanggal_booking, status_booking) VALUES (?, ?, ?, 'Menunggu Pembayaran')";
        String sqlUpdateJadwal = "UPDATE jadwal SET status = 'Dipesan' WHERE id_jadwal = ?";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) return;
            conn.setAutoCommit(false);
            try (PreparedStatement psBooking = conn.prepareStatement(sqlBooking);
                 PreparedStatement psJadwal = conn.prepareStatement(sqlUpdateJadwal)) {

                psBooking.setInt(1, idMember);
                psBooking.setInt(2, idJadwal);
                psBooking.setString(3, tglBooking);
                psBooking.executeUpdate();

                psJadwal.setInt(1, idJadwal);
                psJadwal.executeUpdate();

                conn.commit();
                JOptionPane.showMessageDialog(this, "Booking berhasil disimpan! Silakan lanjutkan pembayaran di Form Transaksi.");
                loadComboJadwal();
                loadDataBooking();
            } catch (SQLException ex) {
                conn.rollback();
                JOptionPane.showMessageDialog(this, "Gagal booking: " + ex.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Koneksi error: " + ex.getMessage());
        }
    }

    /**
     * QUERY LANGSUNG: Membatalkan booking dan mengembalikan jadwal ke 'Tersedia'.
     */
    private void batalkanBooking() {
        int row = tabelBooking.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris booking yang ingin dibatalkan!");
            return;
        }
        int idBooking = Integer.parseInt(modelTabel.getValueAt(row, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin batalkan booking #" + idBooking + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sqlBooking = "UPDATE booking SET status_booking = 'Dibatalkan' WHERE id_booking = ?";
            String sqlJadwal = "UPDATE jadwal j JOIN booking b ON j.id_jadwal = b.id_jadwal SET j.status = 'Tersedia' WHERE b.id_booking = ?";
            try (Connection conn = Koneksi.getConnection()) {
                if (conn == null) return;
                try (PreparedStatement ps1 = conn.prepareStatement(sqlBooking);
                     PreparedStatement ps2 = conn.prepareStatement(sqlJadwal)) {
                    ps1.setInt(1, idBooking);
                    ps1.executeUpdate();
                    ps2.setInt(1, idBooking);
                    ps2.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Booking berhasil dibatalkan!");
                    loadComboJadwal();
                    loadDataBooking();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Gagal membatalkan: " + ex.getMessage());
            }
        }
    }
}
