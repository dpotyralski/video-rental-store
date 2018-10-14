package pl.dpotyralski.videorentalstore.rental;


import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.dpotyralski.videorentalstore.film.FilmDto;
import pl.dpotyralski.videorentalstore.film.FilmFacade;
import pl.dpotyralski.videorentalstore.infrastructure.TimeProvider;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Transactional
class RentalCreator {

    private final FilmFacade filmFacade;
    private final RentalPriceCalculator rentalPriceCalculator;
    private final RentalRepository rentalRepository;
    private final TimeProvider timeProvider;

    RentalCreator(FilmFacade filmFacade, RentalPriceCalculator rentalPriceCalculator,
            RentalRepository rentalRepository, TimeProvider timeProvider) {
        this.filmFacade = filmFacade;
        this.rentalPriceCalculator = rentalPriceCalculator;
        this.rentalRepository = rentalRepository;
        this.timeProvider = timeProvider;
    }

    @Transactional(readOnly = true)
    BigDecimal calculateRentalPrice(RentalPriceCalculateCommand rentalPriceCalculateCommand) {
        List<FilmDto> films = filmFacade.findAllByIds(getFilmIds(rentalPriceCalculateCommand.getDetails()));
        Map<Long, RentalDetail> filmsWithRentalDetails = transformToFilmWithRentalDetailsMap(rentalPriceCalculateCommand.getDetails());

        return films.stream().map(film -> {
            long days = filmsWithRentalDetails.get(film.getId()).getDays();
            return rentalPriceCalculator.calculateRentalPrice(days, film.getFilmType());
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<RentalDto> rent(RentalCommand rentalCommand) {
        Long customerId = rentalCommand.getCustomerId();
        List<FilmDto> films = filmFacade.findAllByIds(getFilmIds(rentalCommand.getFilmDetails()));
        Map<Long, RentalDetail> filmsWithRentalDetails = transformToFilmWithRentalDetailsMap(rentalCommand.getFilmDetails());

        Set<Rental> rentals = films.stream()
                .map(film -> createRental(customerId, film, filmsWithRentalDetails.get(film.getId()).getDays()))
                .collect(toSet());

        return rentalRepository.saveAll(rentals).stream()
                .map(Rental::toDto)
                .collect(toList());
    }

    private Rental createRental(Long customerId, FilmDto filmDto, long days) {
        BigDecimal priceCalculated = rentalPriceCalculator.calculateRentalPrice(days, filmDto.getFilmType());
        LocalDate rentDate = timeProvider.now();
        return Rental.builder()
                .customerId(customerId)
                .filmId(filmDto.getId())
                .priceCalculated(priceCalculated)
                .rentDate(rentDate)
                .dueBy(rentDate.plusDays(days))
                .filmType(filmDto.getFilmType())
                .build();
    }

    private Map<Long, RentalDetail> transformToFilmWithRentalDetailsMap(List<RentalDetail> rentalDetails) {
        return rentalDetails.stream()
                .collect(Collectors.toMap(RentalDetail::getFilmId, rentalDetail -> rentalDetail));
    }

    private Set<Long> getFilmIds(List<RentalDetail> rentalDetails) {
        return rentalDetails.stream()
                .map(RentalDetail::getFilmId)
                .collect(toSet());
    }
}
