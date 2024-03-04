package com.jcanseco.inventoryapi.security.filters;

import com.jcanseco.inventoryapi.security.services.JwtService;
import com.jcanseco.inventoryapi.security.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter implements ApplicationContextAware, InitializingBean {

    private JwtService jwtService;
    private UserService userService;
    private ApplicationContext context;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) {
        try {

            String jwt = parseJwt(request);

            if (jwt != null) {
                setAuthentication(request, jwt);
            }

            filterChain.doFilter(request, response);
        }
        catch (SignatureException e) {
            logger.error(String.format("Invalid JWT signature: {%s}", e.getMessage()));
        }
        catch (MalformedJwtException e) {
            logger.error(String.format("Invalid JWT token: {%s}", e.getMessage()));
        }
        catch (ExpiredJwtException e) {
            logger.error(String.format("JWT token is expired: {%s}", e.getMessage()));
        }
        catch (UnsupportedJwtException e) {
            logger.error(String.format("JWT token is unsupported: {%s}", e.getMessage()));
        }
        catch (IllegalArgumentException e) {
            logger.error(String.format("JWT claims string is empty: {%s}", e.getMessage()));
        }
        catch (Exception e) {
            logger.error(String.format("JWT Unsuspected exception: {%s}", e.getMessage()));
        }
    }


    private void setAuthentication(@NonNull HttpServletRequest request,
                                   @NonNull String jwt) {

        String userEmail = jwtService.extractUserName(jwt);

        if (StringUtils.hasText(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.userDetailsService()
                    .loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
    }

    @Nullable
    private String parseJwt(@NonNull HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (!StringUtils.hasText(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        jwtService = context.getBean(JwtService.class);
        userService = context.getBean(UserService.class);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
