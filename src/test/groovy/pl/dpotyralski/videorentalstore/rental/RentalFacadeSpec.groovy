package pl.dpotyralski.videorentalstore.rental

import pl.dpotyralski.videorentalstore.customer.AddBonusPointsCommand
import pl.dpotyralski.videorentalstore.customer.CustomerFacade
import pl.dpotyralski.videorentalstore.film.FilmDto
import pl.dpotyralski.videorentalstore.film.FilmFacade
import pl.dpotyralski.videorentalstore.film.FilmType
import pl.dpotyralski.videorentalstore.infrastructure.TimeProvider
import spock.lang.Specification

import java.time.LocalDate

import static pl.dpotyralski.videorentalstore.rental.RentalFacadeSpec.Fields.CUSTOMER_ID
import static pl.dpotyralski.videorentalstore.rental.RentalFacadeSpec.Fields.MATRIX_FILM_ID
import static pl.dpotyralski.videorentalstore.rental.RentalFacadeSpec.Fields.OUT_OF_AFRICA_FILM_ID
import static pl.dpotyralski.videorentalstore.rental.RentalFacadeSpec.Fields.SPIDER_MAN_2_FILM_ID
import static pl.dpotyralski.videorentalstore.rental.RentalFacadeSpec.Fields.SPIDER_MAN_FILM_ID

class RentalFacadeSpec extends Specification {

    FilmFacade filmFacade = Stub()
    RentalProperties rentalProperties = Stub()

    TimeProvider timeProvider = Mock()
    CustomerFacade customerFacade = Mock()

    RentalFacade facade

    void setup() {
        facade = new RentalConfiguration().rentalFacade(filmFacade, customerFacade, rentalProperties, timeProvider)
        timeProvider.now() >> Fields.TEST_NOMINAL_DATE
        rentalProperties.getPremiumPrice() >> 40.0
        rentalProperties.getBasicPrice() >> 30.0
    }

    def "Should be able to get all rentals"() {
        given: "4 films are in the system"
        filmFacade.findAllByIds(_ as Set) >> [getMatrixFilmDto(), getSpiderManFilmDto(), getSpiderMan2FilmDto(), getOutOfAfricaFilmDto()]

        when: "we rent all of them"
        facade.rent(new RentalCommand(CUSTOMER_ID,
                [
                        new RentalDetail(MATRIX_FILM_ID, 1),
                        new RentalDetail(SPIDER_MAN_FILM_ID, 5),
                        new RentalDetail(SPIDER_MAN_2_FILM_ID, 2),
                        new RentalDetail(OUT_OF_AFRICA_FILM_ID, 7)
                ]))

        and: "we ask for rentals"
        List<RentalDto> rentals = facade.findRentalsByCustomerId(CUSTOMER_ID, true)

        then: "4 films should be rented"
        rentals.size() == 4
    }

    def "Should rent premium film and add bonus points"() {
        given: "a film is in the system"
        filmFacade.findAllByIds(_ as Set) >> [getMatrixFilmDto()]

        when:
        BigDecimal rentalPrice = facade.rent(new RentalCommand(CUSTOMER_ID, [new RentalDetail(MATRIX_FILM_ID, 5)]))

        then: "should calculate price for premium film"
        rentalPrice == 200.00

        and: "for premium film 2 bonus point should be added"
        1 * customerFacade.addBonusPointsToCustomer({
            it.getBonusPoints() == 2 && it.getCustomerId() == CUSTOMER_ID
        } as AddBonusPointsCommand)
    }

    def "Should rent regular film and add bonus points"() {
        given: "a film is in the system"
        filmFacade.findAllByIds(_ as Set) >> [getSpiderManFilmDto()]

        when:
        BigDecimal rentalPrice = facade.rent(new RentalCommand(CUSTOMER_ID, [new RentalDetail(SPIDER_MAN_FILM_ID, 5)]))

        then: "should calculate price for regular film"
        rentalPrice == 90.0

        and: "for regular film 1 bonus point should be added"
        1 * customerFacade.addBonusPointsToCustomer({
            it.getBonusPoints() == 1 && it.getCustomerId() == CUSTOMER_ID
        } as AddBonusPointsCommand)
    }

    def "Should rent regular film and calculate rental price with discount days"() {
        given: "a film is in the system"
        filmFacade.findAllByIds(_ as Set) >> [getSpiderManFilmDto()]

        when:
        BigDecimal rentalPrice = facade.rent(new RentalCommand(CUSTOMER_ID, [new RentalDetail(SPIDER_MAN_FILM_ID, 3)]))

        then: "should calculate price for regular film"
        rentalPrice == 30.0

        and: "for regular film 1 bonus point should be added"
        1 * customerFacade.addBonusPointsToCustomer({
            it.getBonusPoints() == 1 && it.getCustomerId() == CUSTOMER_ID
        } as AddBonusPointsCommand)
    }

