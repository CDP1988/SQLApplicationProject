package com.cherevko.dmytro;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DAOOracleImpl.getInstance().connect();
        List<Student> list = DAOOracleImpl.getInstance().getAllStudents();
        for (Student student : list) {
            System.out.println(student);
        }
        DAOOracleImpl.getInstance().removeStudentById(1);
        DAOOracleImpl.getInstance().updateStudentById(1, "john", "w-00");
        DAOOracleImpl.getInstance().disconnect();
    }
}
