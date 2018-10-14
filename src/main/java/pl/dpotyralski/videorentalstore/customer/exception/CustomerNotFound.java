package pl.dpotyralski.videorentalstore.customer.exception;

import pl.dpotyralski.videorentalstore.infrastructure.exceptions.ApplicationException;
import pl.dpotyralski.videorentalstore.infrastructure.exceptions.ErrorCodes;

public class CustomerNotFound extends ApplicationException {

    public CustomerNotFound() {
        super(ErrorCodes.CUSTOMER_NOT_FOUND_CODE, "Customer could not be found!");
    }

}
