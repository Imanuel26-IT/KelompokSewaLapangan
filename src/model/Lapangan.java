package model;

/**
 * =====================================================================
 * TUGAS: ANGGOTA 1 (Penampung Data / Entity Model)
 * Deskripsi: Model data untuk Lapangan Olahraga.
 * =====================================================================
 */
public class Lapangan {
    private int idLapangan;
    private String namaLapangan;
    private String jenis;
    private int hargaPerJam;
    private String status;

    public Lapangan() {}

    public Lapangan(int idLapangan, String namaLapangan, String jenis, int hargaPerJam, String status) {
        this.idLapangan = idLapangan;
        this.namaLapangan = namaLapangan;
        this.jenis = jenis;
        this.hargaPerJam = hargaPerJam;
        this.status = status;
    }

    public int getIdLapangan() { return idLapangan; }
    public void setIdLapangan(int idLapangan) { this.idLapangan = idLapangan; }

    public String getNamaLapangan() { return namaLapangan; }
    public void setNamaLapangan(String namaLapangan) { this.namaLapangan = namaLapangan; }

    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }

    public int getHargaPerJam() { return hargaPerJam; }
    public void setHargaPerJam(int hargaPerJam) { this.hargaPerJam = hargaPerJam; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return namaLapangan + " - Rp " + hargaPerJam + "/jam";
    }
}
