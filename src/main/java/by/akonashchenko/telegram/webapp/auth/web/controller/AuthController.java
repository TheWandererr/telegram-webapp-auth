package by.akonashchenko.telegram.webapp.auth.web.controller;

import by.akonashchenko.telegram.webapp.auth.domain.model.security.TelegramUserDetails;
import by.akonashchenko.telegram.webapp.auth.domain.service.security.TelegramUserDetailsService;
import by.akonashchenko.telegram.webapp.auth.web.jte.converter.UserConverter;
import by.akonashchenko.telegram.webapp.auth.web.jte.model.User;
import by.akonashchenko.telegram.webapp.auth.web.service.AuthenticationInitializationService;
import by.akonashchenko.telegram.webapp.auth.web.service.JteResponseWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import static by.akonashchenko.telegram.webapp.auth.util.Constant.UriComponents.LOGIN;
import static by.akonashchenko.telegram.webapp.auth.util.RequestUtils.getTelegramWebAppInitData;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TelegramUserDetailsService telegramUserDetailsService;
    private final AuthenticationInitializationService authenticationInitializationService;
    private final JteResponseWriter responseWriter;

    @SneakyThrows
    @PostMapping(LOGIN)
    public ResponseEntity<String> login(
            @RequestHeader(name = AUTHORIZATION) String authorization,
            HttpServletResponse response
    ) {
        String initData = getTelegramWebAppInitData(authorization);
        TelegramUserDetails telegramUserDetails
                = telegramUserDetailsService.loadUserByInitData(initData);
        authenticationInitializationService.initialize(
                telegramUserDetails,
                response
        );
        User model = UserConverter.INSTANCE.map(telegramUserDetails);
        return responseWriter.writeHtml("user.jte", model);
    }
}
