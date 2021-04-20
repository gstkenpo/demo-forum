package com.forum.controller;

import javax.validation.Valid;

import com.forum.dto.MemberDto;
import com.forum.exception.ApplicationException;
import com.forum.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// @PreAuthorize("hasAuthority('" + Authority.ADMIN + "')")
@RestController
@RequestMapping(AdminRestController.URL)
public class AdminRestController {
    public static final String URL = "/rest/admin";
    
    @Autowired private MemberService memberService;
    @RequestMapping(method = RequestMethod.GET, path="/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<String> deteleMember(@RequestParam("userId") @NonNull Long userId) throws ApplicationException{
        memberService.deleteMember(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<MemberDto> getMemberDetails(@RequestParam("userId") Long userId, @RequestParam("userName") String userName) throws ApplicationException{
        MemberDto memberDto = memberService.getMemberDetail(userId, userName);
        return ResponseEntity.ok(memberDto);
    }

    @PutMapping
    public ResponseEntity<String> updateMember(@RequestParam("userId") Long userId, @RequestBody MemberDto memberDto) throws ApplicationException{
        memberService.editMemberDetail(userId, memberDto.getUserName(), memberDto.getPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<String> createAdminMember(@RequestBody @Valid MemberDto memberDto) throws ApplicationException{
        memberService.createMember(memberDto, Boolean.TRUE);
        return ResponseEntity.ok().build();
    }

}