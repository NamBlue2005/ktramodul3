package vn.codegym.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BorrowDetailDTO {
    private String maMuonSach;
    private String tenSach;
    private String hoTenHocSinh;
    private String lop;
    private LocalDate ngayMuon;
    private LocalDate ngayTra;
    private int maSach;

    public BorrowDetailDTO() {}

    public BorrowDetailDTO(String maMuonSach, String tenSach, String hoTenHocSinh, String lop, LocalDate ngayMuon, LocalDate ngayTra, int maSach) {
        this.maMuonSach = maMuonSach;
        this.tenSach = tenSach;
        this.hoTenHocSinh = hoTenHocSinh;
        this.lop = lop;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
        this.maSach = maSach;
    }

    public String getMaMuonSach() { return maMuonSach; }
    public void setMaMuonSach(String maMuonSach) { this.maMuonSach = maMuonSach; }
    public String getTenSach() { return tenSach; }
    public void setTenSach(String tenSach) { this.tenSach = tenSach; }
    public String getHoTenHocSinh() { return hoTenHocSinh; }
    public void setHoTenHocSinh(String hoTenHocSinh) { this.hoTenHocSinh = hoTenHocSinh; }
    public String getLop() { return lop; }
    public void setLop(String lop) { this.lop = lop; }
    public LocalDate getNgayMuon() { return ngayMuon; }
    public void setNgayMuon(LocalDate ngayMuon) { this.ngayMuon = ngayMuon; }
    public LocalDate getNgayTra() { return ngayTra; }
    public void setNgayTra(LocalDate ngayTra) { this.ngayTra = ngayTra; }
    public int getMaSach() { return maSach; }
    public void setMaSach(int maSach) { this.maSach = maSach; }

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String getFormattedNgayMuon() {
        return ngayMuon != null ? ngayMuon.format(DATE_FORMATTER) : "";
    }

    public String getFormattedNgayTra() {
        return ngayTra != null ? ngayTra.format(DATE_FORMATTER) : "";
    }

}