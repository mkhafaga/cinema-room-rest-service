package cinema.api;

import cinema.errors.TicketException;
import cinema.errors.UnauthorizedException;
import cinema.models.MovieTheatre;
import cinema.models.Seat;
import cinema.models.Statistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
public class PurchaseService {

    private final MovieTheatre movieTheatre;
    private final Statistics statistics;

    private final String ADMIN_PASS = "super_secret";

    private final Map<String, Seat> takenSeats = new ConcurrentHashMap<>();

    public PurchaseService(MovieTheatre movieTheatre, Statistics statistics) {
        this.movieTheatre = movieTheatre;
        this.statistics = statistics;
    }

    @PostMapping("/purchase")
    public ResponseEntity<Map<String, Object>> purchase(@RequestBody Seat seat) {
        List<Seat> seats = movieTheatre.getSeats();
        Map<String, Object> response = new LinkedHashMap<>();
        int row = seat.getRow();
        int column = seat.getColumn();
        if (row < 1 || row > 9 || column < 1 || column > 9) {
            throw new TicketException("The number of a row or a column is out of bounds!");
        }

        Seat seatToBook = seats.get(((row - 1) * 9) + column - 1);
        if (!seatToBook.isBooked()) {
            seatToBook.setBooked(true);
            statistics.setIncome(statistics.getIncome() + seatToBook.getPrice());
            statistics.setPurchased(statistics.getPurchased() + 1);
            statistics.setAvailable(statistics.getAvailable() - 1);
            UUID uuid = UUID.randomUUID();
            takenSeats.put(uuid.toString(), seatToBook);
            response.put("token", uuid.toString());
            response.put("ticket", seatToBook);
            return ResponseEntity.ok(response);
        } else {
            throw new TicketException("The ticket has been already purchased!");
        }
    }

    @PostMapping("/return")
    public ResponseEntity<Map<String, Object>> returnTicket(@RequestBody Map<String, String> params) {
        String uuid = params.get("token");
        if (takenSeats.containsKey(uuid)) {
            Seat returnedSeat = takenSeats.remove(uuid);
            returnedSeat.setBooked(false);
            statistics.setIncome(statistics.getIncome() - returnedSeat.getPrice());
            statistics.setPurchased(statistics.getPurchased() - 1);
            statistics.setAvailable(statistics.getAvailable() + 1);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("ticket", returnedSeat);
            return ResponseEntity.ok(response);
        } else {
            throw new TicketException("Wrong token!");
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Statistics> getStatistics(@RequestParam(required = false) String password) {
        if (password != null && password.equals(ADMIN_PASS)) {
            return ResponseEntity.ok(statistics);
        } else {
            throw new UnauthorizedException("The password is wrong!");
        }
    }

    @ExceptionHandler(TicketException.class)
    public ResponseEntity<Map<String, String>> handleTicketException(TicketException e, WebRequest request) {
        Map<String, String> message = new LinkedHashMap<>();
        message.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedException(UnauthorizedException e,
                                                                           WebRequest request) {
        Map<String, String> message = new LinkedHashMap<>();
        message.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }
}
