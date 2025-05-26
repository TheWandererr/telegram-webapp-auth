package by.akonashchenko.telegram.webapp.auth.web.jte.converter;

import by.akonashchenko.telegram.webapp.auth.domain.model.security.TelegramUserDetails;
import by.akonashchenko.telegram.webapp.auth.web.jte.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    @Mapping(target = "id", source = "externalId")
    User map(TelegramUserDetails source);
}
