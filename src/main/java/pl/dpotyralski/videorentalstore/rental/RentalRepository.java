package pl.dpotyralski.videorentalstore.rental;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findAllByCustomerId(Long customerId);

    @Query("select r from Rental r where r.returned = false")
    List<Rental> findCurrentRentalsByCustomerId(Long customerId);

}
