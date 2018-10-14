package pl.dpotyralski.videorentalstore.rental;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.dpotyralski.videorentalstore.customer.CustomerFacade;
import pl.dpotyralski.videorentalstore.film.FilmFacade;
import pl.dpotyralski.videorentalstore.infrastructure.TimeProvider;

@Configuration
class RentalConfiguration {

    RentalFacade rentalFacade(FilmFacade filmFacade, CustomerFacade customerFacade,
            RentalProperties rentalProperties, TimeProvider timeProvider) {
        InMemoryRentalRepository inMemoryRentalRepository = new InMemoryRentalRepository();
        return rentalFacade(filmFacade, customerFacade, rentalProperties, inMemoryRentalRepository, timeProvider);
    }

    @Bean
    RentalFacade rentalFacade(FilmFacade filmFacade, CustomerFacade customerFacade,
            RentalProperties rentalProperties, RentalRepository rentalRepository, TimeProvider timeProvider) {

        RentalPriceCalculator rentalPriceCalculator = new RentalPriceCalculator(rentalProperties);
        RentalCreator rentalCreator = new RentalCreator(filmFacade, rentalPriceCalculator, rentalRepository, timeProvider);
        RentalSearcher rentalSearch = new RentalSearcher(rentalRepository);
        RentalReturner rentalReturner = new RentalReturner(rentalRepository, rentalPriceCalculator, timeProvider);
        return new RentalFacade(rentalSearch, rentalCreator, rentalReturner, customerFacade);
    }

}
