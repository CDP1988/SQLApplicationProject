package com.cherevko.dmytro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOOracleImpl implements DAOModel {

    private static final DAOOracleImpl INSTANCE = new DAOOracleImpl();
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private DAOOracleImpl(){}

    public static DAOModel getInstance(){
        return INSTANCE;
    }

    @Override
    public void connect() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //Driver driver = new OracleDriver();
            try {
                //Locale.setDefault(Locale.ENGLISH);
                connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "cdp", "020488");
                if(!connection.isClosed()){
                    System.out.println("Connection successfull");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            if (connection != null)
                connection.close();
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Student> getAllStudents() {

        List<Student> students = new ArrayList<>();
        try {

            preparedStatement = connection.prepareStatement("select * from STUDENTS");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                students.add(parseStudent(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    private Student parseStudent(ResultSet resultSet) {
        Student student = null;
        try {
            int id = resultSet.getInt("STUDENT_ID");
            String name = resultSet.getString("STUDENT_NAME");
            String group = resultSet.getString("STUDENT_GROUP");
            student = new Student(id,name,group);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return student;
    }

    @Override
    public void removeStudentById(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE STUDENTS WHERE STUDENT_ID = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void insertStudent(String name, String group) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO  STUDENTS VALUES (STUDENT_SEQ.NEXTVAL ,?,?)",
                    new String[] {"student_id"});
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, group);
            Long studentId = null;

            if (preparedStatement.executeUpdate() > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys != null && generatedKeys.next()) {
                    studentId = generatedKeys.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateStudentById(int id, String name, String group) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE STUDENTS SET STUDENT_NAME = ?, STUDENT_GROUP = ? WHERE STUDENT_ID = ?");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, group);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
