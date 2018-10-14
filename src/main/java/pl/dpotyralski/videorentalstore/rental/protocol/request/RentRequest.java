package pl.dpotyralski.videorentalstore.rental.protocol.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class RentRequest {

    @NotNull
    private Long customerId;

    @Valid
    @NotEmpty
    private Set<FilmDetailsRequest> details;

}
