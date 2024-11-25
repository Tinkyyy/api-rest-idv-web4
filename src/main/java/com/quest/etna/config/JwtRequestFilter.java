package com.quest.etna.config;

import com.quest.etna.model.JwtUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {


    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String headerRawValue = request.getHeader("Authorization");

        if (headerRawValue == null || !headerRawValue.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // We can get only char's after "Bearer "
        String jwtToken = headerRawValue.substring(7);

        try {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            JwtUserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);

            if (!jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                filterChain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (Exception exception) {
            filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
