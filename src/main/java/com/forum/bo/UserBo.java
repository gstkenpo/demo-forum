package com.forum.bo;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserBo extends User {
    private static final long serialVersionUID = -9031044444934607767L;
    private Long userId;
    private Date passwordUpdateDate;
    
    public UserBo(String username, Long userId, Date passwordUpdateDate, String password,
			Collection<? extends GrantedAuthority> authorities) {
        super(username, password, true, true, true, true, authorities);
        this.userId = userId;
        this.passwordUpdateDate = passwordUpdateDate;
	}

    public Long getUserId() {
        return userId;
    }

    public Date getPasswordUpdateDate() {
        return passwordUpdateDate;
    }
}