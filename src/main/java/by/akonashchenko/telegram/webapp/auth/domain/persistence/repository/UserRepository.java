package by.akonashchenko.telegram.webapp.auth.domain.persistence.repository;


import by.akonashchenko.telegram.webapp.auth.domain.persistence.model.UserJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserJpa, String> {

    @Query("SELECT u FROM UserJpa u JOIN FETCH u.role WHERE u.username = ?1")
    Optional<UserJpa> findByUsername(String username);

    @Query("SELECT u FROM UserJpa u JOIN FETCH u.role WHERE u.externalId = ?1")
    Optional<UserJpa> findByExternalId(Long externalId);
}