    def "Should rent old film and calculate rental price only within discount days"() {
        given: "a film is in the system"
        filmFacade.findAllByIds(_ as Set) >> [getOutOfAfricaFilmDto()]

        when:
        BigDecimal rentalPrice = facade.rent(new RentalCommand(CUSTOMER_ID, [new RentalDetail(OUT_OF_AFRICA_FILM_ID, 5)]))

        then: "should calculate price for regular film"
        rentalPrice == 30.0

        and: "for old film 1 bonus point should be added"
        1 * customerFacade.addBonusPointsToCustomer({
            it.getBonusPoints() == 1 && it.getCustomerId() == CUSTOMER_ID
        } as AddBonusPointsCommand)
    }

    def "Should rent old film and calculate rental price"() {
        given: "a film is in the system"
        filmFacade.findAllByIds(_ as Set) >> [getOutOfAfricaFilmDto()]

        when:
        BigDecimal rentalPrice = facade.rent(new RentalCommand(CUSTOMER_ID, [new RentalDetail(OUT_OF_AFRICA_FILM_ID, 7)]))

        then: "should calculate price for regular film"
        rentalPrice == 90.0

        and:
        1 * customerFacade.addBonusPointsToCustomer(_ as AddBonusPointsCommand)
    }

    def "Should calculate rental price"() {
        given: "a films are in the system"
        filmFacade.findAllByIds(_ as Set) >> [getMatrixFilmDto(), getOutOfAfricaFilmDto()]

        when: "we ask for rental price calculation"
        BigDecimal rentalPrice = facade.calculateRentalPrice(new RentalPriceCalculateCommand(
                [new RentalDetail(MATRIX_FILM_ID, 5), new RentalDetail(OUT_OF_AFRICA_FILM_ID, 7)]))

        then: "should calculate price for regular film"
        rentalPrice == 290.0
    }


    def "Should return premium rented film and calculate additional charge"() {
        given: "a film is in the system"
        filmFacade.findAllByIds(_ as Set) >> [getMatrixFilmDto()]
        and: "Matrix film is rented"
        facade.rent(new RentalCommand(CUSTOMER_ID, [new RentalDetail(MATRIX_FILM_ID, 1)]))

        when: "film is going to be returned"
        RentalDto rentalDto = facade.returnFilm(new ReturnCommand(0L))

        then: "should calculate additional charge for 8 days"
        rentalDto.getLateReturnCharge() == 320.00

        and: "rental return date is set to 18th of October 2018"
        1 * timeProvider.now() >> LocalDate.of(2018, 10, 16)
    }

    def "Should return regular rented film and calculate additional charge"() {
        given: "a film is in the system"
        filmFacade.findAllByIds(_ as Set) >> [getSpiderManFilmDto()]

        and: "Spider man film is rented"
        facade.rent(new RentalCommand(CUSTOMER_ID, [new RentalDetail(SPIDER_MAN_FILM_ID, 3)]))

        when: "we are going to return the film"
        RentalDto rentalDto = facade.returnFilm(new ReturnCommand(0L))

        then: "additional charge for 5 days should be calculated"
        rentalDto.getLateReturnCharge() == 150.0

        and: "next time rental return date is set to 18th of October 2018"
        1 * timeProvider.now() >> LocalDate.of(2018, 10, 15)
    }

    def "Should return regular rented film without additional charge"() {
        given: "a film is in the system"
        filmFacade.findAllByIds(_ as Set) >> [getSpiderManFilmDto()]

        and: "Spider man film is rented"
        facade.rent(new RentalCommand(CUSTOMER_ID, [new RentalDetail(SPIDER_MAN_FILM_ID, 3)]))

        when: "we are going to return the film"
        RentalDto rentalDto = facade.returnFilm(new ReturnCommand(0L))

        then: "no additional charge should be added"
        rentalDto.getLateReturnCharge() == 0

        and: "rental return date is set to 10th of October 2018"
        1 * timeProvider.now() >> LocalDate.of(2018, 10, 10)
    }


    private static FilmDto getMatrixFilmDto() {
        new FilmDto(MATRIX_FILM_ID, "Matrix 11", FilmType.NEW_RELEASE)
    }

    private static FilmDto getSpiderManFilmDto() {
        new FilmDto(SPIDER_MAN_FILM_ID, "Spider Man", FilmType.REGULAR)
    }

    private static FilmDto getSpiderMan2FilmDto() {
        new FilmDto(SPIDER_MAN_2_FILM_ID, "Spider Man 2", FilmType.REGULAR)
    }

    private static FilmDto getOutOfAfricaFilmDto() {
        new FilmDto(OUT_OF_AFRICA_FILM_ID, "Out of Africa", FilmType.OLD)
    }

    class Fields {
        public static final Long CUSTOMER_ID = 100L
        public static final Long MATRIX_FILM_ID = 1L
        public static final Long SPIDER_MAN_FILM_ID = 2L
        public static final Long SPIDER_MAN_2_FILM_ID = 3L
        public static final Long OUT_OF_AFRICA_FILM_ID = 4L
        public static final LocalDate TEST_NOMINAL_DATE = LocalDate.of(2018, 10, 07)
    }
}
