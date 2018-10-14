package pl.dpotyralski.videorentalstore.customer;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerDto {

    private Long id;
    private String username;
    private int bonusPoints;

}
