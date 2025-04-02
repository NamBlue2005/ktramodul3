package vn.codegym.repository;

import vn.codegym.model.Student; // Updated import
import java.util.List;

public interface StudentRepository {
    List<Student> findAll();
    Student findById(int id);
}