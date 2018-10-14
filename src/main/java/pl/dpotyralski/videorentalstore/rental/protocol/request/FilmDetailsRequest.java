package pl.dpotyralski.videorentalstore.rental.protocol.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class FilmDetailsRequest {

    @NotNull
    private Long filmId;

    @Min(1)
    private int days;
}
