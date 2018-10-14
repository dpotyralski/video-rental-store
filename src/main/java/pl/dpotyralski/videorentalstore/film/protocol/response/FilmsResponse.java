package pl.dpotyralski.videorentalstore.film.protocol.response;

import lombok.Value;

import java.util.List;

@Value
public class FilmsResponse {

    private List<FilmResponse> films;

    @Value
    public static class FilmResponse {
        private Long id;
        private String title;
    }

}
