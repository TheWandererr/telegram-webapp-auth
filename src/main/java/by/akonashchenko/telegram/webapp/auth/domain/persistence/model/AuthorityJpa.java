package by.akonashchenko.telegram.webapp.auth.domain.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "authorities")
@Getter
@Setter
public class AuthorityJpa {

    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne(fetch = LAZY, cascade = ALL, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private UserRoleJpa role;
    @Column(nullable = false)
    private String value;

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (Hibernate.getClass(this) != Hibernate.getClass(object)) {
            return false;
        }

        AuthorityJpa that = (AuthorityJpa) object;
        if (getId() != null) {
            return Objects.equals(getId(), that.getId());
        }
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return this instanceof HibernateProxy proxy
                ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
