package com.backend.ecommerce.service;

import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.exception.UserNotFoundException;

public interface UserService {
    User findUserById(Long userId) throws UserNotFoundException;
    User findUserProfileByJwt(String jwt) throws UserNotFoundException;
}
