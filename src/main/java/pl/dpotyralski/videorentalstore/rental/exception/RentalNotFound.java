package pl.dpotyralski.videorentalstore.rental.exception;

import pl.dpotyralski.videorentalstore.infrastructure.exceptions.ApplicationException;
import pl.dpotyralski.videorentalstore.infrastructure.exceptions.ErrorCodes;

import java.text.MessageFormat;

public class RentalNotFound extends ApplicationException {

    public RentalNotFound(Long id) {
        super(ErrorCodes.RENTAL_NOT_FOUND_CODE, MessageFormat.format("Rental with id: {0} could not be found", id));
    }
}
