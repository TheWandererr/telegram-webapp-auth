package by.akonashchenko.telegram.webapp.auth.domain.persistence.repository;

import by.akonashchenko.telegram.webapp.auth.domain.persistence.model.AuthorityJpa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<AuthorityJpa, String> {
}
