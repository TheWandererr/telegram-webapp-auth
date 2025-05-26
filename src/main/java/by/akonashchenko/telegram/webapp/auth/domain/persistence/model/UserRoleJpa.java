package by.akonashchenko.telegram.webapp.auth.domain.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;
import static java.util.Objects.isNull;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
public class UserRoleJpa {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(unique = true, nullable = false)
    private String name;
    @OneToMany(cascade = ALL, orphanRemoval = true, mappedBy = "role")
    private Set<AuthorityJpa> authorities;

    public Set<AuthorityJpa> getAuthorities() {
        if (isNull(authorities)) {
            authorities = new HashSet<>();
        }
        return authorities;
    }

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

        UserRoleJpa that = (UserRoleJpa) object;
        if (getId() != null) {
            return Objects.equals(getId(), that.getId());
        }
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return this instanceof HibernateProxy proxy
                ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
