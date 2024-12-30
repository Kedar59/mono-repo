package com.apiGateway.filter;

import com.apiGateway.services.AuthorizationService;
import com.apiGateway.services.JwtService;
import com.apiGateway.services.ProfileService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

//    @Autowired
//    private JwtService jwtService;
//
//    @Autowired
//    private ProfileService profileService;

    private final JwtService jwtService;
    private final AuthorizationService authService;
    private final ProfileService profileService;

    public JwtAuthFilter(JwtService jwtService, AuthorizationService authService, ProfileService profileService) {
        this.jwtService = jwtService;
        this.profileService = profileService;
        this.authService = authService;
    }

    private final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Retrieve the Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Check if the header starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Extract token
            username = jwtService.extractUsername(token); // Extract username from token
        }

        // If the token is valid and no authentication is set in the context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = profileService.loadUserByUsername(username);

            // Validate token and set authentication
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                String path = request.getRequestURI();

                // Extract user and company IDs from URL pattern
                Pattern pattern = Pattern.compile("/\\w+/\\w+/profile/(\\w+)/company/(\\w+)/.*");
                Matcher matcher = pattern.matcher(path);
                logger.info("in authorization filter for path : " + path);
                if (matcher.find()) {
                    logger.info("match found");
                    String profileId = matcher.group(1);
                    String companyId = matcher.group(2);
                    String action = path.substring(path.lastIndexOf('/') + 1);

                    logger.info("profileId : " + profileId);
                    logger.info("companyId : " + companyId);
                    logger.info("action : " + action);
                    // Check authorization based on action
                    boolean isAuthorized = false;

                    if (action.equals("update")) {

                        isAuthorized = authService.isModerator(profileId, companyId);
                    }
                    if (!isAuthorized) {
                        isAuthorized = switch (action) {
                            case "delete", "addCompanyBot", "update", "memberManagementPage", "promote", "demote" ->
                                    authService.isAdmin(profileId, companyId);
                            default -> false;
                        };
                    }
                    logger.info("isAuthorised : " + isAuthorized);

                    if (!isAuthorized) {
                        logger.info("not authorized");
                        response.sendError(HttpStatus.FORBIDDEN.value(), "Insufficient permissions");
                        return;
                    }
                } else logger.info("match not found - Allowing request");
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

}
