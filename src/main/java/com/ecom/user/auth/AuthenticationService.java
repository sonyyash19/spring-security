package com.ecom.user.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.user.config.JwtService;
import com.ecom.user.dto.UserDto;
import com.ecom.user.entities.User;
import com.ecom.user.repository.UserRepository;
import com.ecom.user.service.IUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IUserService userService;
    
    public RegisterResponse register(RegisterRequest request) {
        log.info("Request received with in auth service with " + request);
        var user = User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .build();

        // log.info("In auth service request build with " + user);
        UserDto userDto = userService.saveUser(user);
        var jwtToken = jwtService.generateToken(user);
        return RegisterResponse.builder()
            .user(userDto)
            .token(jwtToken)
            .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
            
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();

    }
    
}
