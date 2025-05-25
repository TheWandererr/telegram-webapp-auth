package by.akonashchenko.telegram.webapp.auth.domain.service;

public interface HashingService {

    byte[] getKey();

    String getHash(String input);

    boolean matches(String verifiable, String hash);
}
