package com.backend.ecommerce.service.implementation;

import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.exception.UserNotFoundException;
import com.backend.ecommerce.repository.UserRepository;
import com.backend.ecommerce.security.JwtService;
import com.backend.ecommerce.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public User findUserById(Long userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent())
            return user.get();
        throw new UserNotFoundException("User with id: " + userId + " not exist!");
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserNotFoundException {
        String email = jwtService.extractEmail(jwt);

        User user = userRepository.findUserByEmail(email);

        if (user == null)
            throw new UserNotFoundException("user with email: " + email + " not exist!");
        return user;
    }
}
