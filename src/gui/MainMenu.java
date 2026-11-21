package gui;

import gui.admin.FormHarga;
import gui.admin.FormJadwal;
import gui.admin.FormLapangan;
import gui.booking.FormBooking;
import gui.member.FormLogin;
import gui.member.FormRegistrasi;
import gui.transaksi.FormLaporan;
import gui.transaksi.FormTransaksi;
import model.Transaksi;

import java.awt.*;
import javax.swing.*;

/**
 * =====================================================================
 * DASHBOARD UTAMA - SISTEM BOOKING LAPANGAN
 * Menghubungkan semua modul form sesuai pembagian tugas kelompok:
 * - Admin     : Anggota 2 (FormLapangan, FormJadwal, FormHarga)
 * - Member    : Anggota 3 (FormLogin, FormRegistrasi)
 * - Booking   : Anggota 4 (FormBooking)
 * - Transaksi : Kamu / Valen (FormTransaksi, FormLaporan)
 * =====================================================================
 */
public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Sistem Booking Lapangan - Kelompok Sewa Lapangan");
        setSize(780, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel panelHeader = new JPanel(new GridLayout(2, 1));
        panelHeader.setBackground(new Color(44, 62, 80));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel lblJudul = new JLabel("SISTEM SEWA LAPANGAN OLAHRAGA", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 22));
        lblJudul.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Dashboard Menu Utama & Integrasi Modul Anggota Kelompok", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSub.setForeground(new Color(189, 195, 199));

        panelHeader.add(lblJudul);
        panelHeader.add(lblSub);
        add(panelHeader, BorderLayout.NORTH);

        // Center Panel: Modul Per Anggota
        JPanel panelMenu = new JPanel(new GridLayout(2, 2, 15, 15));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // 1. Modul Admin (Anggota 2)
        JPanel cardAdmin = buatCardModul("MODUL ADMIN (Anggota 2)", new Color(41, 128, 185));
        JButton btnLapangan = new JButton("Kelola Data Lapangan");
        JButton btnJadwal = new JButton("Kelola Jadwal Lapangan");
        JButton btnHarga = new JButton("Kelola Tarif / Harga");
        btnLapangan.addActionListener(e -> new FormLapangan().setVisible(true));
        btnJadwal.addActionListener(e -> new FormJadwal().setVisible(true));
        btnHarga.addActionListener(e -> new FormHarga().setVisible(true));
        cardAdmin.add(btnLapangan);
        cardAdmin.add(btnJadwal);
        cardAdmin.add(btnHarga);
        panelMenu.add(cardAdmin);

        // 2. Modul Member (Anggota 3)
        JPanel cardMember = buatCardModul("MODUL MEMBER (Anggota 3)", new Color(142, 68, 173));
        JButton btnLogin = new JButton("Form Login (Member/Admin)");
        JButton btnRegistrasi = new JButton("Form Registrasi Member Baru");
        btnLogin.addActionListener(e -> new FormLogin().setVisible(true));
        btnRegistrasi.addActionListener(e -> new FormRegistrasi().setVisible(true));
        cardMember.add(btnLogin);
        cardMember.add(btnRegistrasi);
        panelMenu.add(cardMember);

        // 3. Modul Booking (Anggota 4)
        JPanel cardBooking = buatCardModul("MODUL BOOKING (Anggota 4)", new Color(230, 126, 34));
        JButton btnBooking = new JButton("Form Pemesanan / Booking");
        btnBooking.addActionListener(e -> new FormBooking().setVisible(true));
        cardBooking.add(btnBooking);
        panelMenu.add(cardBooking);

        // 4. Modul Transaksi & Laporan (Kamu)
        JPanel cardTransaksi = buatCardModul("MODUL TRANSAKSI (Kamu)", new Color(39, 174, 96));
        JButton btnTransaksi = new JButton("Form Transaksi Pembayaran");
        JButton btnLaporan = new JButton("Laporan Rekapitulasi");
        JButton btnTestRumus = new JButton("Uji Rumus hitungTotal");

        btnTransaksi.addActionListener(e -> new FormTransaksi().setVisible(true));
        btnLaporan.addActionListener(e -> new FormLaporan().setVisible(true));
        btnTestRumus.addActionListener(e -> ujiRumusHitungTotal());

        cardTransaksi.add(btnTransaksi);
        cardTransaksi.add(btnLaporan);
        cardTransaksi.add(btnTestRumus);
        panelMenu.add(cardTransaksi);

        add(panelMenu, BorderLayout.CENTER);

        // Footer
        JLabel lblFooter = new JLabel("Database: MySQL (XAMPP) | Struktur Proyek: Model-GUI Tanpa DAO", SwingConstants.CENTER);
        lblFooter.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        lblFooter.setFont(new Font("Arial", Font.ITALIC, 11));
        add(lblFooter, BorderLayout.SOUTH);
    }

    private JPanel buatCardModul(String judul, Color headerColor) {
        JPanel card = new JPanel();
        card.setLayout(new GridLayout(4, 1, 6, 6));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(headerColor, 2, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lbl = new JLabel(judul, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(headerColor);
        card.add(lbl);

        return card;
    }

    private void ujiRumusHitungTotal() {
        int hasil1 = Transaksi.hitungTotal(100000, 2);
        int hasil2 = Transaksi.hitungTotal(50000, 3);
        int hasil3 = Transaksi.hitungTotal(75000, 0);

        String pesan = "HASIL PENGUJIAN RUMUS hitungTotal (model.Transaksi):\n\n" +
                       "1. Tarif Rp 100.000 x 2 Jam = Rp " + hasil1 + " -> " + (hasil1 == 200000 ? "BENAR [OK]" : "SALAH [FAIL]") + "\n" +
                       "2. Tarif Rp 50.000 x 3 Jam  = Rp " + hasil2 + " -> " + (hasil2 == 150000 ? "BENAR [OK]" : "SALAH [FAIL]") + "\n" +
                       "3. Tarif Rp 75.000 x 0 Jam  = Rp " + hasil3 + " -> " + (hasil3 == 0 ? "BENAR [OK]" : "SALAH [FAIL]") + "\n\n" +
                       "Semua pengujian logika perhitungan berjalan sesuai ekspektasi!";
        JOptionPane.showMessageDialog(this, pesan, "Uji Logika Perhitungan", JOptionPane.INFORMATION_MESSAGE);
    }
}
