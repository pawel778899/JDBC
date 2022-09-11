package jdbc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
    static MovieRepository movieRepository = new MovieRepository();
    static TeacherRepository teacherRepository = new TeacherRepository();

    public static void main(String[] args) {
        Movie movie = new Movie();
        movie.setGenre("Horror");
        movie.setTitle("Jigsaw");
        movie.setYearOfRelease(2001);

        movieRepository.add(movie);

        Movie movieUpdate = new Movie();
        movieUpdate.setGenre("Horror");
        movieUpdate.setTitle("Jigsaw V");
        movieUpdate.setYearOfRelease(2022);
        movieUpdate.setId(1);

        movieRepository.update(movieUpdate);

        movieRepository.getAll2().forEach(m -> System.out.println(m.getTitle()));

        movieRepository.getAll2().stream().map(m -> {
            m.setGenre("Fantasy");
            movieRepository.update(m);
            return m;
        }).collect(Collectors.toList());

        Optional<Movie> movie1 = movieRepository.findById(99);
        if (movie1.isPresent()) {
            System.out.println("Movie is present");
        } else {
            System.out.println("Movie is absent");
        }

        Teacher teacher = new Teacher();
        teacher.setFirstName("Albert");
        teacher.setLastName("stec");
        teacher.setId(99); // dodajemy id, ponieważ kolumna id w bazie danych teachers nie jest AUTO_INCREMENT

        teacherRepository.add(teacher);

        teacher.setFirstName("Alfred");
        teacherRepository.update(teacher);

        List<Teacher> teachers = teacherRepository.getPaginated(3, 3);
        teachers.forEach(t -> System.out.println(t.getFirstName()));
    }
}
