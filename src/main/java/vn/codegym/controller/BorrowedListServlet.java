package vn.codegym.controller;

import vn.codegym.model.BorrowDetailDTO;
import vn.codegym.repository.BorrowCardRepository;
import vn.codegym.repository.impl.BorrowCardRepositoryImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "BorrowedListServlet", urlPatterns = "/borrowed-books")
public class BorrowedListServlet extends HttpServlet {
    private BorrowCardRepository borrowCardRepository = new BorrowCardRepositoryImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String searchBookName = request.getParameter("searchBookName");
        String searchStudentName = request.getParameter("searchStudentName");

        List<BorrowDetailDTO> borrowedList = borrowCardRepository.searchActiveBorrowedDetails(searchBookName, searchStudentName);

        request.setAttribute("borrowedList", borrowedList);
        request.setAttribute("searchBookName", searchBookName);
        request.setAttribute("searchStudentName", searchStudentName);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/borrow/list.jsp");
        dispatcher.forward(request, response);
    }
}