package com.forum.controller;

import javax.validation.Valid;

import com.forum.dto.MemberDto;
import com.forum.exception.ApplicationException;
import com.forum.service.MemberService;

import org.apache.commons.lang3.math.NumberUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(MemberRestController.URL)
@Api(value = "Member Rest Controller")
public class MemberRestController {
	public final static String URL = "/rest/member";
	private Logger logger = Logger.getLogger(this.getClass());
	@Autowired MemberService memberService;

	@ApiOperation(value = "Create New Member", notes = "All the fields are mandatory", consumes = "application/json", response = MemberDto.class)
	@PostMapping
	public ResponseEntity<String> createMember(@Valid @RequestBody MemberDto memberDto) throws ApplicationException{
		memberService.createMember(memberDto, Boolean.FALSE); //create normal user
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@ApiOperation(value = "Update Member information", notes = "Only user name and password is updatable", consumes = "application/json", response = MemberDto.class)
	@PutMapping
	public ResponseEntity<String> updateMember(@RequestBody MemberDto memberDto) throws ApplicationException{
		String userIdStr = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long userId = NumberUtils.isParsable(userIdStr) ? Long.parseLong(userIdStr) : -1L;
		if (userId < 0) {
			logger.error("Unexpected error in JWT token");
			return new ResponseEntity<String>("Unexpected error in JWT token", HttpStatus.BAD_REQUEST);
		}
		memberService.editMemberDetail(userId, memberDto.getUserName(), memberDto.getPassword());
		//TODO: need to fire a restful call to post micoservice to update user password change time
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@ApiOperation(value = "Get Member details", notes = "Only user name would retieved", consumes = "application/json", response = MemberDto.class)
	@GetMapping
	public ResponseEntity<MemberDto> getMemberDetails(@RequestHeader("userId") String userIdStr) throws ApplicationException{
		Long userId = NumberUtils.isParsable(userIdStr) ? Long.parseLong(userIdStr) : -1L;
		if (userId < 0) {
			logger.error("Unexpected error in JWT token");
			return new ResponseEntity<MemberDto>(new MemberDto(), HttpStatus.BAD_REQUEST);
		}
		MemberDto memberDto = memberService.getMemberDetail(userId);
		return new ResponseEntity<MemberDto>(memberDto, HttpStatus.OK);
	}

}