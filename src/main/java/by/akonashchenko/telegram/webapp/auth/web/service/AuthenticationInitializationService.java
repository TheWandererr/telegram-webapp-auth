package by.akonashchenko.telegram.webapp.auth.web.service;

import by.akonashchenko.telegram.webapp.auth.domain.model.security.TelegramUserDetails;
import by.akonashchenko.telegram.webapp.auth.util.AuthenticationUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationInitializationService {

    private final CookieService cookieService;

    public void initialize(TelegramUserDetails userDetails, HttpServletResponse response) {
        AuthenticationUtils.initAuthentication(userDetails);
        Cookie cookie = cookieService.createAuthCookie(userDetails);
        response.addCookie(cookie);
    }
}
