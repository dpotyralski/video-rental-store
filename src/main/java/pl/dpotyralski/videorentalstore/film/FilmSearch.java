package pl.dpotyralski.videorentalstore.film;

import org.springframework.data.domain.Pageable;
import pl.dpotyralski.videorentalstore.film.exceptions.FilmNotFound;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

class FilmSearch {

    private final FilmRepository filmRepository;

    FilmSearch(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    List<FilmDto> findAll(Pageable pageable) {
        return filmRepository.findAll(pageable).stream()
                .map(Film::toDto)
                .collect(toList());
    }

    FilmDto findById(long id) {
        return filmRepository.findById(id)
                .map(Film::toDto)
                .orElseThrow(FilmNotFound::new);
    }

    List<FilmDto> findAllByIds(Set<Long> ids) {
        return filmRepository.findAllById(ids).stream()
                .map(Film::toDto)
                .collect(toList());
    }
}
