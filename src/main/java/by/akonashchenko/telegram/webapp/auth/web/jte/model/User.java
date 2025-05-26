package by.akonashchenko.telegram.webapp.auth.web.jte.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private boolean premium;
}
