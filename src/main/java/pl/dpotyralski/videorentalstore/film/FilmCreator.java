package pl.dpotyralski.videorentalstore.film;

class FilmCreator {

    private final FilmRepository filmRepository;

    FilmCreator(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    FilmDto createFilm(String title, FilmType filmType) {
        return filmRepository.save(new Film(title, filmType)).toDto();
    }
}
