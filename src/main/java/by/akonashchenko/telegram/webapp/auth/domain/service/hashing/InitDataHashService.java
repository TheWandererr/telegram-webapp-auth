package by.akonashchenko.telegram.webapp.auth.domain.service.hashing;

import by.akonashchenko.telegram.webapp.auth.domain.service.security.TelegramHashDataHolder;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class InitDataHashService {

    // HMAC-SHA256 signature of the Telegram Bot token, using the WebAppData value as the cryptographic key.
    private final byte[] signature;

    public InitDataHashService(TelegramHashDataHolder telegramHashDataHolder) {
        this.signature = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, telegramHashDataHolder.getCryptographicKey())
                .hmac(telegramHashDataHolder.getToken());
    }

    public boolean verifyHash(String checkString, String expectedHash) {
        String actualHash = hmacHex(checkString);
        return StringUtils.equals(actualHash, expectedHash);
    }

    public String hmacHex(String source) {
        HmacUtils mac = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, signature);
        return mac.hmacHex(source);
    }
}
