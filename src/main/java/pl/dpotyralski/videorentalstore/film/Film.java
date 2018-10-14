package pl.dpotyralski.videorentalstore.film;

import lombok.Getter;
import pl.dpotyralski.videorentalstore.infrastructure.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "films")
class Film extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private FilmType type;

    protected Film() {
    }

    public Film(String title, FilmType type) {
        this.title = title;
        this.type = type;
    }

    public FilmDto toDto() {
        return new FilmDto(this.getId(), this.getTitle(), this.getType());
    }
}
