package pl.dpotyralski.videorentalstore.film;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface FilmRepository extends JpaRepository<Film, Long> {

    Optional<Film> findById(long id);

    Film save(Film film);

    PageImpl<Film> findAll(Pageable pageable);
}
