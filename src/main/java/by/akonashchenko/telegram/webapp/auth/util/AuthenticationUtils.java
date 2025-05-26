package by.akonashchenko.telegram.webapp.auth.util;

import by.akonashchenko.telegram.webapp.auth.domain.model.security.TelegramUserDetails;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;

@UtilityClass
public class AuthenticationUtils {

    public static Authentication initAuthentication(UserDetails userDetails) {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static boolean isAuthenticated() {
        return ofNullable(getAuthentication())
                .filter(not(AnonymousAuthenticationToken.class::isInstance))
                .map(Authentication::isAuthenticated)
                .orElse(false);
    }

    public static TelegramUserDetails getTelegramUserDetails() {
        return getTelegramUserDetails(getAuthentication());
    }

    public static TelegramUserDetails getTelegramUserDetails(Authentication authentication) {
        return getOptionalTelegramUserDetails(authentication)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Authentication was not provided"));
    }

    public static Optional<TelegramUserDetails> getOptionalTelegramUserDetails(Authentication authentication) {
        return ofNullable(authentication)
                .map(Authentication::getPrincipal)
                .filter(TelegramUserDetails.class::isInstance)
                .map(TelegramUserDetails.class::cast);
    }
}
