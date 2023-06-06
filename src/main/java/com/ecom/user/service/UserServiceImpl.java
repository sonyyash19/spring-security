package com.ecom.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.user.dto.UserDto;
import com.ecom.user.entities.User;
import com.ecom.user.exception.UserNotAuthenticatedException;
import com.ecom.user.repositoryService.IUserRepositoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class UserServiceImpl implements IUserService{

    @Autowired
    private final IUserRepositoryService userRepositoryService;

    // saves the user in the repostiory
    @Override
    public UserDto saveUser(User user) {
        return userRepositoryService.saveUser(user);
    }

    // retrieve the user from the repository based in userId
    @Override
    public UserDto getUser(String userId, String token) {
        log.info("Request received at service.");
        UserDto userDto;

        try {
            userDto = userRepositoryService.getUser(userId, token);
        } catch (UserNotAuthenticatedException e) {
            throw new UserNotAuthenticatedException("Not authorized to see this page.");
        }
        return userDto;
    }

    // update the user in the repository
    @Override
    public UserDto updateUser(User user, String token) {
        log.info("Request received at service.");
        UserDto userDto;

        try {
            userDto = userRepositoryService.updateUser(user, token);
        } catch (UserNotAuthenticatedException e) {
            throw new UserNotAuthenticatedException("User id or password is wrong.");
        }
        return userDto;
    }
    
}
