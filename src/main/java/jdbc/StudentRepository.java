package jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    public List<Student> getALl() {
        String sql = "SELECT * FROM students";
        List<Student> studentList = new ArrayList<>();

        try (
                Connection connection = DriverManager
                        .getConnection("jdbc:mysql://localhost:3306/university", "root", "778899");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ) {


            while (resultSet.next()) {
                StudentMapper studentMapper = new StudentMapper();
                Student student = studentMapper.map(resultSet);
                studentList.add(student);
            }

        } catch (SQLException e) {
            System.err.println("Exception");
            e.printStackTrace();

        }
        return studentList;
    }

    public void getByYear(int yearParam) {
        String sql = "SELECT * FROM students WHERE enrollment_year = " + yearParam;

        try (
                Connection connection = DriverManager
                        .getConnection("jdbc:mysql://localhost:3306/university", "root", "778899");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ) {


            while (resultSet.next()) {
                int indexNumber = resultSet.getInt("index_number");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                int term = resultSet.getInt("term");
                int year = resultSet.getInt("enrollment_year");

                System.out.println("Index number: " + indexNumber);
                System.out.println("First name: " + firstName);
                System.out.println("Last name: " + lastName);
                System.out.println("Term: " + term);
                System.out.println("Year: " + year);
                System.out.println('\n');
            }

        } catch (SQLException e) {
            System.err.println("Excetion");
            e.printStackTrace();

        }

    }

    public void add(Student student) {
        String sql = "INSERT INTO students (index_number, first_name, last_name, term, enrollment_year) VALUES(?,?,?,?,?)";

        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/university", "root", "778899");
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setInt(1,student.getIndexNumber());
            preparedStatement.setString(2,student.getFirstName());
            preparedStatement.setString(3,student.getLastName());
            preparedStatement.setInt(4,student.getTerm());
            preparedStatement.setInt(5, student.getEnrollmentYear());

            int rows = preparedStatement.executeUpdate();
            System.out.println("Affected: " + rows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateName(String name, int indexNumber) {
        String sql = "UPDATE students SET first_name = ? WHERE index_number = ?";

        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/university", "root", "778899");
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,indexNumber);

            int rows = preparedStatement.executeUpdate();
            System.out.println("Affected: " + rows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Student findByIndexNumber(int indexNumber) {
        String sql = "SELECT * FROM students WHERE index_number = ?";
        Student student= null;
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/university", "root", "778899");
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setInt(1,indexNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery();){
                    StudentMapper studentMapper = new StudentMapper();
                    resultSet.next();
                    student = studentMapper.map(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }


}

