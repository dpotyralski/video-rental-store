package pl.dpotyralski.videorentalstore.rental.protocol.response;

import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
public class RentalSummaryResponse {

    private List<RentalResponse> rentals;

    @Value
    public static class RentalResponse {
        private Long rentalId;
        private BigDecimal rentalPrice;
    }
}
