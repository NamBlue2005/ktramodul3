package vn.codegym.model;

import java.time.LocalDate;

public class BorrowCard {
    private String maMuonSach;
    private int maSach;
    private int maHocSinh;
    private boolean trangThai;
    private LocalDate ngayMuon;
    private LocalDate ngayTra;

    public BorrowCard() {}

    public BorrowCard(String maMuonSach, int maSach, int maHocSinh, boolean trangThai, LocalDate ngayMuon, LocalDate ngayTra) {
        this.maMuonSach = maMuonSach;
        this.maSach = maSach;
        this.maHocSinh = maHocSinh;
        this.trangThai = trangThai;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
    }

    public String getMaMuonSach() { return maMuonSach; }
    public void setMaMuonSach(String maMuonSach) { this.maMuonSach = maMuonSach; }
    public int getMaSach() { return maSach; }
    public void setMaSach(int maSach) { this.maSach = maSach; }
    public int getMaHocSinh() { return maHocSinh; }
    public void setMaHocSinh(int maHocSinh) { this.maHocSinh = maHocSinh; }
    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
    public LocalDate getNgayMuon() { return ngayMuon; }
    public void setNgayMuon(LocalDate ngayMuon) { this.ngayMuon = ngayMuon; }
    public LocalDate getNgayTra() { return ngayTra; }
    public void setNgayTra(LocalDate ngayTra) { this.ngayTra = ngayTra; }

}