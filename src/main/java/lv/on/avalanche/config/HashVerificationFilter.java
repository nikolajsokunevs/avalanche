package lv.on.avalanche.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class HashVerificationFilter extends OncePerRequestFilter {

    private final String secretToken;

    public HashVerificationFilter(@Value("${telegram.secret}") String secretToken) {
        this.secretToken = secretToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        if (requestUri.startsWith("/swagger-ui") || requestUri.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        String initData = request.getHeader("Authorization");
        if (StringUtils.hasText(initData) && verifyHash(initData)) {
            User user = extractUserId(initData);

            if (user != null) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: User ID not found.");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid hash.");
        }
    }

    private boolean verifyHash(String initData) {
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

    private User extractUserId(String initData) {
        try {
            List<NameValuePair> params = URLEncodedUtils.parse(initData, StandardCharsets.UTF_8);
            String userJson = params.stream()
                    .filter(param -> "user".equals(param.getName()))
                    .map(param -> param.getValue())
                    .findFirst()
                    .orElse("");

            if (!userJson.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
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
        private String last_name;
        private String language_code;
        private boolean allows_write_to_pm;
    }
}