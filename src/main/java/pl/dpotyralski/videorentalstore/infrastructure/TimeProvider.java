package pl.dpotyralski.videorentalstore.infrastructure;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TimeProvider {

    public LocalDate now() {
        return LocalDate.now();
    }

}
