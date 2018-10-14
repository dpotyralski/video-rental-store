package pl.dpotyralski.videorentalstore.customer;

import pl.dpotyralski.videorentalstore.customer.exception.CustomerBonusPointsNotFound;
import pl.dpotyralski.videorentalstore.customer.exception.CustomerNotFound;

class CustomerSearch {

    private final CustomerRepository customerRepository;

    CustomerSearch(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(CustomerNotFound::new);
    }

    int getBonusPointsByCustomerId(Long id) {
        return customerRepository.findBonusPointsByCustomerId(id)
                .orElseThrow(CustomerBonusPointsNotFound::new);
    }

}
