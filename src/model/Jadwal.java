package model;

/**
 * =====================================================================
 * TUGAS: ANGGOTA 1 (Penampung Data / Entity Model)
 * Deskripsi: Model data untuk Jadwal Ketersediaan Lapangan.
 * =====================================================================
 */
public class Jadwal {
    private int idJadwal;
    private int idLapangan;
    private String namaLapangan;
    private String tanggal;
    private String jamMulai;
    private String jamSelesai;
    private String status;

    public Jadwal() {}

    public Jadwal(int idJadwal, int idLapangan, String namaLapangan, String tanggal, String jamMulai, String jamSelesai, String status) {
        this.idJadwal = idJadwal;
        this.idLapangan = idLapangan;
        this.namaLapangan = namaLapangan;
        this.tanggal = tanggal;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.status = status;
    }

    public int getIdJadwal() { return idJadwal; }
    public void setIdJadwal(int idJadwal) { this.idJadwal = idJadwal; }

    public int getIdLapangan() { return idLapangan; }
    public void setIdLapangan(int idLapangan) { this.idLapangan = idLapangan; }

    public String getNamaLapangan() { return namaLapangan; }
    public void setNamaLapangan(String namaLapangan) { this.namaLapangan = namaLapangan; }

    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }

    public String getJamMulai() { return jamMulai; }
    public void setJamMulai(String jamMulai) { this.jamMulai = jamMulai; }

    public String getJamSelesai() { return jamSelesai; }
    public void setJamSelesai(String jamSelesai) { this.jamSelesai = jamSelesai; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return tanggal + " (" + jamMulai + " - " + jamSelesai + ") [" + status + "]";
    }
}
