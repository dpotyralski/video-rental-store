package pl.dpotyralski.videorentalstore.rental;

import org.springframework.transaction.annotation.Transactional;
import pl.dpotyralski.videorentalstore.infrastructure.TimeProvider;
import pl.dpotyralski.videorentalstore.rental.exception.RentalNotFound;

class RentalReturner {

    private final RentalRepository rentalRepository;
    private final RentalPriceCalculator rentalPriceCalculator;
    private final TimeProvider timeProvider;

    RentalReturner(RentalRepository rentalRepository, RentalPriceCalculator rentalPriceCalculator, TimeProvider timeProvider) {
        this.rentalRepository = rentalRepository;
        this.rentalPriceCalculator = rentalPriceCalculator;
        this.timeProvider = timeProvider;
    }

    @Transactional
    Rental returnFilm(ReturnCommand returnCommand) {
        Rental currentRental = rentalRepository.findById(returnCommand.getRentalId())
                .orElseThrow(() -> new RentalNotFound(returnCommand.getRentalId()));

        currentRental.returnFilm(rentalPriceCalculator, timeProvider.now());
        return currentRental;
    }

}
