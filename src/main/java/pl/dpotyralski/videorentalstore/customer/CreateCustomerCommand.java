package pl.dpotyralski.videorentalstore.customer;

import lombok.Value;

@Value
class CreateCustomerCommand {
    private String username;
}
