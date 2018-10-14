package pl.dpotyralski.videorentalstore.rental;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pl.dpotyralski.videorentalstore.film.FilmType;
import pl.dpotyralski.videorentalstore.infrastructure.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Table(name = "rentals")
class Rental extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long customerId;

    private Long filmId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate rentDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dueBy;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate returnedOn;

    private BigDecimal priceCalculated;

    private boolean returned;

    @Builder.Default
    private BigDecimal lateReturnCharge = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private FilmType filmType;

    RentalDto toDto() {
        return RentalDto.builder()
                .id(id)
                .customerId(customerId)
                .filmId(filmId)
                .filmType(filmType)
                .rentDate(rentDate)
                .priceCalculated(priceCalculated)
                .returnedOn(returnedOn)
                .lateReturnCharge(lateReturnCharge)
                .dueBy(dueBy).build();
    }

    void returnFilm(RentalPriceCalculator rentalPriceCalculator, LocalDate now) {
        long between = DAYS.between(this.getDueBy(), now);
        lateReturnCharge = rentalPriceCalculator.calculateReturnChargePrice(between, this.getFilmType());
        markAsReturned(now);
    }

    private void markAsReturned(LocalDate now) {
        this.returnedOn = now;
        this.returned = true;
    }

}
