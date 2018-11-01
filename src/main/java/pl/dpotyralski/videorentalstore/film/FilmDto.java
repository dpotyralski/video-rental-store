package pl.dpotyralski.videorentalstore.film;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmDto {

    private Long id;
    private String title;
    private FilmType filmType;

}
