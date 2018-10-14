package pl.dpotyralski.videorentalstore.film;

import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.dpotyralski.videorentalstore.film.protocol.response.FilmsResponse;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmFacade filmFacade;

    public FilmController(FilmFacade filmFacade) {
        this.filmFacade = filmFacade;
    }

    @GetMapping
    @ApiOperation(value = "List of films in the store")
    public FilmsResponse getFilms(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<FilmsResponse.FilmResponse> collect = filmFacade.findAll(PageRequest.of(page, size)).stream()
                .map(this::createFilmResponse)
                .collect(toList());

        return new FilmsResponse(collect);
    }

    private FilmsResponse.FilmResponse createFilmResponse(FilmDto filmDto) {
        return new FilmsResponse.FilmResponse(filmDto.getId(), filmDto.getTitle());
    }

}
