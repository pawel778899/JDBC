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

public class MovieRepository {

    // tworzy baze danych o nazwie hollywood
    public void createDatabase() {
        String sql = "CREATE DATABASE if not exists hollywood;";

        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "778899");
                Statement statement = connection.createStatement();
        ) {
            statement.execute(sql);
            System.out.println("Database created");
        } catch (SQLException e) {
            System.err.println("MySQL Connection Failed!");
            e.printStackTrace();
        }
    }

    // tworzy tabele movies z polami
    // id of type INTEGER AUTO INCREMENT,title of type VARCHAR (255), genre of type VARCHAR (255),yearOfRelease of type INTEGER
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS movies (id INTEGER AUTO_INCREMENT, " +
                "title VARCHAR(255), " +
                "genre VARCHAR(255), " +
                "yearOfRelease INTEGER, " +
                "PRIMARY KEY (id))";

        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hollywood", "root", "778899");
                Statement statement = connection.createStatement();
        ) {
            statement.execute(sql);
            System.out.println("Table created");
        } catch (SQLException e) {
            System.err.println("MySQL Connection Failed!");
            e.printStackTrace();
        }
    }

    // usuwa cala tabele
    public void deleteTable() {
        String sql = "DROP TABLE IF EXISTS movies;";

        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hollywood", "root", "778899");
                Statement statement = connection.createStatement();
        ) {
            statement.execute(sql);
            System.out.println("Table created");
        } catch (SQLException e) {
            System.err.println("MySQL Connection Failed!");
            e.printStackTrace();
        }
    }

    // dodac trzy filmy recznie przez workbench


    public void delete(int id) {
        String sql = "DELETE FROM movies WHERE id=" + id;

        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hollywood", "root", "778899");
                Statement statement = connection.createStatement();
        ) {
            statement.execute(sql);
            System.out.println("Table created");
        } catch (SQLException e) {
            System.err.println("MySQL Connection Failed!");
            e.printStackTrace();
        }
    }

    // wypisuje wszystkie filmy w bazie
    public void getAll() {
        String sql = "SELECT * from movies";

        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hollywood", "root", "778899");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ) {
            System.err.println("Connection Successful! Enjoy. Now it's time to push data");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                int year = resultSet.getInt("yearOfRelease");

                System.out.println("id: " + id);
                System.out.println("title: " + title);
                System.out.println("genre: " + genre);
                System.out.println("year: " + year);
                System.out.println('\n');
            }

        } catch (SQLException e) {
            System.err.println("MySQL Connection Failed!");
            e.printStackTrace();
        }
    }

    // aktualizuje rok wydania filmu o podanym id
    void updateYearOfRelease(int id, int newReleaseYear) {
        String sql = "UPDATE movies SET yearOfRelease = " + newReleaseYear + " WHERE id = " + id;

        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hollywood", "root", "778899");
                Statement statement = connection.createStatement();
        ) {
            statement.execute(sql);
            System.out.println("Updated");
        } catch (SQLException e) {
            System.err.println("MySQL Connection Failed!");
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////

    public void add(Movie movie) {
        String SQL_INSERT = "INSERT INTO movies (title, genre, yearOfRelease) VALUES (?,?,?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hollywood", "root", "778899");
             PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT)) {

            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getGenre());
            preparedStatement.setInt(3, movie.getYearOfRelease());

            int row = preparedStatement.executeUpdate();

            // rows affected
            System.out.println(row); //1

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Movie movie) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hollywood", "root", "778899");
             PreparedStatement updateStatement = connection.prepareStatement("UPDATE movies SET title = ?, genre = ?, yearOfRelease = ? WHERE id = ?")) {
            updateStatement.setString(1, movie.getTitle());
            updateStatement.setString(2, movie.getGenre());
            updateStatement.setInt(3, movie.getYearOfRelease());
            updateStatement.setInt(4, movie.getId());

            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // zmodyfikowac metode getAll do nastepujacej postaci
    public List<Movie> getAll2() {
        String sql = "SELECT * from movies";
        List<Movie> movies = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hollywood", "root", "778899");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ) {
            System.err.println("Connection Successful! Enjoy. Now it's time to push data");

            while (resultSet.next()) {
                MovieMapper movieMapper = new MovieMapper();
                Movie movie = movieMapper.map(resultSet);
                movies.add(movie);
            }

        } catch (SQLException e) {
            System.err.println("MySQL Connection Failed!");
            e.printStackTrace();
        }

        return movies;
    }

    // w metodzie main uzywajac strumieni, pobierz wszystkie filmy metoda List<Movie> getAll2(); i zmien wszystkich genre na "Horror" uzywajac metody


    // zwraca film opakowany w Optional o podanym id. W mainie uzywajac if sprawdz czy optional jest pusty czy nie
    Optional<Movie> findById(int id) {
        String sql = "SELECT * from movies where id = ?";

        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hollywood", "root", "778899");
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                MovieMapper movieMapper = new MovieMapper();

                if (resultSet.next()) {
                    Movie movie = movieMapper.map(resultSet);
                    return Optional.ofNullable(movie);
                } else {
                    return Optional.empty();

                }
            }

        } catch (SQLException e) {
            System.err.println("MySQL Connection Failed!");
            e.printStackTrace();
        }

        return Optional.empty();
    }
}


