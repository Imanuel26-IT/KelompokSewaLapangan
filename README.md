# ⚽ KelompokSewaLapangan - Sistem Booking Lapangan Olahraga

Repositori awal (starter template) untuk proyek kelompok mata kuliah Pemrograman Berorientasi Objek (PBO) / Tugas Kelompok. Seluruh folder arsitektur proyek telah disiapkan sesuai kaidah **MVC (Model-View-Controller) tanpa DAO** (query langsung pada form/modul).

File implementasi `.java` sengaja dikosongkan agar masing-masing anggota kelompok dapat membuat dan mengisi kodenya sendiri sesuai pembagian tugas.

---

## 📁 Struktur Folder Proyek (MVC Tanpa DAO)

```
KelompokSewaLapangan/
├── .gitignore                          # Pengaturan ignore build & cache NetBeans
├── README.md                           # Panduan struktur folder & tugas anggota
├── db_booking_lapangan.sql             # Skema basis data MySQL & data dummy awal
├── lib/
│   └── mysql-connector-j-26.10.0.jar  # Driver JDBC MySQL (siap pakai)
├── nbproject/                          # Konfigurasi Apache NetBeans IDE
│   ├── build-impl.xml
│   ├── genfiles.properties
│   ├── project.properties
│   └── project.xml
├── build.xml                           # Ant build script NetBeans
├── manifest.mf
└── src/
    ├── main/                           # Entry Point aplikasi (Main.java)
    ├── koneksi/                        # Koneksi database MySQL JDBC (Koneksi.java) <- Anggota 1
    ├── model/                          # [M - Model] Class Entity Data Penampung <- Anggota 1
    │   ├── Member.java
    │   ├── Lapangan.java
    │   ├── Jadwal.java
    │   ├── Booking.java
    │   └── Transaksi.java              # (Ada rumus hitungTotal)
    ├── controller/                     # [C - Controller] Controller MVC (jika digunakan)
    └── gui/                            # [V - View] Form Antarmuka Swing (Tanpa DAO)
        ├── MainMenu.java               # Dashboard Menu Utama
        ├── admin/                      # Form Admin <- Anggota 2
        │   ├── FormLapangan.java
        │   ├── FormJadwal.java
        │   └── FormHarga.java
        ├── member/                     # Form Member <- Anggota 3
        │   ├── FormLogin.java
        │   └── FormRegistrasi.java
        ├── booking/                    # Form Booking <- Anggota 4
        │   └── FormBooking.java
        └── transaksi/                  # Form Transaksi & Laporan <- Kamu (Valen)
            ├── FormTransaksi.java
            └── FormLaporan.java
```

---

## 👥 Panduan Pembagian Tugas Anggota Kelompok

Setiap anggota kelompok silakan membuat file baru di dalam package masing-masing melalui Apache NetBeans (**Klik kanan package > New > Java Class...** atau **New > JFrame Form...**):

| Bagian | Anggota | Folder / Package | File yang Perlu Dibuat | Deskripsi Tugas |
|---|---|---|---|---|
| **Koneksi & Model** | **Anggota 1** | `src/koneksi/`<br>`src/model/` | `Koneksi.java`<br>`Member.java`<br>`Lapangan.java`<br>`Jadwal.java`<br>`Booking.java`<br>`Transaksi.java` | - Mengatur koneksi JDBC MySQL.<br>- Membuat class entity (atribut, constructor, getter/setter) dan rumus `hitungTotal` pada `Transaksi.java`. |
| **Admin** | **Anggota 2** | `src/gui/admin/` | `FormLapangan.java`<br>`FormJadwal.java`<br>`FormHarga.java` | Membuat GUI dan query database untuk mengelola data lapangan, jadwal operasional, dan tarif sewa per jam. |
| **Member** | **Anggota 3** | `src/gui/member/` | `FormLogin.java`<br>`FormRegistrasi.java` | Membuat GUI dan query autentikasi login (Admin/Member) serta pendaftaran akun member baru. |
| **Booking** | **Anggota 4** | `src/gui/booking/` | `FormBooking.java` | Membuat GUI dan query pemesanan lapangan serta mengubah status jadwal menjadi *Dipesan*. |
| **Transaksi & Laporan** | **Kamu (Valen)** | `src/gui/transaksi/` | `FormTransaksi.java`<br>`FormLaporan.java` | Membuat GUI transaksi pembayaran dengan perhitungan total otomatis, rekapitulasi laporan pendapatan, dan pembatalan transaksi. |
| **Integrasi Utama** | **Tim** | `src/main/`<br>`src/gui/` | `Main.java`<br>`MainMenu.java` | Dashboard utama yang menghubungkan seluruh form dan menjalankan aplikasi. |

---

## 🚀 Cara Menjalankan di Apache NetBeans
1. Nyalakan modul **Apache** dan **MySQL** di XAMPP Control Panel.
2. Import file **`db_booking_lapangan.sql`** ke phpMyAdmin.
3. Buka Apache NetBeans > **File** > **Open Project...** > pilih folder **`KelompokSewaLapangan`**.
4. Driver MySQL di folder `lib/` sudah otomatis terpasang.
