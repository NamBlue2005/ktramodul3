package vn.codegym.repository.impl;

import vn.codegym.model.Student;
import vn.codegym.repository.StudentRepository;
import vn.codegym.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepositoryImpl implements StudentRepository {

    private static final String SELECT_ALL_HOCSINH = "SELECT * FROM HocSinh ORDER BY hoTen ASC;";
    private static final String SELECT_HOCSINH_BY_ID = "SELECT * FROM HocSinh WHERE maHocSinh = ?;";

    @Override
    public List<Student> findAll() {
        List<Student> studentList = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_HOCSINH);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                studentList.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return studentList;
    }

    @Override
    public Student findById(int id) {
        Student student = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_HOCSINH_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    student = mapResultSetToStudent(rs);
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return student;
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        int maHocSinh = rs.getInt("maHocSinh");
        String hoTen = rs.getString("hoTen");
        String lop = rs.getString("lop");
        return new Student(maHocSinh, hoTen, lop);
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