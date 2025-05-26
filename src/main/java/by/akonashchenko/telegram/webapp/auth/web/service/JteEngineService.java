package by.akonashchenko.telegram.webapp.auth.web.service;

import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class JteEngineService {

    private final TemplateEngine templateEngine;

    public String render(
            String template,
            Object model
    ) {
        var output = new StringOutput();
        templateEngine.render(
                template,
                model,
                output
        );
        return output.toString();
    }
}
