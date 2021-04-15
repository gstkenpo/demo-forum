package com.forum.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.forum.bo.UserBo;
import com.forum.converter.MemberConverter;
import com.forum.dao.MemberRepository;
import com.forum.dao.entity.Member;
import com.forum.dto.Authority;
import com.forum.dto.MemberDto;
import com.forum.exception.ApplicationException;
import com.forum.service.MemberService;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService, UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private MemberRepository memberRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void createMember(MemberDto membersDto, Boolean isAdmin) throws ApplicationException {
        Member member = memberRepository.findByEmail(membersDto.getEmail());
        if (member != null) {
            logger.error("This email {} has been registered", membersDto.getEmail());
            throw new ApplicationException("This email " + membersDto.getEmail() + " has been registered");
        }
        membersDto.setPassword(bCryptPasswordEncoder.encode(membersDto.getPassword()));
        MemberConverter memberConverter = new MemberConverter();
        member = memberConverter.convertFromDto(membersDto);
        Date today = new Date();
        member.setCreateDate(today);
        member.setIsAdmin(BooleanUtils.isTrue(isAdmin) ? "Y" : "N");
        member = memberRepository.save(member);
        if (member == null) throw new ApplicationException("This email " + membersDto.getEmail() + " has been registered");
    }

    @Override
    public void editMemberDetail(Long userId, String userName, String password) throws ApplicationException {
        Member member = memberRepository.findById(userId).orElse(null);
        if (member == null) {
            logger.error("User Id: {} has attempted to change the member details", userId);
            throw new ApplicationException("userId: "+ userId +" has attempted to change the member details");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        if (!StringUtils.equals(member.getUserName(), userName)) member.setUserName(userName);
        if (!bCryptPasswordEncoder.matches(password, member.getPassword())) {
            member.setPasswordUpdateDate(new Date());
            member.setPassword(encodedPassword);
        }
        memberRepository.save(member);
    }

    @Override
    public MemberDto getMemberDetail(Long userId, String userName) throws ApplicationException {
        if (userId != null) return this.getMemberDetail(userId);
        else {
            Member member = memberRepository.findByUserName(userName);
            if (member == null) {
                logger.error("userName: {} has attempted to retrieve user detials", userName);
                throw new ApplicationException("userName: "+ userName +" has attempted to retrieve user detials");
            }
            MemberDto memberDto = new MemberDto();
            memberDto.setUserName(member.getUserName());
            return memberDto;
        }
    }

    @Override
    public MemberDto getMemberDetail(Long userId) throws ApplicationException {
        Member member = memberRepository.findById(userId).orElse(null);
        if (member == null) {
            logger.error("User Id: {} has attempted to retrieve user detials", userId);
            throw new ApplicationException("userId: "+ userId +" has attempted to retrieve user detials");
        }
        MemberDto memberDto = new MemberDto();
        memberDto.setUserName(member.getUserName());
        return memberDto;
    }

    /**
     * For internal function only
     * return true if JWT is valid against creation date
     */
    @Override
    public Boolean validedJwtDate(Long userId, Date createDate) throws ApplicationException {
        if (userId < 0 || userId == null || createDate == null) return Boolean.FALSE;
        Member member = memberRepository.findById(userId).orElse(null);
        if (member == null) {
            throw new ApplicationException("No member is found by userId: " + userId, HttpStatus.UNAUTHORIZED);
        }
        Date passwordUpdateDate = member.getPasswordUpdateDate();
        if (passwordUpdateDate == null) return Boolean.TRUE;
        return  passwordUpdateDate.before(createDate);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (StringUtils.isNotEmpty(email)) {
            Member member = memberRepository.findByEmail(email);
            List<Authority> authorities = new ArrayList<Authority>();
            if ("Y".equalsIgnoreCase(member.getIsAdmin())) {
                authorities.add(new Authority(Authority.ADMIN));
            }
            if (member != null) {
                return new UserBo(member.getUserName(), member.getId(), member.getPasswordUpdateDate(), member.getPassword(), authorities);
            }
        }
        throw new UsernameNotFoundException(email);
    }

    @Override
    public void deleteMember(Long userId) throws ApplicationException {
        if (userId == null) throw new ApplicationException("user Id can't be null");
        Member member = memberRepository.findById(userId).orElse(null);
        if (member == null) throw new ApplicationException("user doesn't exist");
        memberRepository.delete(member);
    }
    
}