package lv.on.avalanche.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import static lv.on.avalanche.utils.HashUtils.extractUserId;
import static lv.on.avalanche.utils.HashUtils.verifyHash;
import static lv.on.avalanche.utils.HashUtils.User;

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
        if (requestUri.startsWith("/ws/")) {
            filterChain.doFilter(request, response);
            return;
        }
        String initData = request.getHeader("Authorization");
        if (StringUtils.hasText(initData) && verifyHash(initData, secretToken)) {
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
}