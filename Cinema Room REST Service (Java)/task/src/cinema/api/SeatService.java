package cinema.api;

import cinema.models.MovieTheatre;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeatService {
    @GetMapping("/seats")
    public MovieTheatre getMovieTheatre(){
        return new MovieTheatre(9,9);
    }
}
