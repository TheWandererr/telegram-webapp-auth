package by.akonashchenko.telegram.webapp.auth.web.controller;

import by.akonashchenko.telegram.webapp.auth.base.IntegrationTest;
import by.akonashchenko.telegram.webapp.auth.config.model.AuthConfig;
import by.akonashchenko.telegram.webapp.auth.domain.model.InitData;
import by.akonashchenko.telegram.webapp.auth.domain.persistence.model.AuthorityJpa;
import by.akonashchenko.telegram.webapp.auth.domain.persistence.model.UserRoleJpa;
import by.akonashchenko.telegram.webapp.auth.domain.persistence.repository.AuthorityRepository;
import by.akonashchenko.telegram.webapp.auth.domain.persistence.repository.UserRoleRepository;
import by.akonashchenko.telegram.webapp.auth.domain.service.hashing.InitDataHashService;
import by.akonashchenko.telegram.webapp.auth.domain.service.security.InitDataParser;
import by.akonashchenko.telegram.webapp.auth.domain.service.security.InitDataValidationServiceTestWrapper;
import by.akonashchenko.telegram.webapp.auth.util.Constant;
import by.akonashchenko.telegram.webapp.auth.web.jte.model.User;
import by.akonashchenko.telegram.webapp.auth.web.service.JteEngineServiceTestWrapper;
import gg.jte.TemplateEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.OK;

public class AuthControllerTest extends IntegrationTest {

    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private InitDataParser initDataParser;
    @Autowired
    private InitDataHashService initDataHashService;
    @Autowired
    private AuthConfig authConfig;
    @Autowired
    private TemplateEngine templateEngine;

    private InitDataValidationServiceTestWrapper initDataValidationService;
    private JteEngineServiceTestWrapper jteEngineService;

    @BeforeEach
    public void beforeEach() {
        cleanUpData();
        insertData();
        initDataValidationService = new InitDataValidationServiceTestWrapper(
                authConfig,
                initDataParser,
                initDataHashService
        );
        jteEngineService = new JteEngineServiceTestWrapper(templateEngine);
    }

    private void cleanUpData() {
        userRoleRepository.deleteAll();
        authorityRepository.deleteAll();
    }

    private void insertData() {
        UserRoleJpa regularRole = new UserRoleJpa();
        regularRole.setName(Constant.Roles.REGULAR);
        AuthorityJpa regularRoleAuthorityJpa = new AuthorityJpa();
        regularRoleAuthorityJpa.setValue("WRITE");
        regularRoleAuthorityJpa.setRole(regularRole);
        regularRole.getAuthorities().add(regularRoleAuthorityJpa);

        UserRoleJpa premiumRole = new UserRoleJpa();
        premiumRole.setName(Constant.Roles.PREMIUM);
        AuthorityJpa premiumRoleAuthorityJpa = new AuthorityJpa();
        premiumRoleAuthorityJpa.setValue("WRITE");
        premiumRoleAuthorityJpa.setRole(premiumRole);
        premiumRole.getAuthorities().add(premiumRoleAuthorityJpa);

        authorityRepository.saveAll(
                List.of(
                        regularRoleAuthorityJpa,
                        premiumRoleAuthorityJpa
                )
        );
        userRoleRepository.saveAll(
                List.of(
                        regularRole,
                        premiumRole
                )
        );
    }

    @Test
    void shouldLoginByValidInitData() {
        // given
        StringBuilder initDataBuilder = new StringBuilder(INITIAL_INIT_DATA_STRING);
        Instant authDate = Instant.now();
        initDataBuilder.append(Constant.Symbols.AND)
                .append(Constant.InitData.AUTH_DATE_KEY)
                .append(Constant.Symbols.EQUALS)
                .append(authDate.getEpochSecond());
        InitData checkStringInitData = initDataParser.parse(initDataBuilder.toString());
        String checkString = initDataValidationService.createCheckString(checkStringInitData);
        String hash = initDataHashService.hmacHex(checkString);
        initDataBuilder.append(Constant.Symbols.AND)
                .append(Constant.InitData.HASH_KEY)
                .append(Constant.Symbols.EQUALS)
                .append(hash);
        String initDataString = initDataBuilder.toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, Constant.InitData.AUTHORIZATION_PREFIX + initDataString);

        HttpEntity<String> request = new HttpEntity<>(null, headers);

        // then
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/auth" + Constant.UriComponents.LOGIN,
                request,
                String.class
        );
        assertThat(response.getStatusCode())
                .isEqualTo(OK);
        String content = jteEngineService.render(
                "user.jte",
                convert(initDataParser.parse(initDataString))
        );
        assertThat(response.getBody())
                .isEqualTo(content);
        assertThat(response.getHeaders())
                .isNotNull();
        assertTrue(response.getHeaders().containsKey(HttpHeaders.SET_COOKIE));
    }

    private User convert(InitData initData) {
        InitData.User initDataUser = initData.getUser();
        return User.builder()
                .id(initDataUser.getId())
                .firstName(initDataUser.getFirstName())
                .lastName(initDataUser.getLastName())
                .username(initDataUser.getUsername())
                .premium(initDataUser.isPremium())
                .build();
    }

}
