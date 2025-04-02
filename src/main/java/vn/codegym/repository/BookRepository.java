package vn.codegym.repository;

import vn.codegym.model.Book;
import java.sql.SQLException;
import java.util.List;


public interface BookRepository {
    List<Book> findAll();
    Book findById(int id);
    void updateQuantity(int id, int change) throws SQLException;
}