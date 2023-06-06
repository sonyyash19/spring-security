package com.ecom.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.user.dto.UserDto;
import com.ecom.user.entities.User;
import com.ecom.user.exception.UserNotAuthenticatedException;
import com.ecom.user.service.IUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Log4j2
public class UserController {

    private final IUserService userService;

    @GetMapping
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("Hello from secured end point.");
    }

    @GetMapping("/user_info/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId, @RequestHeader("Authorization") String token){
        log.info("Request received at controller with token " + token.substring(7));
        UserDto userDto;
        try {
            userDto = userService.getUser(userId, token);
        } catch (UserNotAuthenticatedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        return ResponseEntity.ok(userDto);

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user, @RequestHeader("Authorization") String token){
        log.info("Request received at controller with token " + token.substring(7));
        UserDto userDto;
        try {
            userDto = userService.updateUser(user, token);
        } catch (UserNotAuthenticatedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        return ResponseEntity.ok(userDto);
    }
    
}
