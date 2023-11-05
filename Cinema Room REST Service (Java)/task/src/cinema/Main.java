package cinema;

import cinema.models.MovieTheatre;
import cinema.models.Statistics;
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

    @Bean
    public Statistics getStatistics() {
        return new Statistics(0, 81, 0);
    }
}
