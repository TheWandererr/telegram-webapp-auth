package by.akonashchenko.telegram.webapp.auth.config.security;

import by.akonashchenko.telegram.webapp.auth.web.jte.model.Error;
import by.akonashchenko.telegram.webapp.auth.web.service.JteResponseWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final JteResponseWriter writer;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {
        log.error("Unauthorized request: {}", exception.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("text/html");
        writeResponse(response, exception);
    }

    private void writeResponse(HttpServletResponse response, AuthenticationException exception) throws IOException {
        writer.writeHtml(
                response,
                "unauthorized.jte",
                Error.builder().message(exception.getMessage()).build()
        );
    }
}
