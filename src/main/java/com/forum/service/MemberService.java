package com.forum.service;

import java.util.Date;

import com.forum.dto.MemberDto;
import com.forum.exception.ApplicationException;

public interface MemberService {
    public void createMember(MemberDto membersDto, Boolean isAdmin) throws ApplicationException;
    public void editMemberDetail(Long userId, String userName, String password) throws ApplicationException;
    public Boolean validedJwtDate(Long userId, Date createDate) throws ApplicationException;
    public MemberDto getMemberDetail(Long userId) throws ApplicationException;
    public MemberDto getMemberDetail(Long userId, String userName) throws ApplicationException;
    public void deleteMember(Long userId) throws ApplicationException;
}