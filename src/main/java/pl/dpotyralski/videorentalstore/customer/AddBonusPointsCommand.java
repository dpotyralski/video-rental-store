package pl.dpotyralski.videorentalstore.customer;

import lombok.Value;

@Value
public class AddBonusPointsCommand {

    private int bonusPoints;
    private long customerId;

}
