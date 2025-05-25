package by.akonashchenko.telegram.webapp.auth.domain.service.security;

import by.akonashchenko.telegram.webapp.auth.domain.model.InitData;
import by.akonashchenko.telegram.webapp.auth.domain.model.security.TelegramUserDetails;
import by.akonashchenko.telegram.webapp.auth.domain.persistence.model.UserJpa;
import by.akonashchenko.telegram.webapp.auth.domain.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramUserDetailsService implements UserDetailsService {

    private final InitDataValidationService initDataValidationService;
    private final InitDataOperationService initDataOperationService;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(existing -> createTelegramUserDetails(existing, Instant.now().getEpochSecond()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username"));
    }

    @Transactional
    public UserDetails loadUserByInitData(String initDataString) {
        InitData initData = initDataValidationService.validate(initDataString);
        return tryLoadById(initData)
                .or(() -> tryLoadByUsername(initData))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User was not loaded by provided data:" + initDataString
                ));
    }

    private Optional<UserDetails> tryLoadById(InitData initData) {
        InitData.User initDataUser = initData.getUser();
        if (!initDataUser.hasId()) {
            return Optional.empty();
        }
        return userRepository.findByExternalId(initDataUser.getId())
                .map(existing -> createTelegramUserDetails(existing, initData))
                .or(() -> Optional.of(createTelegramUserDetails(initData)))
                .map(UserDetails.class::cast);
    }

    private Optional<UserDetails> tryLoadByUsername(InitData initData) {
        InitData.User initDataUser = initData.getUser();
        if (!initDataUser.hasUsername()) {
            return Optional.empty();
        }
        return userRepository.findByUsername(initDataUser.getUsername())
                .map(existing -> createTelegramUserDetails(existing, initData))
                .or(() -> Optional.of(createTelegramUserDetails(initData)))
                .map(UserDetails.class::cast);
    }

    private TelegramUserDetails createTelegramUserDetails(
            UserJpa existingUser,
            InitData initData
    ) {
        actualizeUserData(initData.getUser(), existingUser);
        return createTelegramUserDetails(existingUser, initData.getAuthDate());
    }

    private void actualizeUserData(InitData.User source, UserJpa destination) {
        destination.setUsername(source.getUsername());
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        actualizeRole(source, destination);
        destination.setPremium(source.isPremium());
    }

    private void actualizeRole(InitData.User source, UserJpa destination) {
        if (source.isPremium() != destination.isPremium()) {
            destination.setRole(initDataOperationService.resolveUserRole(source));
        }
    }

    private TelegramUserDetails createTelegramUserDetails(
            UserJpa existingUser,
            long authDate
    ) {
        log.info("Loading existing user");
        return TelegramUserDetails.of(
                existingUser,
                authDate
        );
    }

    private TelegramUserDetails createTelegramUserDetails(InitData initData) {
        log.info("Loading new user");
        return TelegramUserDetails.of(
                initDataOperationService.createUser(initData),
                initData.getAuthDate()
        );
    }
}
