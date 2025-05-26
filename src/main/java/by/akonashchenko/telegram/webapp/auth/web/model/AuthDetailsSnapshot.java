package by.akonashchenko.telegram.webapp.auth.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthDetailsSnapshot {

    private Long externalId;
}
