package by.akonashchenko.telegram.webapp.auth.domain.service.hashing;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacUtils;

public class HmacHashingService implements HashingService {

    private final byte[] key;
    private final HmacUtils mac;

    public HmacHashingService(String algorithm, String secretInput) {
        this.key = DigestUtils.sha256(secretInput);
        this.mac = new HmacUtils(algorithm, key);
    }

    @Override
    public byte[] getKey() {
        return key;
    }

    @Override
    public String getHash(String input) {
        return mac.hmacHex(input);
    }

    @Override
    public boolean matches(String verifiable, String hash) {
        String actualHash = getHash(verifiable);
        return actualHash.equals(hash);
    }
}
