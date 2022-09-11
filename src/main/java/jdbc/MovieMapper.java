package jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieMapper {

    public Movie map(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String title = resultSet.getString("title");
        String genre = resultSet.getString("genre");
        int yearOfRelease= resultSet.getInt("yearOfRelease");

        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle(title);
        movie.setGenre(genre);
        movie.setYearOfRelease(yearOfRelease);

        return movie;
    }
}
