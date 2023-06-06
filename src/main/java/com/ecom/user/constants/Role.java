package com.ecom.user.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public enum Role {
    
    ADMIN("0"), USER("1");

    private String value;
}
