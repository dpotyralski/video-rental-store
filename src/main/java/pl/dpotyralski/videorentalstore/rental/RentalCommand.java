package pl.dpotyralski.videorentalstore.rental;

import lombok.Value;

import java.util.List;

@Value
class RentalCommand {

    private Long customerId;
    private List<RentalDetail> filmDetails;

}
