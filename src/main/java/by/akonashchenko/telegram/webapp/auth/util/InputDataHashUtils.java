package by.akonashchenko.telegram.webapp.auth.util;

import by.akonashchenko.telegram.webapp.auth.domain.model.InitData;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.TreeMap;

import static by.akonashchenko.telegram.webapp.auth.util.Constant.InitData.HASH_KEY;

@UtilityClass
public class InputDataHashUtils {

    public static String createCheckString(InitData source) {
        Map<String, String> data = new TreeMap<>(source.getRaw());
        data.remove(HASH_KEY);
        if (data.isEmpty()) {
            throw new IllegalArgumentException("InitData is required");
        }
        StringBuilder output = new StringBuilder();
        data.forEach((key, value) -> {
            output.append(key);
            output.append("=");
            output.append(value);
            output.append("\n");
        });
        output.setLength(output.length() - 1);
        return output.toString();
    }
}
