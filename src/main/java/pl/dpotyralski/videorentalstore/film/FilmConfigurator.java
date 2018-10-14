package pl.dpotyralski.videorentalstore.film;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FilmConfigurator {

    FilmFacade filmFacade() {
        InMemoryFilmRepository inMemoryFilmRepository = new InMemoryFilmRepository();
        return filmFacade(inMemoryFilmRepository);
    }

    @Bean
    FilmFacade filmFacade(FilmRepository filmRepository) {
        FilmCreator filmCreator = new FilmCreator(filmRepository);
        FilmSearch filmSearch = new FilmSearch(filmRepository);
        return new FilmFacade(filmCreator, filmSearch);
    }

}
