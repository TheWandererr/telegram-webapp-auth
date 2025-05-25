package by.akonashchenko.telegram.webapp.auth.domain.persistence.repository;

import by.akonashchenko.telegram.webapp.auth.domain.persistence.model.UserRoleJpa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRoleJpa, String> {

    UserRoleJpa findByName(String name);

}
