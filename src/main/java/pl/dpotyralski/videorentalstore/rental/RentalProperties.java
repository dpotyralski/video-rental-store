package pl.dpotyralski.videorentalstore.rental;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "video-store")
class RentalProperties {

    @NotNull
    private BigDecimal basicPrice;

    @NotNull
    private BigDecimal premiumPrice;

}
