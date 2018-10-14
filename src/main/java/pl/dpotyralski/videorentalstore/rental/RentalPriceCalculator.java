package pl.dpotyralski.videorentalstore.rental;

import pl.dpotyralski.videorentalstore.film.FilmType;
import pl.dpotyralski.videorentalstore.film.PriceType;

import java.math.BigDecimal;

import static pl.dpotyralski.videorentalstore.film.PriceType.PREMIUM;


class RentalPriceCalculator {

    private final RentalProperties rentalProperties;

    RentalPriceCalculator(RentalProperties rentalProperties) {
        this.rentalProperties = rentalProperties;
    }

    BigDecimal calculateRentalPrice(long days, FilmType filmType) {
        FilmPriceCalculationType filmPriceCalculationType = FilmPriceCalculationType.resolveFilmPriceCalculationTypeByFilmType(filmType);
        return filmPriceCalculationType.getPriceCalculationStrategy()
                .calculate(resolvePriceValue(filmPriceCalculationType.getFilmType().getPriceType()), days);
    }

    BigDecimal calculateReturnChargePrice(long days, FilmType filmType) {
        FilmPriceCalculationType filmPriceCalculationType = FilmPriceCalculationType.resolveFilmPriceCalculationTypeByFilmType(filmType);
        return filmPriceCalculationType.getReturnPriceCalculateStrategy()
                .calculate(resolvePriceValue(filmPriceCalculationType.getFilmType().getPriceType()), days);
    }

    private BigDecimal resolvePriceValue(PriceType priceType) {
        return PREMIUM.equals(priceType) ? rentalProperties.getPremiumPrice() : rentalProperties.getBasicPrice();
    }

}
