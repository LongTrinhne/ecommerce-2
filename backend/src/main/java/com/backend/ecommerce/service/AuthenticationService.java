package com.backend.ecommerce.service;

import com.backend.ecommerce.request.AuthenticationRequest;
import com.backend.ecommerce.response.AuthenticationResponse;
import com.backend.ecommerce.request.RegisterRequest;
import com.backend.ecommerce.entity.Role;
import com.backend.ecommerce.repository.UserRepository;
import com.backend.ecommerce.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.backend.ecommerce.entity.User;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    public AuthenticationResponse register(RegisterRequest request) {

        User tmpUser = userRepository.findUserByEmail(request.getEmail());

        if (tmpUser == null) {
            var user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse
                    .builder()
                    .jwtToken(jwtToken)
                    .build();
        }
        else throw new IllegalArgumentException("Email existed");
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        var user = userRepository.findUserByEmail(request.getEmail());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .jwtToken(jwtToken)
                .build();
    }
}
