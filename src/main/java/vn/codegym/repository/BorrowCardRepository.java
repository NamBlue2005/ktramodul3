package vn.codegym.repository;

import vn.codegym.model.BorrowCard;
import vn.codegym.model.BorrowDetailDTO;

import java.sql.SQLException;
import java.util.List;

public interface BorrowCardRepository {
    void save(BorrowCard borrowCard) throws SQLException;
    void updateStatus(String maMuonSach, boolean status) throws SQLException;
    BorrowCard findById(String id);
    List<BorrowDetailDTO> searchActiveBorrowedDetails(String searchBookName, String searchStudentName);
    String getStudentNameByBorrowCode(String borrowCode);
}