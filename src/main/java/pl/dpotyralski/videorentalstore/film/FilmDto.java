package pl.dpotyralski.videorentalstore.film;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FilmDto {

    private Long id;
    private String title;
    private FilmType filmType;

}
