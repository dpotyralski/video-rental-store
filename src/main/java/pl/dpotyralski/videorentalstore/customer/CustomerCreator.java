package pl.dpotyralski.videorentalstore.customer;

class CustomerCreator {

    private final CustomerRepository customerRepository;

    CustomerCreator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    Customer createCustomer(CreateCustomerCommand createCustomerCommand) {
        Customer customer = new Customer(createCustomerCommand.getUsername());
        return customerRepository.save(customer);
    }

}
