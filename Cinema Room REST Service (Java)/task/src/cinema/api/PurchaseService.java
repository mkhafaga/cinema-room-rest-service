package cinema.api;

import cinema.errors.ErrorMessage;
import cinema.models.MovieTheatre;
import cinema.models.Seat;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PurchaseService {

    private final MovieTheatre movieTheatre;

    public PurchaseService(MovieTheatre movieTheatre) {
        this.movieTheatre = movieTheatre;
    }

    @PostMapping("/purchase")
    public ResponseEntity purchase(@RequestBody Seat seat) {
        List<Seat> seats = movieTheatre.getSeats();
        int row = seat.getRow();
        int column = seat.getColumn();
        if (row < 1 || row > 9 || column < 1 || column > 9) {
            return ResponseEntity.badRequest()
                    .body(new ErrorMessage("The number of a row or a column is out of bounds!"));
        }

        Seat seatToBook = seats.get(((row-1) * 9) + column-1);
        if (!seatToBook.isBooked()) {
            seatToBook.setBooked(true);
            return ResponseEntity.ok(seatToBook);
        } else {
            return ResponseEntity.badRequest().body(new ErrorMessage("The ticket has been already purchased!"));
        }
    }
}
