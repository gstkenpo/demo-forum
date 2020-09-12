package com.forum.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="MEMBER")
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name="USER_NAME", nullable = false)
    private String userName;

    @Column(name="PASSWORD", nullable = false)
    private String password;

    @Column(name="EMAIL", nullable = false)
    private String email;

    @Column(name="CREATE_DATE", nullable = false)
    private Date createDate;

    @Column(name="PASSWORD_UPDATE_DATE")
    private Date passwordUpdateDate;

    @Column(name="IS_ADMIN", nullable = false)
    private String isAdmin;

    @Column(name="TOKEN_INVALID_DATE")
    private Date tokenInvalidDate;

    public Member() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getPasswordUpdateDate() {
        return passwordUpdateDate;
    }

    public void setPasswordUpdateDate(Date passwordUpdateDate) {
        this.passwordUpdateDate = passwordUpdateDate;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Date getTokenInvalidDate() {
        return tokenInvalidDate;
    }

    public void setTokenInvalidDate(Date tokenInvalidDate) {
        this.tokenInvalidDate = tokenInvalidDate;
    }
}