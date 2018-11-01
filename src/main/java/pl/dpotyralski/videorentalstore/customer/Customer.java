package pl.dpotyralski.videorentalstore.customer;

import lombok.Getter;
import pl.dpotyralski.videorentalstore.infrastructure.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "customers")
class Customer extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private int bonusPoints = 0;

    protected Customer() {
    }

    Customer(String username) {
        this.username = username;
    }

    CustomerDto toDto() {
        return CustomerDto.builder()
                .id(id)
                .username(username)
                .bonusPoints(bonusPoints)
                .build();
    }

    void addBonusPoints(int bonusPoints) {
        this.bonusPoints += bonusPoints;
    }
}
