package by.akonashchenko.telegram.webapp.auth.config.security;

import by.akonashchenko.telegram.webapp.auth.web.filter.CookieAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static by.akonashchenko.telegram.webapp.auth.util.Constant.Authorities.WRITE;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            AccessDeniedHandler apiAccessDeniedHandler,
            AuthenticationEntryPoint telegramAuthenticationEntryPoint,
            CookieAuthenticationFilter cookieAuthenticationFilter
    ) throws Exception {
        http.authorizeHttpRequests(customizer ->
                customizer.requestMatchers(GET).permitAll()
                        .requestMatchers(POST, "/auth/**").permitAll()
                        .requestMatchers(POST).hasAnyAuthority(WRITE)
                        .requestMatchers(PUT).hasAnyAuthority(WRITE)
                        .requestMatchers(DELETE).hasAnyAuthority(WRITE)
        );

        http.sessionManagement(customizer -> customizer.sessionCreationPolicy(STATELESS));

        http.exceptionHandling(customizer ->
                customizer.accessDeniedHandler(apiAccessDeniedHandler)
                        .authenticationEntryPoint(telegramAuthenticationEntryPoint)
        );

        http.addFilterBefore(cookieAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.cors(withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
