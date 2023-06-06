package com.ecom.user.repositoryService;

import com.ecom.user.dto.UserDto;
import com.ecom.user.entities.User;

public interface IUserRepositoryService {
    UserDto saveUser(User user);
    UserDto getUser(String userId, String toke);
    UserDto updateUser(User user, String token);
}
