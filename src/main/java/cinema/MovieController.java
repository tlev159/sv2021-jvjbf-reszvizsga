package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/cinema")
public class MovieController {
    
    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieDTO> getMovies(@RequestParam Optional<String> title) {
        return movieService.getMovies(title);
    }
    
    @GetMapping("/{id}")
    public MovieDTO getMovieById(@PathVariable("id") long id) {
        return movieService.getMovieById(id);
    }
    
    @PostMapping
    public MovieDTO createMovie(@Valid @RequestBody CreateMovieCommand command) {
        return movieService.createMovie(command);
    }

    @PostMapping("/{id}/reserve")
    public MovieDTO createReservation(@PathVariable("id") long id, @RequestBody CreateReservationCommand command) {
        return movieService.createReservation(id, command);
    }

    @PutMapping("/{id}")
    public MovieDTO updateMovieDatum(@PathVariable("id") long id, @RequestBody UpdateDateCommand command) {
        return movieService.updateMovieDate(id, command);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllMovie() {
        movieService.deleteAllMovie();
    }
    
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Problem> handleNotFound(NoSuchElementException nse) {
        Problem problem = Problem.builder()
                .withType(URI.create("cinema/not-found"))
                .withTitle("Not found!")
                .withStatus(Status.NOT_FOUND)
                .withDetail(nse.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Problem> handleBadReservation(IllegalStateException ise) {
        Problem problem = Problem.builder()
                .withType(URI.create("cinema/bad-reservation"))
                .withTitle("Not enough free spaces to reserve!")
                .withStatus(Status.BAD_REQUEST)
                .withDetail(ise.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

}
