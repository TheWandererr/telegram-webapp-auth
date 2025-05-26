package by.akonashchenko.telegram.webapp.auth.web.filter;

import by.akonashchenko.telegram.webapp.auth.domain.model.security.TelegramUserDetails;
import by.akonashchenko.telegram.webapp.auth.domain.service.security.TelegramUserDetailsService;
import by.akonashchenko.telegram.webapp.auth.util.AuthenticationUtils;
import by.akonashchenko.telegram.webapp.auth.web.jte.model.Error;
import by.akonashchenko.telegram.webapp.auth.web.model.AuthDetailsSnapshot;
import by.akonashchenko.telegram.webapp.auth.web.service.CookieService;
import by.akonashchenko.telegram.webapp.auth.web.service.JteResponseWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static by.akonashchenko.telegram.webapp.auth.util.AuthenticationUtils.isAuthenticated;
import static by.akonashchenko.telegram.webapp.auth.util.Constant.UriComponents.LOGIN;

@Component
@RequiredArgsConstructor
@Slf4j
public class CookieAuthenticationFilter extends OncePerRequestFilter {

    private final CookieService cookieService;
    private final TelegramUserDetailsService telegramUserDetailsService;
    private final JteResponseWriter writer;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (isLoginAttempt(request) || isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            log.info("Auth cookie verification...");
            AuthDetailsSnapshot authDetailsSnapshot = cookieService.verifyAuthCookie(request);
            log.info("Auth cookie verified");
            TelegramUserDetails telegramUserDetails
                    = telegramUserDetailsService.loadUserByAuthSnapshot(authDetailsSnapshot);
            AuthenticationUtils.initAuthentication(telegramUserDetails);
            filterChain.doFilter(request, response);
        } catch (IllegalArgumentException argumentException) {
            log.error("Authentication verification failed with message: {}", argumentException.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            writeResponse("unauthorized.jte", response, argumentException);
            return;
        } catch (Exception exception) {
            log.error("Unexpected error while authentication verification: {}", exception.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeResponse("error.jte", response, exception);
            return;
        }
    }

    private boolean isLoginAttempt(HttpServletRequest request) {
        return request.getRequestURI()
                .endsWith(LOGIN);
    }

    private void writeResponse(
            String template,
            HttpServletResponse response,
            Exception exception
    ) throws IOException {
        writer.writeHtml(
                response,
                template,
                Error.builder().message(exception.getMessage()).build()
        );
    }
}
