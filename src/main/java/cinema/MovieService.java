package cinema;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private AtomicLong id = new AtomicLong();

    private ModelMapper modelMapper;

    private List<Movie> movies = Collections.synchronizedList(new ArrayList<>());

    public MovieService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<MovieDTO> getMovies(Optional<String> title) {
        List<Movie> result = movies.stream()
                .filter(m -> title.isEmpty() || m.getTitle().equalsIgnoreCase(title.get()))
                .collect(Collectors.toList());
        Type targetListType = new TypeToken<List<MovieDTO>>() {
        }.getType();
        return modelMapper.map(result, targetListType);
    }

    public MovieDTO getMovieById(long id) {
        Movie movie = findById(id);
        return modelMapper.map(movie, MovieDTO.class);
    }

    private Movie findById(long id) {
        return movies.stream()
                .filter(m -> m.getId() == id)
                .findFirst().orElseThrow(() -> new NoSuchElementException("Can not find movie with id: " + id));
    }

    public MovieDTO createMovie(CreateMovieCommand command) {
        Movie movie = new Movie(id.incrementAndGet(), command);
        movies.add(movie);
        return modelMapper.map(movie, MovieDTO.class);
    }


    public MovieDTO createReservation(long id, CreateReservationCommand command) {
        Movie movie = findById(id);
        System.out.println(movie);
        movie.addReservations(command.getReservations());
        System.out.println("2." + movie);
        return modelMapper.map(movie, MovieDTO.class);
    }

    public void deleteAllMovie() {
        movies.clear();
        id = new AtomicLong();
    }

    public MovieDTO updateMovieDate(long id, UpdateDateCommand command) {
        Movie movie = findById(id);
        movie.setDate(command.getDate());
        return modelMapper.map(movie, MovieDTO.class);
    }
}
