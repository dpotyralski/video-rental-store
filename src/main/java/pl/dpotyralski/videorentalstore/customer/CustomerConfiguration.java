package pl.dpotyralski.videorentalstore.customer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CustomerConfiguration {

    CustomerFacade customerFacade() {
        InMemoryCustomerRepository inMemoryCustomerRepository = new InMemoryCustomerRepository();
        return customerFacade(inMemoryCustomerRepository);
    }

    @Bean
    public CustomerFacade customerFacade(CustomerRepository customerRepository) {
        CustomerSearch customerSearch = new CustomerSearch(customerRepository);
        CustomerCreator customerCreator = new CustomerCreator((customerRepository));
        return new CustomerFacade(customerSearch, customerCreator);
    }

}
