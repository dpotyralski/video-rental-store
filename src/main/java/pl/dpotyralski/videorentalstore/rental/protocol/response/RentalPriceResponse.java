package pl.dpotyralski.videorentalstore.rental.protocol.response;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class RentalPriceResponse {

    private BigDecimal rentalPrice;

}
