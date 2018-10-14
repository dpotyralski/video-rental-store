package pl.dpotyralski.videorentalstore.rental.protocol.response;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class ReturnFilmResponse {

    private BigDecimal lateReturnCharge;

}
