package com.ecom.user.dto;

import com.ecom.user.constants.Role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@Setter
public class UserDto {
    
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
