package jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherMapper {

    public Teacher map(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");

        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);

        return teacher;
    }

}
