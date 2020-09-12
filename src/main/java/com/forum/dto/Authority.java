package com.forum.dto;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {
    private static final long serialVersionUID = 1L;
    public static final String ADMIN = "Admin";
    private String authority;

    public Authority(String authority){
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
    
}