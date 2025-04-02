package vn.codegym.repository.impl;

import vn.codegym.model.Book;
import vn.codegym.repository.BookRepository;
import vn.codegym.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {

    private static final String SELECT_ALL_SACH = "SELECT * FROM Sach ORDER BY tenSach ASC;";
    private static final String SELECT_SACH_BY_ID = "SELECT * FROM Sach WHERE maSach = ?;";
    private static final String UPDATE_SACH_QUANTITY = "UPDATE Sach SET soLuong = soLuong + ? WHERE maSach = ?;";

    @Override
    public List<Book> findAll() {
        List<Book> bookList = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_SACH);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                bookList.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return bookList;
    }

    @Override
    public Book findById(int id) {
        Book book = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SACH_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    book = mapResultSetToBook(rs);
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return book;
    }

    @Override
    public void updateQuantity(int id, int change) throws SQLException {
        Book book = findById(id);
        if (book == null) {
            throw new SQLException("Book not found with ID: " + id);
        }
        if (book.getSoLuong() + change < 0) {
            throw new SQLException("Cannot decrease quantity below zero for book ID: " + id);
        }
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SACH_QUANTITY)) {
            preparedStatement.setInt(1, change);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        }
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        int maSach = rs.getInt("maSach");
        String tenSach = rs.getString("tenSach");
        String tacGia = rs.getString("tacGia");
        String moTa = rs.getString("moTa");
        int soLuong = rs.getInt("soLuong");
        return new Book(maSach, tenSach, tacGia, moTa, soLuong);
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