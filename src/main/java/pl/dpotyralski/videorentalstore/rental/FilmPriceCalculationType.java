package pl.dpotyralski.videorentalstore.rental;

import pl.dpotyralski.videorentalstore.film.FilmType;

import java.math.BigDecimal;
import java.util.stream.Stream;

enum FilmPriceCalculationType {

    PREMIUM_NEW_RELEASE(FilmType.NEW_RELEASE, PriceCalculationStrategy.PREMIUM, PriceCalculationStrategy.RETURN),
    BASIC_REGULAR(FilmType.REGULAR, new BasicPriceCalculateStrategy(3), PriceCalculationStrategy.RETURN),
    BASIC_OLD(FilmType.OLD, new BasicPriceCalculateStrategy(5), PriceCalculationStrategy.RETURN);

    private FilmType filmType;
    private PriceCalculationStrategy priceCalculateStrategy;
    private PriceCalculationStrategy returnPriceCalculateStrategy;

    FilmPriceCalculationType(FilmType filmType, PriceCalculationStrategy priceCalculateStrategy,
            PriceCalculationStrategy returnPriceCalculateStrategy) {
        this.filmType = filmType;
        this.priceCalculateStrategy = priceCalculateStrategy;
        this.returnPriceCalculateStrategy = returnPriceCalculateStrategy;
    }

    public static FilmPriceCalculationType resolveFilmPriceCalculationTypeByFilmType(FilmType filmType) {
        return Stream.of(FilmPriceCalculationType.values())
                .filter(priceType -> priceType.getFilmType().equals(filmType))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public FilmType getFilmType() {
        return filmType;
    }

    public PriceCalculationStrategy getPriceCalculationStrategy() {
        return priceCalculateStrategy;
    }

    public PriceCalculationStrategy getReturnPriceCalculateStrategy() {
        return returnPriceCalculateStrategy;
    }

    interface PriceCalculationStrategy {
        PriceCalculationStrategy PREMIUM = ((priceValue, days) -> priceValue.multiply(new BigDecimal(days)));
        PriceCalculationStrategy RETURN = (PriceCalculationStrategy::getReturnStrategy);

        static BigDecimal getReturnStrategy(BigDecimal priceValue, long days) {
            return days <= 0 ? BigDecimal.ZERO : priceValue.multiply(new BigDecimal(days));
        }

        BigDecimal calculate(BigDecimal priceValue, long days);
    }

    static class BasicPriceCalculateStrategy implements PriceCalculationStrategy {

        private int discountDays;

        BasicPriceCalculateStrategy(int discountDays) {
            this.discountDays = discountDays;
        }

        @Override
        public BigDecimal calculate(BigDecimal priceValue, long days) {
            if (days <= discountDays) {
                return priceValue;
            }
            long daysLeft = days - discountDays;
            return priceValue.add(priceValue.multiply(new BigDecimal(daysLeft)));
        }
    }

}
