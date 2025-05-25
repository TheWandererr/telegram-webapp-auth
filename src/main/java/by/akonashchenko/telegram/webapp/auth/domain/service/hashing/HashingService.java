package by.akonashchenko.telegram.webapp.auth.domain.service.hashing;

public interface HashingService {

    byte[] getKey();

    String getHash(String input);

    boolean matches(String verifiable, String hash);
}
