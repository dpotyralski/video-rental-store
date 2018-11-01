package pl.dpotyralski.videorentalstore.customer;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CustomerFacade {

    private final CustomerSearch customerSearch;
    private final CustomerCreator customerCreator;

    public CustomerFacade(CustomerSearch customerSearch, CustomerCreator customerCreator) {
        this.customerSearch = customerSearch;
        this.customerCreator = customerCreator;
    }

    @Transactional(readOnly = true)
    public int getCustomerBonusPoints(Long id) {
        return customerSearch.getBonusPointsByCustomerId(id);
    }

    public void addBonusPointsToCustomer(AddBonusPointsCommand addBonusPointsCommand) {
        customerSearch.getCustomerById(addBonusPointsCommand.getCustomerId())
                .addBonusPoints(addBonusPointsCommand.getBonusPoints());
    }

    public CustomerDto saveCustomer(CreateCustomerCommand createCustomerCommand) {
        return customerCreator.createCustomer(createCustomerCommand).toDto();
    }

}
