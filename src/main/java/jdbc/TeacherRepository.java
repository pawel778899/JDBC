package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeacherRepository {

    // wypisuje dane wszystkich nauczycieli
    public void getAll() {
        String sql = "SELECT * from teachers";

        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/university", "root", "778899");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ) {
            System.err.println("Connection Successful! Enjoy. Now it's time to push data");

            while (resultSet.next()) {
                long id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                System.out.println("Index number: " + id);
                System.out.println("firstName: " + firstName);
                System.out.println("lastName: " + lastName);
                System.out.println('\n');
            }

        } catch (SQLException e) {
            System.err.println("MySQL Connection Failed!");
            e.printStackTrace();
        }
    }

    // wypisuje nauczyciela znalezionego po id
    public void getById(int givenId){
        String sql = "SELECT * from teachers where id=" + givenId;

        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/university", "root", "778899");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ) {
            System.err.println("Connection Successful! Enjoy. Now it's time to push data");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                System.out.println("Index number: " + id);
                System.out.println("firstName: " + firstName);
                System.out.println("lastName: " + lastName);
                System.out.println('\n');
            }

        } catch (SQLException e) {
            System.err.println("MySQL Connection Failed!");
            e.printStackTrace();
        }
    }

    // wypisuje liczbe rekordow nauczycieli
    public void count() {
        String sql = "SELECT count(*) as count from teachers";

        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/university", "root", "778899");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ) {
            System.err.println("Connection Successful! Enjoy. Now it's time to push data");

            while (resultSet.next()) {
                int count = resultSet.getInt("count");
                System.out.println("count: " + count);
            }

        } catch (SQLException e) {
            System.err.println("MySQL Connection Failed!");
            e.printStackTrace();
        }
    }

    // updatuje pole term dla studenta o podanym id
    //public void updateTerm(int term, int id);

    ////// Prepared statements /////////////////////
    public void add(Teacher teacher) {
        String SQL_INSERT = "INSERT IGNORE INTO teachers (first_name, last_name, id) VALUES (?,?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/university", "root", "778899");
             PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT)) {

            preparedStatement.setString(1, teacher.getFirstName());
            preparedStatement.setString(2, teacher.getLastName());
            preparedStatement.setInt(3, teacher.getId());

            int row = preparedStatement.executeUpdate();

            // rows affected
            System.out.println(row);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Teacher teacher) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/university", "root", "778899");
             PreparedStatement updateStatement = connection.prepareStatement("UPDATE teachers SET first_name = ?, last_name = ? WHERE id = ?")) {
            updateStatement.setString(1, teacher.getFirstName());
            updateStatement.setString(2, teacher.getLastName());
            updateStatement.setInt(3, teacher.getId());

            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Teacher> getPaginated(int limit, int offset) {
        String sql = "SELECT * from teachers LIMIT ? OFFSET ?";
        List<Teacher> teachers = new ArrayList<>();

        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/university", "root", "778899");
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    TeacherMapper teacherMapper = new TeacherMapper();
                    Teacher teacher = teacherMapper.map(resultSet);
                    teachers.add(teacher);
                }
            }

        } catch (SQLException e) {
            System.err.println("MySQL Connection Failed!");
            e.printStackTrace();
        }

        return teachers;
    }
}
