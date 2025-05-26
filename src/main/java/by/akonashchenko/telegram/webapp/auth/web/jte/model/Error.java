package by.akonashchenko.telegram.webapp.auth.web.jte.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Error {

    private String message;
}
