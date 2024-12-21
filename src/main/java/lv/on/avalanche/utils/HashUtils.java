package lv.on.avalanche.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HashUtils {

    public static boolean verifyHash(String initData, String secretToken) {
        try {
            List<NameValuePair> params = URLEncodedUtils.parse(initData, StandardCharsets.UTF_8);
            List<NameValuePair> preparedData = new ArrayList<>(params.stream()
                    .filter(e -> !e.getName().equals("hash"))
                    .toList());
            preparedData.sort(Comparator.comparing(NameValuePair::getName));
            String dataCheckString = String.join("\n", preparedData.stream()
                    .map(e -> e.getName() + "=" + e.getValue())
                    .toList());

            byte[] secretKey = new HmacUtils("HmacSHA256", "WebAppData").hmac(secretToken);
            String initDataHash = new HmacUtils("HmacSHA256", secretKey).hmacHex(dataCheckString);
            String presentedHash = params.stream()
                    .filter(e -> e.getName().equals("hash"))
                    .findFirst()
                    .map(NameValuePair::getValue)
                    .orElse("");

            return initDataHash.equals(presentedHash);

        } catch (Exception e) {
            return false;
        }
    }

    public static User extractUserId(String initData) {
        try {
            List<NameValuePair> params = URLEncodedUtils.parse(initData, StandardCharsets.UTF_8);
            String userJson = params.stream()
                    .filter(param -> "user".equals(param.getName()))
                    .map(param -> param.getValue())
                    .findFirst()
                    .orElse("");

            if (!userJson.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                return objectMapper.readValue(userJson, User.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Data
    public static class User {
        private Long id;
        private String first_name;
        private String username;
        private String last_name;
        private String language_code;
        private boolean allows_write_to_pm;
        private String photo_url;
    }
}
