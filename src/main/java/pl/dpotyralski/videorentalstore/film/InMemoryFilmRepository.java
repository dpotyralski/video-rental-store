package pl.dpotyralski.videorentalstore.film;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

class InMemoryFilmRepository implements FilmRepository {

    private ConcurrentHashMap<Long, Film> map = new ConcurrentHashMap<>();

    private AtomicLong counter = new AtomicLong();

    public Film save(Film film) {
        Field id = ReflectionUtils.findField(Film.class, "id");
        ReflectionUtils.makeAccessible(id);
        ReflectionUtils.setField(id, film, counter.getAndIncrement());
        map.put(film.getId(), film);
        return film;
    }

    @Override
    public PageImpl<Film> findAll(Pageable pageable) {
        ArrayList<Film> films = new ArrayList<>(map.values());
        return new PageImpl<>(films, pageable, films.size());
    }

    @Override
    public Optional<Film> findById(long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Film> findAllById(Iterable<Long> ids) {
        HashSet<Object> objects = new HashSet<>();
        ids.forEach(objects::add);

        return map.values().stream()
                .filter(film -> objects.contains(film.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> findAll() {
        return null;
    }

    @Override
    public List<Film> findAll(Sort sort) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Film entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends Film> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Film> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Film> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Film> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Film> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Film getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends Film> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Film> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Film> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Film> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Film> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Film> boolean exists(Example<S> example) {
        return false;
    }
}
