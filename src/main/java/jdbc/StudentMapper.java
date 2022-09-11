package jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentMapper {

    public Student map(ResultSet resultSet) throws SQLException {
        int indexNumber = resultSet.getInt("index_number");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        int term = resultSet.getInt("term");
        int year = resultSet.getInt("enrollment_year");

        Student student = new Student();
        student.setIndexNumber(indexNumber);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setTerm(term);
        student.setEnrollmentYear(year);

        return student;
    }

}
