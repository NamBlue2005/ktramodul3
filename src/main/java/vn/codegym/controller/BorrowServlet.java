package vn.codegym.controller;

import vn.codegym.model.Student;
import vn.codegym.model.Book;
import vn.codegym.model.BorrowCard;
import vn.codegym.repository.StudentRepository;
import vn.codegym.repository.BookRepository;
import vn.codegym.repository.BorrowCardRepository;
import vn.codegym.repository.impl.StudentRepositoryImpl;
import vn.codegym.repository.impl.BookRepositoryImpl;
import vn.codegym.repository.impl.BorrowCardRepositoryImpl;
import vn.codegym.util.DatabaseConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet(name = "BorrowServlet", urlPatterns = "/borrow")
public class BorrowServlet extends HttpServlet {
    private BookRepository bookRepository = new BookRepositoryImpl();
    private StudentRepository studentRepository = new StudentRepositoryImpl();
    private BorrowCardRepository borrowCardRepository = new BorrowCardRepositoryImpl();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Pattern BORROW_CODE_PATTERN = Pattern.compile("^MS-\\d{4}$");

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String bookIdStr = request.getParameter("bookId");

        try {
            int bookId = Integer.parseInt(bookIdStr);
            Book book = bookRepository.findById(bookId);

            if (book == null) {
                response.sendRedirect(request.getContextPath() + "/books?error=BookNotFound");
                return;
            }

            List<Student> studentList = studentRepository.findAll();
            String borrowDateStr = LocalDate.now().format(DISPLAY_FORMATTER);

            request.setAttribute("sach", book);
            request.setAttribute("hocSinhList", studentList);
            request.setAttribute("ngayMuonDisplay", borrowDateStr);
            request.setAttribute("today", LocalDate.now().format(DATE_FORMATTER));

            RequestDispatcher dispatcher = request.getRequestDispatcher("/book/borrow.jsp");
            dispatcher.forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/books?error=InvalidBookId");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String maMuonSach = request.getParameter("maMuonSach");
        String maSachStr = request.getParameter("maSach");
        String maHocSinhStr = request.getParameter("maHocSinh");
        String ngayTraStr = request.getParameter("ngayTra");

        int maSach = -1;
        int maHocSinh = -1;
        LocalDate ngayMuon = LocalDate.now();
        LocalDate ngayTra = null;
        Book book = null;

        boolean hasError = false;
        StringBuilder errors = new StringBuilder();

        if (maMuonSach == null || !BORROW_CODE_PATTERN.matcher(maMuonSach).matches()) {
            errors.append("Mã mượn sách không hợp lệ (Phải là MS-XXXX).<br>");
            hasError = true;
        } else {
            if (borrowCardRepository.findById(maMuonSach) != null) {
                errors.append("Mã mượn sách đã tồn tại.<br>");
                hasError = true;
            }
        }

        try {
            maSach = Integer.parseInt(maSachStr);
            book = bookRepository.findById(maSach);
            if (book == null) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            errors.append("Mã sách không hợp lệ.<br>"); hasError = true;
        }
        try {
            maHocSinh = Integer.parseInt(maHocSinhStr);
            if (studentRepository.findById(maHocSinh) == null) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            errors.append("Mã học sinh không hợp lệ.<br>"); hasError = true;
        }

        if (!hasError && book != null) {
            book = bookRepository.findById(maSach);
            if(book.getSoLuong() <= 0) {
                errors.append("Rất tiếc, sách này vừa hết hàng!<br>"); hasError = true;
            }
        }

        if (ngayTraStr != null && !ngayTraStr.isEmpty()) {
            try {
                ngayTra = LocalDate.parse(ngayTraStr, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                errors.append("Ngày trả không hợp lệ.<br>");
                hasError = true;
            }
        } else {
            errors.append("Ngày trả không được để trống.<br>");
            hasError = true;
        }

        if (hasError) {
            List<Student> studentList = studentRepository.findAll();
            String borrowDateStr = LocalDate.now().format(DISPLAY_FORMATTER);
            request.setAttribute("sach", book);
            request.setAttribute("hocSinhList", studentList);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/book/borrow.jsp");
            dispatcher.forward(request, response);
        } else {
            BorrowCard borrowCard = new BorrowCard(maMuonSach, maSach, maHocSinh, true, ngayMuon, ngayTra);
            Connection conn = null;
            try {
                conn = DatabaseConnection.getConnection();
                conn.setAutoCommit(false);

                borrowCardRepository.save(borrowCard);
                bookRepository.updateQuantity(maSach, -1);

                conn.commit();
                response.sendRedirect(request.getContextPath() + "/books?success=Borrowed");

            } catch (SQLException e) {
                if (conn != null) try { conn.rollback(); } catch (SQLException ex) { printSQLException(ex); }
                request.setAttribute("errorMessage", "Lỗi hệ thống khi mượn sách: " + e.getMessage());
                List<Student> studentList = studentRepository.findAll();
                request.setAttribute("sach", bookRepository.findById(maSach));
                request.setAttribute("hocSinhList", studentList);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/book/borrow.jsp");
                dispatcher.forward(request, response);
            } finally {
                if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { printSQLException(e); }
            }
        }
    }
    private void printSQLException(SQLException ex) {}
}