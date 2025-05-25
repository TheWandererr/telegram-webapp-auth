package by.akonashchenko.telegram.webapp.auth.domain.model.security;

import by.akonashchenko.telegram.webapp.auth.domain.persistence.model.AuthorityJpa;
import by.akonashchenko.telegram.webapp.auth.domain.persistence.model.UserJpa;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class TelegramUserDetails implements UserDetails {

    private final String username;
    private final boolean active;
    private final boolean banned;
    private final long authDate;
    private final List<SimpleGrantedAuthority> authenticationAuthorities;

    private TelegramUserDetails(
            String username,
            boolean active,
            boolean banned,
            long authDate,
            List<SimpleGrantedAuthority> authenticationAuthorities
    ) {
        this.username = username;
        this.active = active;
        this.banned = banned;
        this.authDate = authDate;
        this.authenticationAuthorities = authenticationAuthorities;
    }

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
        return active;
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

    public static TelegramUserDetails of(UserJpa userJpa, long authDate) {
        return new TelegramUserDetails(
                userJpa.getUsername(),
                userJpa.isActive(),
                userJpa.isBanned(),
                authDate,
                userJpa.getRole().getAuthorities().stream()
                        .map(AuthorityJpa::getValue)
                        .map(SimpleGrantedAuthority::new)
                        .toList());
    }
}
