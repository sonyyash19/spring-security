package com.ecom.user.service;

import com.ecom.user.dto.UserDto;
import com.ecom.user.entities.User;

public interface IUserService {
    
    public UserDto saveUser(User user);
    public UserDto getUser(String userId, String token);
    public UserDto updateUser(User user, String token);
}
