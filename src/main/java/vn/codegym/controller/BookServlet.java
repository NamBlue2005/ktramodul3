package vn.codegym.controller;

import vn.codegym.model.Book; // Updated import
import vn.codegym.repository.BookRepository; // Updated import
import vn.codegym.repository.impl.BookRepositoryImpl; // Updated import

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "BookServlet", urlPatterns = "/books")
public class BookServlet extends HttpServlet {
    private BookRepository bookRepository = new BookRepositoryImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        listBooks(request, response);
    }

    private void listBooks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        List<Book> bookList = bookRepository.findAll(); // Updated variable type
        request.setAttribute("bookList", bookList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/book/list.jsp");
        dispatcher.forward(request, response);
    }
}