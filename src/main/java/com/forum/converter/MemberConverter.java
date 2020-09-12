package com.forum.converter;

import com.forum.dao.entity.Member;
import com.forum.dto.MemberDto;

public class MemberConverter extends Converter<Member, MemberDto> {
    public MemberConverter() {
        super(MemberConverter::convertToEntity, MemberConverter::convertToDto);
    }

    private static final Member convertToEntity(MemberDto memberDto){
        Member member = new Member();
        member.setEmail(memberDto.getEmail());
        member.setPassword(memberDto.getPassword());
        member.setUserName(memberDto.getUserName());
        return member;
    }

    private static final MemberDto convertToDto(Member member){
        MemberDto membersDto = new MemberDto();
        membersDto.setEmail(member.getEmail());
        membersDto.setPassword(member.getPassword());
        membersDto.setUserName(member.getUserName());
        return membersDto;
    }

}