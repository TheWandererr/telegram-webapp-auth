package by.akonashchenko.telegram.webapp.auth.web.service;

import by.akonashchenko.telegram.webapp.auth.domain.model.security.TelegramUserDetails;
import by.akonashchenko.telegram.webapp.auth.domain.service.hashing.InitDataHashService;
import by.akonashchenko.telegram.webapp.auth.web.model.AuthDetailsSnapshot;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

import static by.akonashchenko.telegram.webapp.auth.util.Constant.Cookies.AUTH;
import static by.akonashchenko.telegram.webapp.auth.util.Constant.Symbols.COLON;

@Service
@RequiredArgsConstructor
public class CookieService {

    public static final String DELIMITER = COLON;

    private final InitDataHashService initDataHashService;

    public Cookie createAuthCookie(TelegramUserDetails userDetails) {
        String userId = String.valueOf(userDetails.externalId());
        String hashedUserId = initDataHashService.hmacHex(userId);
        Cookie cookie = new Cookie(AUTH, userId + DELIMITER + hashedUserId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(calculateMaxAge(userDetails.expiredAt()));
        return cookie;
    }

    private int calculateMaxAge(Instant expiredAt) {
        return (int) Duration.between(Instant.now(), expiredAt).getSeconds();
    }

    public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> name.equals(cookie.getName()))
                .findFirst();
    }

    public AuthDetailsSnapshot verifyAuthCookie(HttpServletRequest request) {
        Cookie authCookie = getCookie(request, AUTH)
                .orElseThrow(() -> new IllegalArgumentException("Auth cookie is missing"));
        String value = authCookie.getValue();
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("Empty auth cookie");
        }
        String[] parts = value.split(DELIMITER);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Auth cookie is corrupted");
        }
        String userId = parts[0];
        if (!initDataHashService.verifyHash(userId, parts[1])) {
            throw new IllegalArgumentException("Auth cookie is compromised");
        }
        return AuthDetailsSnapshot.builder()
                .externalId(Long.parseLong(userId))
                .build();
    }
}
