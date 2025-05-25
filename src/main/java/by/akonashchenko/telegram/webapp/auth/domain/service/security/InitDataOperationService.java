package by.akonashchenko.telegram.webapp.auth.domain.service.security;

import by.akonashchenko.telegram.webapp.auth.domain.model.InitData;
import by.akonashchenko.telegram.webapp.auth.domain.persistence.model.UserJpa;
import by.akonashchenko.telegram.webapp.auth.domain.persistence.model.UserRoleJpa;
import by.akonashchenko.telegram.webapp.auth.domain.persistence.repository.UserRepository;
import by.akonashchenko.telegram.webapp.auth.domain.persistence.repository.UserRoleRepository;
import by.akonashchenko.telegram.webapp.auth.util.Constant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InitDataOperationService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserJpa createUser(InitData initData) {
        InitData.User initDataUser = initData.getUser();
        UserJpa newUser = UserJpa.builder()
                .role(resolveUserRole(initData.getUser()))
                .active(true)
                .externalId(initDataUser.getId())
                .firstName(initDataUser.getFirstName())
                .lastName(initDataUser.getLastName())
                .premium(initDataUser.isPremium())
                .build();
        return userRepository.save(newUser);
    }

    public UserRoleJpa resolveUserRole(InitData.User initDataUser) {
        return initDataUser.isPremium()
                ? userRoleRepository.findByName(Constant.Jpa.PREMIUM_ROLE_NAME)
                : userRoleRepository.findByName(Constant.Jpa.REGULAR_ROLE_NAME);
    }
}
