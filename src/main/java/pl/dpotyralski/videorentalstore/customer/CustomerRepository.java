package pl.dpotyralski.videorentalstore.customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

interface CustomerRepository extends Repository<Customer, Long> {

    Optional<Customer> findById(Long id);

    @Query("select c.bonusPoints from Customer c where c.id = :id")
    Optional<Integer> findBonusPointsByCustomerId(@Param("id") Long id);

    Customer save(Customer customer);
}
