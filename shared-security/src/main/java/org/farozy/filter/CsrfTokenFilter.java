package org.farozy.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class CsrfTokenFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/auth/login", "POST");
    private final CsrfTokenRepository csrfTokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        if (requestMatcher.matches(request)) {
            CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

            if (csrfToken == null) {
                csrfToken = csrfTokenRepository.generateToken(request);
                csrfTokenRepository.saveToken(csrfToken, request, response);
            }

            String csrfTokenValue = csrfToken.getToken();

            Cookie csrfCookie = new Cookie("X-CSRF-TOKEN", csrfTokenValue);
            csrfCookie.setPath("/");
            csrfCookie.setHttpOnly(false);
            csrfCookie.setSecure(request.isSecure());
            response.addCookie(csrfCookie);
        }

        filterChain.doFilter(request, response);
    }

}
