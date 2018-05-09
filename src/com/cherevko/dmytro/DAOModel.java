package com.cherevko.dmytro;

import java.util.List;

public interface DAOModel {
    void connect();
    void disconnect();
    List<Student> getAllStudents();
    void insertStudent(String name, String group);
    void removeStudentById(int id);
    void updateStudentById(int id, String name, String group);

}
