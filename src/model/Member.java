package model;

/**
 * =====================================================================
 * TUGAS: ANGGOTA 1 (Penampung Data / Entity Model)
 * Deskripsi: Model data untuk Member / Pengguna.
 * =====================================================================
 */
public class Member {
    private int idMember;
    private String nama;
    private String username;
    private String password;
    private String noHp;
    private String alamat;

    public Member() {}

    public Member(int idMember, String nama, String username, String password, String noHp, String alamat) {
        this.idMember = idMember;
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.noHp = noHp;
        this.alamat = alamat;
    }

    public int getIdMember() { return idMember; }
    public void setIdMember(int idMember) { this.idMember = idMember; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNoHp() { return noHp; }
    public void setNoHp(String noHp) { this.noHp = noHp; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    @Override
    public String toString() {
        return nama + " (" + username + ")";
    }
}
