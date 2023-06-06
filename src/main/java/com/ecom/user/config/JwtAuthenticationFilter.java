/* When you make a HTTP request first thing that intercepts your request is JwtAuthFilter
 * We are creating that auth filter here
 * You need to define it of type bean thats why @component is used and requiredargsconstructor to make constructor for any field if present
 */
package com.ecom.user.config;

import java.io.IOException;

// import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request, 
            @Nonnull HttpServletResponse response, 
            @Nonnull FilterChain filterChain)
            throws ServletException, IOException {

                // get the authorization header from the request
                final String authHeader = request.getHeader("Authorization");

                if(authHeader == null || !authHeader.startsWith("Bearer ")){
                    filterChain.doFilter(request, response);
                    return;
                }

                // extract the jwt token from the header
                final String jwt = authHeader.substring(7);
                final String userEmail = jwtService.extractUserName(jwt);

                // securitycontextholder checks if the user is already authenticated or not
                if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);  // get the user details from the database

                    // check if token is valid or not and update the securitycontextholder
                    if(jwtService.isTokenValid(jwt, userDetails)){
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userEmail,
                            null,
                            userDetails.getAuthorities());

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }

                filterChain.doFilter(request, response);
    }
    
}
