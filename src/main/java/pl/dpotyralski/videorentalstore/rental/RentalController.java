package pl.dpotyralski.videorentalstore.rental;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.dpotyralski.videorentalstore.rental.protocol.request.FilmDetailsRequest;
import pl.dpotyralski.videorentalstore.rental.protocol.request.RentRequest;
import pl.dpotyralski.videorentalstore.rental.protocol.request.RentalPriceCalculateRequest;
import pl.dpotyralski.videorentalstore.rental.protocol.response.RentalPriceResponse;
import pl.dpotyralski.videorentalstore.rental.protocol.response.RentalSummaryResponse;
import pl.dpotyralski.videorentalstore.rental.protocol.response.ReturnFilmResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/rentals")
@AllArgsConstructor
class RentalController {

    private final RentalFacade rentalFacade;

    @GetMapping
    @ApiOperation(value = "Customer rentals by customer Id and option to get only current/active or all rentals")
    public RentalSummaryResponse findCustomersRentals(@RequestParam Long byCustomerId,
            @RequestParam(required = false) boolean onlyActive) {

        List<RentalSummaryResponse.RentalResponse> rentalResponses =
                rentalFacade.findRentalsByCustomerId(byCustomerId, onlyActive).stream()
                        .map(this::getCustomerRentalResponse)
                        .collect(toList());

        return new RentalSummaryResponse(rentalResponses);
    }

    @PostMapping
    @ApiOperation(value = "Customer rent film endpoint")
    public RentalPriceResponse rentFilm(@RequestBody @Valid RentRequest rentRequest) {
        RentalCommand rentalCommand = convertToRentCommand(rentRequest);
        return new RentalPriceResponse(rentalFacade.rent(rentalCommand));
    }

    @PostMapping(path = "/price-calculation")
    @ApiOperation(value = "Rental price calculation endpoint")
    public RentalPriceResponse calculateRentalPrice(@RequestBody @Valid RentalPriceCalculateRequest rentalPriceCalculateRequest) {
        return new RentalPriceResponse(rentalFacade.calculateRentalPrice(convertToRentalPriceCalculateCommand(rentalPriceCalculateRequest)));
    }

    @PostMapping(path = "/{id}/return")
    @ApiOperation(value = "Rental return endpoint")
    public ReturnFilmResponse returnFilm(@PathVariable Long id) {
        ReturnCommand returnCommand = convertToReturnCommand(id);
        RentalDto rentalDto = rentalFacade.returnFilm(returnCommand);
        return new ReturnFilmResponse(rentalDto.getLateReturnCharge());
    }

    private RentalSummaryResponse.RentalResponse getCustomerRentalResponse(RentalDto rentalDto) {
        return new RentalSummaryResponse.RentalResponse(rentalDto.getId(), rentalDto.getPriceCalculated());
    }

    private RentalPriceCalculateCommand convertToRentalPriceCalculateCommand(RentalPriceCalculateRequest rentalPriceCalculateRequest) {
        return new RentalPriceCalculateCommand(map(rentalPriceCalculateRequest.getDetails()));
    }

    private RentalCommand convertToRentCommand(RentRequest rentRequest) {
        return new RentalCommand(rentRequest.getCustomerId(), map(rentRequest.getDetails()));
    }

    private ReturnCommand convertToReturnCommand(Long id) {
        return new ReturnCommand(id);
    }

    private List<RentalDetail> map(Set<FilmDetailsRequest> requestFilmDetails) {
        return requestFilmDetails.stream()
                .map(this::createFilmDetail)
                .collect(Collectors.toList());
    }

    private RentalDetail createFilmDetail(FilmDetailsRequest film) {
        return new RentalDetail(film.getFilmId(), film.getDays());
    }

}
