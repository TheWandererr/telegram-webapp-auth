package by.akonashchenko.telegram.webapp.auth.web.controller;

import by.akonashchenko.telegram.webapp.auth.util.AuthenticationUtils;
import by.akonashchenko.telegram.webapp.auth.web.jte.converter.UserConverter;
import by.akonashchenko.telegram.webapp.auth.web.jte.model.User;
import by.akonashchenko.telegram.webapp.auth.web.service.JteResponseWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final JteResponseWriter responseWriter;

    @SneakyThrows
    @PreAuthorize("hasAnyAuthority('WRITE')")
    @GetMapping("/")
    public ResponseEntity<String> renderHome() {
        User model = UserConverter.INSTANCE.map(AuthenticationUtils.getTelegramUserDetails());
        return responseWriter.writeHtml(
                "user.jte",
                model
        );
    }
}
