package by.akonashchenko.telegram.webapp.auth.domain.persistence.repository;


import by.akonashchenko.telegram.webapp.auth.domain.persistence.model.UserJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserJpa, String> {

    Optional<UserJpa> findByUsername(String username);

    Optional<UserJpa> findByExternalId(Long externalId);
}
