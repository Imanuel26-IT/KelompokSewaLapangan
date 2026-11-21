package model;

/**
 * =====================================================================
 * TUGAS: ANGGOTA 1 (Penampung Data / Entity Model)
 * Deskripsi: Model data untuk Transaksi Pemesanan / Booking.
 * =====================================================================
 */
public class Booking {
    private int idBooking;
    private int idMember;
    private String namaMember;
    private int idJadwal;
    private String namaLapangan;
    private String tanggalJadwal;
    private String jam;
    private int hargaPerJam;
    private String tanggalBooking;
    private String statusBooking;

    public Booking() {}

    public Booking(int idBooking, int idMember, String namaMember, int idJadwal, 
                   String namaLapangan, String tanggalJadwal, String jam, 
                   int hargaPerJam, String tanggalBooking, String statusBooking) {
        this.idBooking = idBooking;
        this.idMember = idMember;
        this.namaMember = namaMember;
        this.idJadwal = idJadwal;
        this.namaLapangan = namaLapangan;
        this.tanggalJadwal = tanggalJadwal;
        this.jam = jam;
        this.hargaPerJam = hargaPerJam;
        this.tanggalBooking = tanggalBooking;
        this.statusBooking = statusBooking;
    }

    public int getIdBooking() { return idBooking; }
    public void setIdBooking(int idBooking) { this.idBooking = idBooking; }

    public int getIdMember() { return idMember; }
    public void setIdMember(int idMember) { this.idMember = idMember; }

    public String getNamaMember() { return namaMember; }
    public void setNamaMember(String namaMember) { this.namaMember = namaMember; }

    public int getIdJadwal() { return idJadwal; }
    public void setIdJadwal(int idJadwal) { this.idJadwal = idJadwal; }

    public String getNamaLapangan() { return namaLapangan; }
    public void setNamaLapangan(String namaLapangan) { this.namaLapangan = namaLapangan; }

    public String getTanggalJadwal() { return tanggalJadwal; }
    public void setTanggalJadwal(String tanggalJadwal) { this.tanggalJadwal = tanggalJadwal; }

    public String getJam() { return jam; }
    public void setJam(String jam) { this.jam = jam; }

    public int getHargaPerJam() { return hargaPerJam; }
    public void setHargaPerJam(int hargaPerJam) { this.hargaPerJam = hargaPerJam; }

    public String getTanggalBooking() { return tanggalBooking; }
    public void setTanggalBooking(String tanggalBooking) { this.tanggalBooking = tanggalBooking; }

    public String getStatusBooking() { return statusBooking; }
    public void setStatusBooking(String statusBooking) { this.statusBooking = statusBooking; }

    @Override
    public String toString() {
        return "Booking #" + idBooking + " - " + namaMember + " (" + namaLapangan + ")";
    }
}
