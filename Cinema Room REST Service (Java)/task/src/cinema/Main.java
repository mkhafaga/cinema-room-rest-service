package cinema;

import cinema.models.MovieTheatre;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public MovieTheatre getMovieTheatre() {
        return new MovieTheatre(9, 9);
    }
}
