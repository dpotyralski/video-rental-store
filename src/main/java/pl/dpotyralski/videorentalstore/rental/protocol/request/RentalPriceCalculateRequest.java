package pl.dpotyralski.videorentalstore.rental.protocol.request;

import lombok.Data;

import java.util.Set;

@Data
public class RentalPriceCalculateRequest {

    private Set<FilmDetailsRequest> details;

}
