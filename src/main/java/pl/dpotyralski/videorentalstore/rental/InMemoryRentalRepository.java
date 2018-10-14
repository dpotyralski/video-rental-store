package pl.dpotyralski.videorentalstore.rental;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

class InMemoryRentalRepository implements RentalRepository {

    private ConcurrentHashMap<Long, Rental> map = new ConcurrentHashMap<>();

    private AtomicLong counter = new AtomicLong();

    @Override
    public <S extends Rental> S save(S rental) {
        Field id = ReflectionUtils.findField(Rental.class, "id");
        ReflectionUtils.makeAccessible(id);
        ReflectionUtils.setField(id, rental, counter.getAndIncrement());
        map.put(rental.getId(), rental);
        return rental;
    }

    @Override
    public <S extends Rental> List<S> saveAll(Iterable<S> entities) {
        List<S> results = new ArrayList<>();
        for (S entity : entities) {
            results.add(save(entity));
        }
        return results;
    }

    @Override
    public Optional<Rental> findById(Long rentalId) {
        return Optional.ofNullable(map.get(rentalId));
    }

    @Override
    public List<Rental> findAllByCustomerId(Long customerId) {
        return map.values()
                .stream()
                .filter(rental -> rental.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Rental> findCurrentRentalsByCustomerId(Long customerId) {
        return map.values()
                .stream()
                .filter(rental -> rental.getCustomerId().equals(customerId))
                .filter(rental -> !rental.isReturned())
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Rental> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Rental> findAll(Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<Rental> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Rental> findAllById(Iterable<Long> longs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Rental entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(Iterable<? extends Rental> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Rental> S saveAndFlush(S entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteInBatch(Iterable<Rental> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllInBatch() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Rental getOne(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Rental> Optional<S> findOne(Example<S> example) {

        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Rental> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Rental> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Rental> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Rental> long count(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Rental> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException();
    }
}
