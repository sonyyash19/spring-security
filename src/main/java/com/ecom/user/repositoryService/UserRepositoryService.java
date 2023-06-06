package com.ecom.user.repositoryService;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ecom.user.config.JwtService;
import com.ecom.user.dto.UserDto;
import com.ecom.user.entities.User;
import com.ecom.user.exception.UserNotAuthenticatedException;
import com.ecom.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserRepositoryService implements IUserRepositoryService{

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final JwtService jwtService;

    @Override
    public UserDto saveUser(User user) {
        log.info("Request received to save the user " + user);
        User saveUser = userRepository.save(user);
        // log.info("User saved in the db " + user);
        return this.modelMapper.map(saveUser, UserDto.class);
    }

    @Override
    public UserDto getUser(String userId, String token) {
        UserDetails user = userRepository.findById(userId).orElseThrow();
        final String jwtToken = token.substring(7);
//        check if the token is of valid user user or not
        if(!jwtService.isTokenValid(jwtToken, user)){
            throw new UserNotAuthenticatedException("Not authorized to see this page.");
        }
        
        log.info("User fetched from the db based on id " + user);

        // Convert the User entity to the UserDto and return it
        // Used so that the actual entity doesnot gets passed around in the application and there by reducing the chance to alter 
        // the entity data
        return this.modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(User user, String token) {
        // check if token is of valid user or not
        UserDetails userDetails = userRepository.findById(user.getUserId()).orElseThrow();
        final String jwtToken = token.substring(7);
        if(!jwtService.isTokenValid(jwtToken, userDetails)){
            throw new UserNotAuthenticatedException("Email or password is wrong");
        }
        
        User updatedUser = userRepository.save(user);
        return this.modelMapper.map(updatedUser, UserDto.class);

    }

    
    
}
