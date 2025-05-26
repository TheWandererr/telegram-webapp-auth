package by.akonashchenko.telegram.webapp.auth.domain.model.security;

import by.akonashchenko.telegram.webapp.auth.config.model.AuthConfig;
import by.akonashchenko.telegram.webapp.auth.domain.persistence.model.AuthorityJpa;
import by.akonashchenko.telegram.webapp.auth.domain.persistence.model.UserJpa;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record TelegramUserDetails(
        UUID id,
        Long externalId,
        String username,
        String firstName,
        String lastName,
        boolean premium,
        boolean active,
        boolean banned,
        Instant expiredAt,
        List<SimpleGrantedAuthority> authenticationAuthorities
) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authenticationAuthorities;
    }

    @Override
    public String getPassword() {
        return StringUtils.EMPTY;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return Instant.now()
                .isBefore(expiredAt);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !banned;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return active && !isAccountNonLocked();
    }

    public static TelegramUserDetails init(
            UserJpa userJpa,
            long authDate,
            AuthConfig authConfig
    ) {
        return new TelegramUserDetails(
                userJpa.getId(),
                userJpa.getExternalId(),
                userJpa.getUsername(),
                userJpa.getFirstName(),
                userJpa.getLastName(),
                userJpa.isPremium(),
                userJpa.isActive(),
                userJpa.isBanned(),
                Instant.ofEpochSecond(authDate + authConfig.getValidityAmountSeconds()),
                userJpa.getRole().getAuthorities().stream()
                        .map(AuthorityJpa::getValue)
                        .map(SimpleGrantedAuthority::new)
                        .toList()
        );
    }
}
