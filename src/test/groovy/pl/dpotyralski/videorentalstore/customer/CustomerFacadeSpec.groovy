package pl.dpotyralski.videorentalstore.customer

import pl.dpotyralski.videorentalstore.customer.exception.CustomerBonusPointsNotFound
import spock.lang.Specification

class CustomerFacadeSpec extends Specification {

    CustomerFacade facade

    void setup() {
        facade = new CustomerConfiguration().customerFacade()
    }

    def "Should be possible to add bonus points to customer"() {
        given: "customer with zero points is in system"
        CustomerDto customer = facade.saveCustomer(new CreateCustomerCommand(Fields.TEST_USERNAME))

        when: "we ask to add 10 bonus points"
        facade.addBonusPointsToCustomer(new AddBonusPointsCommand(10, customer.getId()))

        and: "we ask to add 5 points more"
        facade.addBonusPointsToCustomer(new AddBonusPointsCommand(5, customer.getId()))

        then: "customer should have 15 bonus points"
        facade.getCustomerBonusPoints(customer.getId()) == 15
    }

    def "Should throw an exception when customer has not been found"() {
        when: "we ask to add 10 bonus points"
        facade.getCustomerBonusPoints(Fields.NOT_EXISTING_CUSTOMER_ID)

        then: "exception should be thrown"
        thrown(CustomerBonusPointsNotFound)
    }

    class Fields {
        public static final TEST_USERNAME = "test@mail.com"
        public static final long NOT_EXISTING_CUSTOMER_ID = 1L
    }
}
