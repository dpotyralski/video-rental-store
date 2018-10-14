package pl.dpotyralski.videorentalstore.customer.exception;

import pl.dpotyralski.videorentalstore.infrastructure.exceptions.ApplicationException;
import pl.dpotyralski.videorentalstore.infrastructure.exceptions.ErrorCodes;

public class CustomerBonusPointsNotFound extends ApplicationException {

    public CustomerBonusPointsNotFound() {
        super(ErrorCodes.CUSTOMER_BONUS_POINTS_NOT_FOUND_CODE, "Customer bonus points could not be found!");
    }

}
