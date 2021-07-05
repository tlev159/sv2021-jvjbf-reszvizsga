package cinema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    private long id;
    private String title;
    private LocalDateTime date;
    private int maxSpaces;
    private int freeSpaces;

    public Movie(long id, CreateMovieCommand command) {
        this.id = id;
        this.title = command.getTitle();
        this.date = command.getDate();
        this.maxSpaces = command.getMaxSpaces();
        this.freeSpaces = command.getMaxSpaces();
    }

    public int addReservations(int reservations) {
        int temp = freeSpaces - reservations;
        if (temp < 0) {
            throw new IllegalStateException("There is no more free spaces!");
        } else {
            freeSpaces = temp;
            return temp;
        }
    }
}
