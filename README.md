# ⚽ KelompokSewaLapangan - Sistem Booking Lapangan Olahraga (Tanpa DAO)

Repositori resmi untuk proyek kelompok mata kuliah Pemrograman Berorientasi Objek (PBO) / Tugas Kelompok. Aplikasi desktop dibangun menggunakan **Java Swing** dan terhubung ke basis data **MySQL (JDBC)** dengan arsitektur **Tanpa DAO** (query SQL dieksekusi langsung di dalam class GUI masing-masing modul).

---

## 📁 Struktur Folder Proyek

```
KelompokSewaLapangan/
├── build.xml                           # Script Ant Build untuk NetBeans
├── manifest.mf                         # Java Manifest file
├── .gitignore                          # Ignore build & cache IDE
├── db_booking_lapangan.sql             # Skrip database MySQL & dummy data
├── README.md                           # Dokumentasi proyek & pembagian tugas
├── lib/
│   └── mysql-connector-j-26.10.0.jar  # Driver JDBC MySQL
├── nbproject/                          # Konfigurasi Apache NetBeans
│   ├── build-impl.xml
│   ├── genfiles.properties
│   ├── project.properties
│   └── project.xml
└── src/
    ├── main/
    │   └── Main.java                   # Main Entry Point (Menjalankan MainMenu)
    ├── koneksi/
    │   └── Koneksi.java                <- Anggota 1 (Koneksi Database JDBC)
    ├── model/                          <- Anggota 1 (Penampung Data / Entity)
    │   ├── Member.java
    │   ├── Lapangan.java
    │   ├── Jadwal.java
    │   ├── Booking.java
    │   └── Transaksi.java              # Ada rumus hitungTotal(harga, durasi)
    └── gui/                            <- SEMUA QUERY ADA DI SINI (Tanpa DAO)
        ├── MainMenu.java               # Dashboard Menu Utama
        ├── admin/                      <- Anggota 2
        │   ├── FormLapangan.java       # Query CRUD data lapangan
        │   ├── FormJadwal.java         # Query kelola jadwal lapangan
        │   └── FormHarga.java          # Query update tarif harga per jam
        ├── member/                     <- Anggota 3
        │   ├── FormLogin.java          # Query autentikasi user / admin
        │   └── FormRegistrasi.java     # Query insert pendaftaran member
        ├── booking/                    <- Anggota 4
        │   └── FormBooking.java        # Query pemesanan jadwal lapangan
        └── transaksi/                  <- Kamu (Valen)
            ├── FormTransaksi.java      # Query transaksi & hitung total
            └── FormLaporan.java        # Query rekapitulasi & pembatalan
```

---

## 👥 Pembagian Tugas Anggota Kelompok

| Bagian | Anggota | File / Modul | Deskripsi Tugas |
|---|---|---|---|
| **Koneksi & Model** | **Anggota 1** | `koneksi/Koneksi.java`<br>`model/*.java` | - Membuat koneksi JDBC ke MySQL (`db_booking_lapangan`).<br>- Membuat class entity model data: `Member`, `Lapangan`, `Jadwal`, `Booking`, dan `Transaksi` (lengkap dengan rumus `hitungTotal`). |
| **Admin** | **Anggota 2** | `gui/admin/FormLapangan.java`<br>`gui/admin/FormJadwal.java`<br>`gui/admin/FormHarga.java` | - Mengelola CRUD data lapangan olahraga.<br>- Mengatur jadwal ketersediaan lapangan.<br>- Mengatur tarif dan harga sewa per jam lapangan. |
| **Member** | **Anggota 3** | `gui/member/FormLogin.java`<br>`gui/member/FormRegistrasi.java` | - Form autentikasi login untuk Member dan Admin.<br>- Form pendaftaran member baru ke sistem. |
| **Booking** | **Anggota 4** | `gui/booking/FormBooking.java` | - Memproses booking jadwal lapangan oleh member.<br>- Mengubah status jadwal lapangan menjadi *Dipesan* secara otomatis. |
| **Transaksi & Laporan** | **Kamu (Valen)** | `gui/transaksi/FormTransaksi.java`<br>`gui/transaksi/FormLaporan.java` | - Memproses pembayaran booking dan menghitung total harga otomatis (`harga/jam * durasi`).<br>- Menampilkan rekapitulasi transaksi pendapatan serta pembatalan booking. |

---

## 🚀 Panduan Menjalankan Proyek

### 1. Persiapan Database MySQL (XAMPP)
1. Buka **XAMPP Control Panel**, nyalakan modul **Apache** dan **MySQL** (klik *Start*).
2. Buka browser dan kunjungi [http://localhost/phpmyadmin](http://localhost/phpmyadmin).
3. Buat database baru bernama `db_booking_lapangan` (atau klik tab **Import**).
4. Pilih file **`db_booking_lapangan.sql`** yang tersedia di repositori ini, lalu klik **Import / Go**.
5. *Catatan:* Jika MySQL XAMPP Anda menggunakan password, buka `src/koneksi/Koneksi.java` dan sesuaikan variabel `PASS = "password_anda"`.

### 2. Membuka & Menjalankan di Apache NetBeans
1. Buka **Apache NetBeans IDE**.
2. Pilih menu **File** -> **Open Project...** (`Ctrl + Shift + O`).
3. Cari dan pilih folder repositori **`KelompokSewaLapangan`** (ikon cangkir kopi Java akan terdeteksi otomatis).
4. Klik **Open Project**. Driver MySQL di folder `lib/` sudah otomatis terhubung ke classpath proyek.
5. Jalankan aplikasi dengan menekan tombol **F6** (atau klik kanan proyek -> **Run**).
6. Main Class: `main.Main`.

### 3. Menjalankan Lewat Terminal / Command Prompt
```bash
# Clone repositori
git clone https://github.com/Imanuel26-IT/KelompokSewaLapangan.git
cd KelompokSewaLapangan

# Kompilasi semua file Java
javac -cp "lib/mysql-connector-j-26.10.0.jar;src" -d bin src/main/*.java src/koneksi/*.java src/model/*.java src/gui/*.java src/gui/admin/*.java src/gui/member/*.java src/gui/booking/*.java src/gui/transaksi/*.java

# Jalankan aplikasi
java -cp "lib/mysql-connector-j-26.10.0.jar;bin" main.Main
```

---

## 💡 Konsep Arsitektur Tanpa DAO
Pada proyek ini, seluruh query SQL (`SELECT`, `INSERT`, `UPDATE`, `DELETE`) dituliskan secara langsung di dalam method handler event masing-masing Form GUI (Swing). Pendekatan ini mempermudah setiap anggota kelompok untuk mengerjakan dan menguji bagian form-nya masing-masing secara independen tanpa ketergantungan layer DAO terpusat.
