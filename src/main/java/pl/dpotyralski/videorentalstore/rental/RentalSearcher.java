package pl.dpotyralski.videorentalstore.rental;

import java.util.List;

class RentalSearcher {

    private final RentalRepository rentalRepository;

    RentalSearcher(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    List<Rental> findRentalsByCustomerIdAndRentalStatus(Long customerId, boolean active) {
        if (active) {
            return rentalRepository.findCurrentRentalsByCustomerId(customerId);
        }
        return rentalRepository.findAllByCustomerId(customerId);
    }


}
