package com.forum.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.forum.config.WithMockCustomUser;
import com.forum.dto.MemberDto;
import com.forum.exception.ApplicationException;
import com.forum.service.MemberService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberRestController.class)
public class MemberRestControllerTest extends GeneralTestController{
    @Autowired private MockMvc mockMvc;

    @MockBean private MemberService memberService;

    @Test
    public void testCreateMemberApi_EmptyRequest() throws Exception{
        mockMvc.perform(post(MemberRestController.URL)).andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateMemberApi_NormalRequest() throws Exception{
        MemberDto memberDto = new MemberDto();
        memberDto.setEmail("Test@t.com");
        memberDto.setUserName("TestingUser");
        memberDto.setPassword("Password");
        //Mockito.when(memberService.createMember(Mockito.any(MemberDto.class), Mockito.anyBoolean())).thenReturn(memberDto);
        mockMvc.perform(post(MemberRestController.URL)
                        .contentType(MediaType.APPLICATION_JSON)                            
                        .content(asJsonString(memberDto))
                        .accept(MediaType.APPLICATION_JSON)
                        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateMemberApi_OversizeRequest() throws Exception{
        MemberDto memberDto = new MemberDto();
        memberDto.setEmail("Test@tttttttttttt.com");
        memberDto.setUserName("TestingUsersssssssssssssssss");
        memberDto.setPassword("Password~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        mockMvc.perform(post(MemberRestController.URL)
                        .contentType(MediaType.APPLICATION_JSON)                            
                        .content(asJsonString(memberDto))
                        .accept(MediaType.APPLICATION_JSON)
                        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[*].field", containsInAnyOrder("email", "userName", "password")))
                .andExpect(jsonPath("$.[*].errorMsg", containsInAnyOrder("Email length should be lesser than 20", 
                "user name length should be lesser than 20", 
                "Password length should be lesser than 30")));
    }

    @Test
    public void testCreateMemberApi_EmptyObjectRequest() throws Exception{
        MemberDto memberDto = new MemberDto();
        mockMvc.perform(post(MemberRestController.URL)
                        .contentType(MediaType.APPLICATION_JSON)                            
                        .content(asJsonString(memberDto))
                        .accept(MediaType.APPLICATION_JSON)
                        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[*].field", containsInAnyOrder("email", "userName", "password")))
                .andExpect(jsonPath("$.[*].errorMsg", containsInAnyOrder("Email address can't be blank", 
                "user name can't be blank", 
                "Password can't be blank")));
    }

    @Test
    @WithAnonymousUser
    public void testupdateMember_ForbiddenRequest() throws Exception {
        MemberDto memberDto = new MemberDto();
        memberDto.setUserName("TestingUser");
        memberDto.setPassword("Password");
        mockMvc.perform(put(MemberRestController.URL)
                        .contentType(MediaType.APPLICATION_JSON)                            
                        .content(asJsonString(memberDto))
                        .accept(MediaType.APPLICATION_JSON)
                        )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(userId = 1L)
    public void testupdateMember_OkRequest() throws Exception {
        MemberDto memberDto = new MemberDto();
        memberDto.setUserName("TestingUser");
        memberDto.setPassword("Password");
        mockMvc.perform(put(MemberRestController.URL)
                        .contentType(MediaType.APPLICATION_JSON)                            
                        .content(asJsonString(memberDto))
                        .accept(MediaType.APPLICATION_JSON)
                        )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser(userId = 1L)
    public void testupdateMember_BadRequest() throws Exception {
        final String errorMsg = "Testing exception";
        MemberDto memberDto = new MemberDto();
        memberDto.setUserName("TestingUser");
        memberDto.setPassword("Password");
        Mockito.doThrow(new ApplicationException(errorMsg)).when(memberService).editMemberDetail(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        mockMvc.perform(put(MemberRestController.URL)
                        .contentType(MediaType.APPLICATION_JSON)                            
                        .content(asJsonString(memberDto))
                        .accept(MediaType.APPLICATION_JSON)
                        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMsg));
    }
}
//credit: https://www.petrikainulainen.net/programming/spring-framework/integration-testing-of-spring-mvc-applications-write-clean-assertions-with-jsonpath/