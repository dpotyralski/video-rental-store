package pl.dpotyralski.videorentalstore.rental;

import lombok.Value;

@Value
class RentalDetail {
    private Long filmId;
    private long days;
}