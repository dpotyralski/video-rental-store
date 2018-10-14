package pl.dpotyralski.videorentalstore.rental;

import lombok.Builder;
import lombok.Getter;
import pl.dpotyralski.videorentalstore.film.FilmType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class RentalDto {

    private Long id;

    private Long customerId;

    private Long filmId;

    private LocalDate rentDate;

    private LocalDate dueBy;

    private LocalDate returnedOn;

    private BigDecimal priceCalculated;

    private BigDecimal lateReturnCharge;

    @Enumerated(EnumType.STRING)
    private FilmType filmType;

}
