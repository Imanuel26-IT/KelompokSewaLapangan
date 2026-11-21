package model;

/**
 * =====================================================================
 * TUGAS: ANGGOTA 1 (Penampung Data / Entity Model)
 * Deskripsi: Model data untuk Transaksi Pembayaran dan Rumus Hitung Total.
 * =====================================================================
 */
public class Transaksi {
    private int idTransaksi;
    private int idBooking;
    private String namaMember;
    private String namaLapangan;
    private String tanggalJadwal;
    private String jam;
    private int hargaPerJam;
    private int durasiJam;
    private int totalHarga;
    private String metodePembayaran;
    private String statusPembayaran;
    private String tanggalTransaksi;

    public Transaksi() {}

    public Transaksi(int idTransaksi, int idBooking, String namaMember, String namaLapangan, 
                     String tanggalJadwal, String jam, int hargaPerJam, int durasiJam, 
                     int totalHarga, String metodePembayaran, String statusPembayaran, 
                     String tanggalTransaksi) {
        this.idTransaksi = idTransaksi;
        this.idBooking = idBooking;
        this.namaMember = namaMember;
        this.namaLapangan = namaLapangan;
        this.tanggalJadwal = tanggalJadwal;
        this.jam = jam;
        this.hargaPerJam = hargaPerJam;
        this.durasiJam = durasiJam;
        this.totalHarga = totalHarga;
        this.metodePembayaran = metodePembayaran;
        this.statusPembayaran = statusPembayaran;
        this.tanggalTransaksi = tanggalTransaksi;
    }

    // Getter & Setter
    public int getIdTransaksi() { return idTransaksi; }
    public void setIdTransaksi(int idTransaksi) { this.idTransaksi = idTransaksi; }

    public int getIdBooking() { return idBooking; }
    public void setIdBooking(int idBooking) { this.idBooking = idBooking; }

    public String getNamaMember() { return namaMember; }
    public void setNamaMember(String namaMember) { this.namaMember = namaMember; }

    public String getNamaLapangan() { return namaLapangan; }
    public void setNamaLapangan(String namaLapangan) { this.namaLapangan = namaLapangan; }

    public String getTanggalJadwal() { return tanggalJadwal; }
    public void setTanggalJadwal(String tanggalJadwal) { this.tanggalJadwal = tanggalJadwal; }

    public String getJam() { return jam; }
    public void setJam(String jam) { this.jam = jam; }

    public int getHargaPerJam() { return hargaPerJam; }
    public void setHargaPerJam(int hargaPerJam) { this.hargaPerJam = hargaPerJam; }

    public int getDurasiJam() { return durasiJam; }
    public void setDurasiJam(int durasiJam) { this.durasiJam = durasiJam; }

    public int getTotalHarga() { return totalHarga; }
    public void setTotalHarga(int totalHarga) { this.totalHarga = totalHarga; }

    public String getMetodePembayaran() { return metodePembayaran; }
    public void setMetodePembayaran(String metodePembayaran) { this.metodePembayaran = metodePembayaran; }

    public String getStatusPembayaran() { return statusPembayaran; }
    public void setStatusPembayaran(String statusPembayaran) { this.statusPembayaran = statusPembayaran; }

    public String getTanggalTransaksi() { return tanggalTransaksi; }
    public void setTanggalTransaksi(String tanggalTransaksi) { this.tanggalTransaksi = tanggalTransaksi; }

    /**
     * =====================================================================
     * RUMUS HITUNG TOTAL: hargaPerJam * durasiJam
     * Sesuai catatan: (ada rumus hitungTotal)
     * =====================================================================
     */
    public static int hitungTotal(int hargaPerJam, int durasiJam) {
        if (hargaPerJam <= 0 || durasiJam <= 0) {
            return 0;
        }
        return hargaPerJam * durasiJam;
    }

    public boolean isValid() {
        int expected = hitungTotal(hargaPerJam, durasiJam);
        return totalHarga == expected && totalHarga > 0;
    }
}
