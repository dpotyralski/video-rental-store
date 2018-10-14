package pl.dpotyralski.videorentalstore.film;

import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional
public class FilmFacade {

    private final FilmCreator filmCreator;
    private final FilmSearch filmSearch;

    public FilmFacade(FilmCreator filmCreator, FilmSearch filmSearch) {
        this.filmCreator = filmCreator;
        this.filmSearch = filmSearch;
    }

    @Transactional(readOnly = true)
    public FilmDto findById(long id) {
        return filmSearch.findById(id);
    }

    @Transactional(readOnly = true)
    public List<FilmDto> findAllByIds(Set<Long> ids) {
        return filmSearch.findAllByIds(ids);
    }

    @Transactional(readOnly = true)
    public List<FilmDto> findAll(PageRequest pageRequest) {
        return filmSearch.findAll(pageRequest);
    }

    public FilmDto addFilm(String title, FilmType filmType) {
        return filmCreator.createFilm(title, filmType);
    }

}
