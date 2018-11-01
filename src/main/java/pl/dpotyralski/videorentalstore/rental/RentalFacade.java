package pl.dpotyralski.videorentalstore.rental;

import org.springframework.transaction.annotation.Transactional;
import pl.dpotyralski.videorentalstore.customer.AddBonusPointsCommand;
import pl.dpotyralski.videorentalstore.customer.CustomerFacade;
import pl.dpotyralski.videorentalstore.film.FilmType;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
public class RentalFacade {

    private final RentalSearcher rentalSearch;
    private final RentalCreator rentalCreator;
    private final RentalReturner rentalReturner;
    private final CustomerFacade customerFacade;

    public RentalFacade(RentalSearcher rentalSearch, RentalCreator rentalCreator, RentalReturner rentalReturner,
            CustomerFacade customerFacade) {
        this.rentalSearch = rentalSearch;
        this.rentalCreator = rentalCreator;
        this.rentalReturner = rentalReturner;
        this.customerFacade = customerFacade;
    }

    @Transactional(readOnly = true)
    public List<RentalDto> findRentalsByCustomerId(Long customerId, boolean onlyActive) {
        return rentalSearch.findRentalsByCustomerIdAndRentalStatus(customerId, onlyActive).stream()
                .map(Rental::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateRentalPrice(RentalPriceCalculateCommand rentalPriceCalculateCommand) {
        return rentalCreator.calculateRentalPrice(rentalPriceCalculateCommand);
    }

    public BigDecimal rent(RentalCommand rentalCommand) {
        List<RentalDto> rentals = rentalCreator.rent(rentalCommand);
        customerFacade.addBonusPointsToCustomer(new AddBonusPointsCommand(getBonusPoints(rentals), rentalCommand.getCustomerId()));
        return rentals.stream()
                .map(RentalDto::getPriceCalculated)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public RentalDto returnFilm(ReturnCommand returnCommand) {
        return rentalReturner.returnFilm(returnCommand).toDto();
    }

    private int getBonusPoints(List<RentalDto> rentals) {
        return rentals.stream()
                .map(RentalDto::getFilmType)
                .mapToInt(FilmType::getBonusPoints).sum();
    }

}
