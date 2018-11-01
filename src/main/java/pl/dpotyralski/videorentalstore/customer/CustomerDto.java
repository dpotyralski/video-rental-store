package pl.dpotyralski.videorentalstore.customer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerDto {

    private Long id;
    private String username;
    private int bonusPoints;

}
