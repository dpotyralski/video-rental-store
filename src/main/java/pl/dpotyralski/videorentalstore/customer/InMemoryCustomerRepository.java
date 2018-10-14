package pl.dpotyralski.videorentalstore.customer;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

class InMemoryCustomerRepository implements CustomerRepository {

    private ConcurrentHashMap<Long, Customer> map = new ConcurrentHashMap<>();

    private AtomicLong counter = new AtomicLong();

    @Override
    public Optional<Customer> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public Optional<Integer> findBonusPointsByCustomerId(Long id) {
        return findById(id).map(Customer::getBonusPoints);
    }

    @Override
    public Customer save(Customer customer) {
        Field id = ReflectionUtils.findField(Customer.class, "id");
        ReflectionUtils.makeAccessible(id);
        ReflectionUtils.setField(id, customer, counter.getAndIncrement());
        map.put(customer.getId(), customer);
        return customer;

    }
}
