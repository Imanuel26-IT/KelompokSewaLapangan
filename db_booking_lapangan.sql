-- =====================================================================
-- DATABASE: db_booking_lapangan
-- Sistem Booking Lapangan Olahraga (Tanpa DAO)
-- Kolaborasi Kelompok Sewa Lapangan
-- =====================================================================

DROP DATABASE IF EXISTS db_booking_lapangan;
CREATE DATABASE db_booking_lapangan;
USE db_booking_lapangan;

-- ---------------------------------------------------------------------
-- 1. TABEL ADMIN
-- Dikelola oleh: Anggota 2 & Anggota 3
-- ---------------------------------------------------------------------
CREATE TABLE admin (
    id_admin INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nama_admin VARCHAR(100) NOT NULL
);

-- ---------------------------------------------------------------------
-- 2. TABEL MEMBER
-- Dikelola oleh: Anggota 3 (FormLogin, FormRegistrasi)
-- Model data oleh: Anggota 1 (model.Member)
-- ---------------------------------------------------------------------
CREATE TABLE member (
    id_member INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    no_hp VARCHAR(20) NOT NULL,
    alamat TEXT
);

-- ---------------------------------------------------------------------
-- 3. TABEL LAPANGAN
-- Dikelola oleh: Anggota 2 (FormLapangan, FormHarga)
-- Model data oleh: Anggota 1 (model.Lapangan)
-- ---------------------------------------------------------------------
CREATE TABLE lapangan (
    id_lapangan INT AUTO_INCREMENT PRIMARY KEY,
    nama_lapangan VARCHAR(100) NOT NULL,
    jenis VARCHAR(50) NOT NULL,
    harga_per_jam INT NOT NULL,
    status VARCHAR(30) DEFAULT 'Tersedia'
);

-- ---------------------------------------------------------------------
-- 4. TABEL JADWAL
-- Dikelola oleh: Anggota 2 (FormJadwal)
-- Model data oleh: Anggota 1 (model.Jadwal)
-- ---------------------------------------------------------------------
CREATE TABLE jadwal (
    id_jadwal INT AUTO_INCREMENT PRIMARY KEY,
    id_lapangan INT NOT NULL,
    tanggal DATE NOT NULL,
    jam_mulai TIME NOT NULL,
    jam_selesai TIME NOT NULL,
    status VARCHAR(30) DEFAULT 'Tersedia',
    FOREIGN KEY (id_lapangan) REFERENCES lapangan(id_lapangan) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- 5. TABEL BOOKING
-- Dikelola oleh: Anggota 4 (FormBooking)
-- Model data oleh: Anggota 1 (model.Booking)
-- ---------------------------------------------------------------------
CREATE TABLE booking (
    id_booking INT AUTO_INCREMENT PRIMARY KEY,
    id_member INT NOT NULL,
    id_jadwal INT NOT NULL,
    tanggal_booking DATE NOT NULL,
    status_booking VARCHAR(40) DEFAULT 'Menunggu Pembayaran',
    FOREIGN KEY (id_member) REFERENCES member(id_member) ON DELETE CASCADE,
    FOREIGN KEY (id_jadwal) REFERENCES jadwal(id_jadwal) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- 6. TABEL TRANSAKSI
-- Dikelola oleh: Kamu / Valen (FormTransaksi, FormLaporan)
-- Model data oleh: Anggota 1 (model.Transaksi - ada rumus hitungTotal)
-- ---------------------------------------------------------------------
CREATE TABLE transaksi (
    id_transaksi INT AUTO_INCREMENT PRIMARY KEY,
    id_booking INT NOT NULL,
    tanggal_transaksi DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_harga INT NOT NULL,
    metode_pembayaran VARCHAR(50) NOT NULL,
    status_pembayaran VARCHAR(30) DEFAULT 'Belum Lunas',
    FOREIGN KEY (id_booking) REFERENCES booking(id_booking) ON DELETE CASCADE
);

-- =====================================================================
-- DUMMY DATA UNTUK PENGUJIAN APLIKASI
-- =====================================================================

-- 1. Admin
INSERT INTO admin (username, password, nama_admin) VALUES
('admin', 'admin123', 'Administrator Utama'),
('operator', 'operator123', 'Operator Lapangan');

-- 2. Member
INSERT INTO member (nama, username, password, no_hp, alamat) VALUES
('Valen Linggi', 'valen', '12345', '081234567890', 'Salatiga'),
('Budi Santoso', 'budi', '12345', '081234567891', 'Semarang'),
('Siti Rahma', 'siti', '12345', '081234567892', 'Solo'),
('Andi Wijaya', 'andi', '12345', '081234567893', 'Yogyakarta'),
('Rina Marlina', 'rina', '12345', '081234567894', 'Salatiga');

-- 3. Lapangan
INSERT INTO lapangan (nama_lapangan, jenis, harga_per_jam, status) VALUES
('Lapangan Futsal A (Vinyl)', 'Futsal', 100000, 'Tersedia'),
('Lapangan Futsal B (Rumput Sintetis)', 'Futsal', 120000, 'Tersedia'),
('Lapangan Badminton 1', 'Badminton', 50000, 'Tersedia'),
('Lapangan Badminton 2', 'Badminton', 50000, 'Tersedia'),
('Lapangan Basket Semi-Indoor', 'Basket', 80000, 'Tersedia');

-- 4. Jadwal
INSERT INTO jadwal (id_lapangan, tanggal, jam_mulai, jam_selesai, status) VALUES
(1, '2026-11-21', '08:00:00', '10:00:00', 'Dipesan'),
(1, '2026-11-21', '10:00:00', '12:00:00', 'Tersedia'),
(1, '2026-11-21', '15:00:00', '17:00:00', 'Dipesan'),
(2, '2026-11-21', '09:00:00', '11:00:00', 'Dipesan'),
(2, '2026-11-21', '13:00:00', '15:00:00', 'Tersedia'),
(3, '2026-11-21', '08:00:00', '10:00:00', 'Dipesan'),
(3, '2026-11-21', '14:00:00', '16:00:00', 'Tersedia'),
(4, '2026-11-21', '16:00:00', '18:00:00', 'Tersedia'),
(5, '2026-11-21', '18:00:00', '20:00:00', 'Tersedia');

-- 5. Booking
INSERT INTO booking (id_member, id_jadwal, tanggal_booking, status_booking) VALUES
(1, 1, '2026-11-20', 'Terkonfirmasi'),
(2, 3, '2026-11-20', 'Menunggu Pembayaran'),
(3, 4, '2026-11-20', 'Terkonfirmasi'),
(4, 6, '2026-11-20', 'Menunggu Pembayaran');

-- 6. Transaksi
-- Rumus: total_harga = harga_per_jam * durasi
-- Booking 1: Lapangan 1 (100.000) x 2 jam = 200.000
-- Booking 3: Lapangan 2 (120.000) x 2 jam = 240.000
INSERT INTO transaksi (id_booking, tanggal_transaksi, total_harga, metode_pembayaran, status_pembayaran) VALUES
(1, '2026-11-20 10:15:00', 200000, 'Cash', 'Lunas'),
(3, '2026-11-20 11:30:00', 240000, 'QRIS', 'Lunas');
