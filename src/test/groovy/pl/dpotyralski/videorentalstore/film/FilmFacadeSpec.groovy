package pl.dpotyralski.videorentalstore.film

import org.springframework.data.domain.PageRequest
import spock.lang.Specification


class FilmFacadeSpec extends Specification {

    FilmFacade facade

    void setup() {
        facade = new FilmConfigurator().filmFacade()
    }

    def "Should save new film and be able to find it"() {
        when: "film is added"
        facade.addFilm(Fields.MATRIX_FILM_TITLE, FilmType.NEW_RELEASE)

        then: "system has this film"
        facade.findById(Fields.MATRIX_FILM_ID) != null
    }

    def "Should find all saved films"() {
        given: "two films are in system"
        facade.addFilm(Fields.MATRIX_FILM_TITLE, FilmType.NEW_RELEASE)
        facade.addFilm(Fields.STAR_WARS_TITLE, FilmType.REGULAR)

        when: "we ask system for all saved films"
        List<FilmDto> all = facade.findAll(new PageRequest(0, 20))

        then: "system returns saved films"
        all.size() == 2
    }

    class Fields {
        static final MATRIX_FILM_ID = 0L
        static final MATRIX_FILM_TITLE = "Matrix"
        static final STAR_WARS_TITLE = "Star Wars"
    }

}
