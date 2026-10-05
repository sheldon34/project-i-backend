package com.example.securityskilltesting.security;

import com.example.securityskilltesting.Service.UserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class JWTAuthentificationFilter extends OncePerRequestFilter {

    private final JWTGenerator tokenGenerator;
    private final UserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = getJWTFromRequest(request);
            if (StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {
                Claims claims = Jwts.parser()
                        .setSigningKey(SecurityConstants.JWT_SECRET)
                        .parseClaimsJws(token)
                        .getBody();
                String username = claims.getSubject();
                UserDetails userDetails = userDetailService.loadUserByUsername(username);

                Set<GrantedAuthority> authorities = new HashSet<>();

                List<?> tokenRoles = claims.get("roles", List.class);
                if (tokenRoles != null) {
                    for (Object roleObj : tokenRoles) {
                        if (roleObj != null) {
                            String role = roleObj.toString().trim();
                            if (!role.isEmpty()) {
                                authorities.add(new SimpleGrantedAuthority(role));
                                if (role.startsWith("ROLE_")) {
                                    authorities.add(new SimpleGrantedAuthority(role.substring(5)));
                                } else {
                                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                                }
                            }
                        }
                    }
                }

                if (userDetails != null && userDetails.getAuthorities() != null) {
                    for (GrantedAuthority ga : userDetails.getAuthorities()) {
                        String auth = ga.getAuthority();
                        if (StringUtils.hasText(auth)) {
                            authorities.add(new SimpleGrantedAuthority(auth));
                            if (auth.startsWith("ROLE_")) {
                                authorities.add(new SimpleGrantedAuthority(auth.substring(5)));
                            } else {
                                authorities.add(new SimpleGrantedAuthority("ROLE_" + auth));
                            }
                        }
                    }
                }

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            log.info("Jwt header: {}", request.getHeader("Authorization"));
            log.info("Jwt username: {}", SecurityContextHolder.getContext().getAuthentication());
        } catch (Exception e) {
            log.error("jwt filter error: {} - {}", e.getClass().getName(), e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
