package com.backend.ecommerce.service;

import com.backend.ecommerce.entity.Cart;
import com.backend.ecommerce.request.AuthenticationRequest;
import com.backend.ecommerce.response.AuthenticationResponse;
import com.backend.ecommerce.request.RegisterRequest;
import com.backend.ecommerce.entity.Role;
import com.backend.ecommerce.repository.UserRepository;
import com.backend.ecommerce.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.backend.ecommerce.entity.User;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final CartService cartService;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 CartService cartService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.cartService = cartService;
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
            Cart cart = cartService.createCart(user);

            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), request.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtService.generateToken(authentication);
            return AuthenticationResponse
                    .builder()
                    .jwtToken(token)
                    .message("Sign Up Successfully!")
                    .build();
        }
        else throw new IllegalArgumentException("Email existed");
    }
    public AuthenticationResponse logIn(AuthenticationRequest request) {

        Authentication authentication = authenticate(request.getEmail(), request.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtService.generateToken(authentication);

        return AuthenticationResponse
                .builder()
                .jwtToken(token)
                .message("Sign In Successfully")
                .build();
    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = userRepository.findUserByEmail(email);
        if (userDetails == null)
            throw new BadCredentialsException("Invalid Email!!!");
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Password!!!");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
