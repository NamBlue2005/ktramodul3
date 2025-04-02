package vn.codegym.controller;

import vn.codegym.model.Book;
import vn.codegym.repository.BookRepository;
import vn.codegym.repository.BorrowCardRepository;
import vn.codegym.repository.impl.BookRepositoryImpl;
import vn.codegym.repository.impl.BorrowCardRepositoryImpl;
import vn.codegym.util.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "ReturnBookServlet", urlPatterns = "/return-book")
public class ReturnBookServlet extends HttpServlet {
    private BorrowCardRepository borrowCardRepository = new BorrowCardRepositoryImpl();
    private BookRepository bookRepository = new BookRepositoryImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String maMuonSach = request.getParameter("maMuonSach");
        String maSachStr = request.getParameter("maSach");

        Connection conn = null;
        try {
            int maSach = Integer.parseInt(maSachStr);
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            borrowCardRepository.updateStatus(maMuonSach, false);
            bookRepository.updateQuantity(maSach, 1);

            // Lấy thông tin sách và học sinh để hiển thị trong thông báo
            Book book = bookRepository.findById(maSach);
            String studentName = borrowCardRepository.getStudentNameByBorrowCode(maMuonSach);

            conn.commit();

            // Thêm thông tin vào request
            request.setAttribute("returnedBookName", book.getTenSach());
            request.setAttribute("returnedBorrowCode", maMuonSach);
            request.setAttribute("returnedStudentName", studentName);

            response.sendRedirect(request.getContextPath() + "/borrowed-books?success=Returned");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/borrowed-books?error=InvalidBookId");
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { printSQLException(ex); }
            response.sendRedirect(request.getContextPath() + "/borrowed-books?error=SystemError");
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { printSQLException(e); }
        }
    }
    private void printSQLException(SQLException ex) {
        ex.printStackTrace();
    }
}