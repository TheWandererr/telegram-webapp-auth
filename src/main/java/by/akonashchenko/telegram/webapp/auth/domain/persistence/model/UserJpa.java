package by.akonashchenko.telegram.webapp.auth.domain.persistence.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserJpa {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(unique = true)
    private Long externalId;
    @Column(unique = true, nullable = false)
    private String username;
    private String firstName;
    private String lastName;
    private boolean premium;
    private boolean banned;
    private boolean active;
    @ManyToOne(cascade = ALL, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private UserRoleJpa role;

    @Override
    public final boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (Hibernate.getClass(this) != Hibernate.getClass(object)) {
            return false;
        }

        UserJpa that = (UserJpa) object;
        if (getId() != null) {
            return Objects.equals(getId(), that.getId());
        }
        return Objects.equals(getExternalId(), that.getExternalId());
    }

    @Override
    public final int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }
}
