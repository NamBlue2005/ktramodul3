package vn.codegym.repository.impl;

import vn.codegym.model.BorrowCard;
import vn.codegym.model.BorrowDetailDTO;
import vn.codegym.repository.BorrowCardRepository;
import vn.codegym.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowCardRepositoryImpl implements BorrowCardRepository {

    private static final String INSERT_THEMUONSACH = "INSERT INTO TheMuonSach (maMuonSach, maSach, maHocSinh, trangThai, ngayMuon, ngayTra) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_THEMUONSACH_STATUS = "UPDATE TheMuonSach SET trangThai = ? WHERE maMuonSach = ?;";
    private static final String SELECT_THEMUONSACH_BY_ID = "SELECT * FROM TheMuonSach WHERE maMuonSach = ?;";
    private static final String SEARCH_ACTIVE_BORROWS_BASE =
            "SELECT tms.maMuonSach, s.tenSach, hs.hoTen, hs.lop, tms.ngayMuon, tms.ngayTra, tms.maSach " +
                    "FROM TheMuonSach tms " +
                    "JOIN Sach s ON tms.maSach = s.maSach " +
                    "JOIN HocSinh hs ON tms.maHocSinh = hs.maHocSinh " +
                    "WHERE tms.trangThai = true ";
    private static final String GET_STUDENT_NAME_BY_BORROW_CODE =
            "SELECT hs.hoTen FROM HocSinh hs JOIN TheMuonSach tms ON hs.maHocSinh = tms.maHocSinh WHERE tms.maMuonSach = ?;";

    @Override
    public void save(BorrowCard borrowCard) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_THEMUONSACH)) {
            preparedStatement.setString(1, borrowCard.getMaMuonSach());
            preparedStatement.setInt(2, borrowCard.getMaSach());
            preparedStatement.setInt(3, borrowCard.getMaHocSinh());
            preparedStatement.setBoolean(4, borrowCard.isTrangThai());

            if (borrowCard.getNgayMuon() != null) {
                preparedStatement.setDate(5, Date.valueOf(borrowCard.getNgayMuon()));
            } else {
                preparedStatement.setDate(5, null);
            }

            if (borrowCard.getNgayTra() != null) {
                preparedStatement.setDate(6, Date.valueOf(borrowCard.getNgayTra()));
            } else {

                LocalDate defaultNgayTra = LocalDate.now().plusDays(7);
                preparedStatement.setDate(6, Date.valueOf(defaultNgayTra));

            }

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateStatus(String maMuonSach, boolean status) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_THEMUONSACH_STATUS)) {
            preparedStatement.setBoolean(1, status);
            preparedStatement.setString(2, maMuonSach);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public BorrowCard findById(String id) {
        BorrowCard card = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_THEMUONSACH_BY_ID)) {
            preparedStatement.setString(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    card = new BorrowCard(
                            rs.getString("maMuonSach"),
                            rs.getInt("maSach"),
                            rs.getInt("maHocSinh"),
                            rs.getBoolean("trangThai"),
                            rs.getDate("ngayMuon").toLocalDate(),
                            rs.getDate("ngayTra").toLocalDate()
                    );
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return card;
    }

    @Override
    public List<BorrowDetailDTO> searchActiveBorrowedDetails(String searchBookName, String searchStudentName) {
        List<BorrowDetailDTO> borrowList = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder(SEARCH_ACTIVE_BORROWS_BASE);
        List<Object> params = new ArrayList<>();

        if (searchBookName != null && !searchBookName.trim().isEmpty()) {
            sqlBuilder.append("AND s.tenSach LIKE ? ");
            params.add("%" + searchBookName.trim() + "%");
        }
        if (searchStudentName != null && !searchStudentName.trim().isEmpty()) {
            sqlBuilder.append("AND hs.hoTen LIKE ? ");
            params.add("%" + searchStudentName.trim() + "%");
        }
        sqlBuilder.append("ORDER BY tms.ngayTra ASC, s.tenSach ASC;");

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString())) {
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    borrowList.add(mapResultSetToBorrowDetailDTO(rs));
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return borrowList;
    }
    @Override
    public String getStudentNameByBorrowCode(String borrowCode) {
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_STUDENT_NAME_BY_BORROW_CODE)) {
            statement.setString(1, borrowCode);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getString("hoTen");
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return null;
    }

    private BorrowDetailDTO mapResultSetToBorrowDetailDTO(ResultSet rs) throws SQLException {
        return new BorrowDetailDTO(
                rs.getString("maMuonSach"),
                rs.getString("tenSach"),
                rs.getString("hoTen"),
                rs.getString("lop"),
                rs.getDate("ngayMuon").toLocalDate(),
                rs.getDate("ngayTra").toLocalDate(),
                rs.getInt("maSach")
        );
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}