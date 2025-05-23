package by.akonashchenko.telegram.webapp.auth.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static by.akonashchenko.telegram.webapp.auth.util.Constant.HMAC_SHA256;
import static by.akonashchenko.telegram.webapp.auth.util.Constant.SHA256;
import static org.apache.commons.lang3.StringUtils.getBytes;
import static org.apache.tomcat.util.buf.HexUtils.toHexString;

@Service
@Slf4j
public class HashingService {

    private static final MessageDigest SHA256_DIGEST;
    private static final Mac SHA256_MAC;

    static {
        try {
            SHA256_DIGEST = MessageDigest.getInstance(SHA256);
            SHA256_MAC = Mac.getInstance(HMAC_SHA256);
        } catch (NoSuchAlgorithmException e) {
            throw new BeanInitializationException(e.getMessage());
        }
    }

    public byte[] calculateSha256Key(String value) {
        return SHA256_DIGEST.digest(value.getBytes(StandardCharsets.UTF_8));
    }

    public String getSha256Hash(byte[] key, String input) {
        try {
            SHA256_MAC.init(new SecretKeySpec(key, HMAC_SHA256));
        } catch (InvalidKeyException e) {
            log.error("Error while calculating hash with");
            throw new IllegalArgumentException(e);
        }
        byte[] result = SHA256_MAC.doFinal(getBytes(input, StandardCharsets.UTF_8));
        return toHexString(result);
    }
}
