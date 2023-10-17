package cinema.models;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

public class MovieTheatre {
    private int rows;
    private int columns;
    List<Seat> seats;

    public MovieTheatre(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        seats = new ArrayList<>();
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                seats.add(new Seat(i, j));
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
