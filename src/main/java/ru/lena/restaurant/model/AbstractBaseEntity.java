package ru.lena.restaurant.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@MappedSuperclass
@Access(AccessType.FIELD)
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AbstractBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    public boolean isNew() {
        return this.id == null;
    }

    public String toString() {
        return getClass().getSimpleName() + ":" + id;
    }
}
