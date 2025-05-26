package by.akonashchenko.telegram.webapp.auth.domain.service.security;

import by.akonashchenko.telegram.webapp.auth.config.model.AuthConfig;
import by.akonashchenko.telegram.webapp.auth.domain.model.InitData;
import by.akonashchenko.telegram.webapp.auth.domain.model.security.TelegramUserDetails;
import by.akonashchenko.telegram.webapp.auth.domain.persistence.model.UserJpa;
import by.akonashchenko.telegram.webapp.auth.domain.persistence.repository.UserRepository;
import by.akonashchenko.telegram.webapp.auth.web.model.AuthDetailsSnapshot;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramUserDetailsService {

    private final AuthConfig authConfig;
    private final InitDataValidationService initDataValidationService;
    private final InitDataOperationService initDataOperationService;
    private final UserRepository userRepository;

    @Transactional
    public TelegramUserDetails loadUserByInitData(String initDataString) {
        InitData initData = initDataValidationService.validate(initDataString);
        return tryLoadById(initData)
                .or(() -> tryLoadByUsername(initData))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User was not loaded by provided data:" + initDataString
                ));
    }

    @Transactional
    public TelegramUserDetails loadUserByAuthSnapshot(AuthDetailsSnapshot authDetailsSnapshot) {
        return userRepository.findByExternalId(authDetailsSnapshot.getExternalId())
                .map(existing -> createTelegramUserDetails(existing, Instant.now().getEpochSecond()))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User was not loaded by provided ID:" + authDetailsSnapshot.getExternalId()
                ));
    }

    private Optional<TelegramUserDetails> tryLoadById(InitData initData) {
        InitData.User initDataUser = initData.getUser();
        if (!initDataUser.hasId()) {
            return Optional.empty();
        }
        return userRepository.findByExternalId(initDataUser.getId())
                .map(existing -> createTelegramUserDetails(existing, initData))
                .or(() -> Optional.of(createTelegramUserDetails(initData)));
    }

    private Optional<TelegramUserDetails> tryLoadByUsername(InitData initData) {
        InitData.User initDataUser = initData.getUser();
        if (!initDataUser.hasUsername()) {
            return Optional.empty();
        }
        return userRepository.findByUsername(initDataUser.getUsername())
                .map(existing -> createTelegramUserDetails(existing, initData))
                .or(() -> Optional.of(createTelegramUserDetails(initData)));
    }

    private TelegramUserDetails createTelegramUserDetails(
            UserJpa existingUser,
            InitData initData
    ) {
        log.info("Loading existing user by InitData");
        actualizeUserData(initData.getUser(), existingUser);
        return createTelegramUserDetails(existingUser, initData.getAuthDate());
    }

    private TelegramUserDetails createTelegramUserDetails(InitData initData) {
        log.info("Loading new user by InitData");
        UserJpa user = initDataOperationService.createUser(initData);
        return createTelegramUserDetails(user, Instant.now().getEpochSecond());
    }

    private TelegramUserDetails createTelegramUserDetails(
            UserJpa existingUser,
            long authDate
    ) {
        return TelegramUserDetails.init(
                existingUser,
                authDate,
                authConfig
        );
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
}
