package by.akonashchenko.telegram.webapp.auth.web.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@Component
@RequiredArgsConstructor
public class JteResponseWriter {

    private static final MediaType HTML = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);

    private final JteEngineService jteEngineService;

    public void writeHtml(HttpServletResponse response, String template, Object model) throws IOException {
        response.setContentType(HTML.toString());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String content = jteEngineService.render(template, model);
        response.getWriter().write(content);
        response.getWriter().flush();
    }

    public ResponseEntity<String> writeHtml(String template, Object model) {
        return writeHtml(template, model, OK);
    }

    public ResponseEntity<String> writeHtml(String template, Object model, HttpStatus status) {
        return ResponseEntity.status(status)
                .contentType(HTML)
                .body(jteEngineService.render(template, model));
    }
}
