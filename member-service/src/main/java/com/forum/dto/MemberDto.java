package com.forum.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.codec.digest.DigestUtils;

public class MemberDto {
    @NotBlank(message = "user name can't be blank")
    @Size(max = 20, message = "user name length should be lesser than 20")
    private String userName;

    @NotBlank(message = "Email address can't be blank")
    @Email(message = "Email address format is wrong")
    @Size(max = 20, message = "Email length should be lesser than 20")
    private String email;

    @NotBlank(message = "Password can't be blank")
    @Size(max = 30, message = "Password length should be lesser than 30")
    private String password;

    public MemberDto() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
        return new String("userName: " + userName + "\n " + 
        "email: " + email + "\n " + 
        "password hash: " + DigestUtils.sha512Hex(password)
        );
    }
}