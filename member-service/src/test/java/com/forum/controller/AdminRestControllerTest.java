package com.forum.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminRestController.class)
public class AdminRestControllerTest extends GeneralTestController{
    @Autowired private MockMvc mockMvc;

    @MockBean private MemberService memberService;

    @Test
    public void testTest_forbidden() throws Exception {
        mockMvc.perform(get(AdminRestController.URL + "/test")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(isAdmin = "Y")
    public void testTest_ok() throws Exception {
        mockMvc.perform(get(AdminRestController.URL + "/test")).andExpect(status().isOk());
    }

    @Test
    public void testDeteleMember_forbidden() throws Exception {
        mockMvc.perform(delete(AdminRestController.URL)
                        .param("userId", "2")
                        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(isAdmin = "Y")
    public void testDeteleMember_ok() throws Exception {
        mockMvc.perform(delete(AdminRestController.URL)
                        .param("userId", "2")
                        ).andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser(isAdmin = "Y")
    public void testDeteleMember_ApplicationException() throws Exception {
        Mockito.doThrow(new ApplicationException("Testing Exception")).when(memberService).deleteMember(Mockito.anyLong());
        mockMvc.perform(delete(AdminRestController.URL)
                        .param("userId", "2")
                        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockCustomUser(isAdmin = "Y")
    public void testDeteleMember_MissingParam() throws Exception {
        mockMvc.perform(delete(AdminRestController.URL)).andExpect(status().isBadRequest());
    }

    //----
    @Test
    public void testGetMemberDetails_forbidden() throws Exception {
        mockMvc.perform(get(AdminRestController.URL)
                        .param("userId", "2")
                        .param("userName", "")
                        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(isAdmin = "Y")
    public void testGetMemberDetails_ok() throws Exception {
        mockMvc.perform(get(AdminRestController.URL)
                        .param("userId", "2")
                        .param("userName", "")
                        ).andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser(isAdmin = "Y")
    public void testGetMemberDetails_ApplicationException() throws Exception {
        Mockito.doThrow(new ApplicationException("Testing Exception")).when(memberService).getMemberDetail(Mockito.anyLong(), Mockito.anyString());
        mockMvc.perform(get(AdminRestController.URL)
                        .param("userId", "2")
                        .param("userName", "")
                        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockCustomUser(isAdmin = "Y")
    public void testGetMemberDetails_MissingParam() throws Exception {
        mockMvc.perform(get(AdminRestController.URL)).andExpect(status().isBadRequest());
    }

    //----
    @Test
    public void testUpdateMember_forbidden() throws Exception {
        MemberDto memberDto = new MemberDto();
        memberDto.setUserName("userName");
        memberDto.setPassword("password");
        mockMvc.perform(put(AdminRestController.URL)
                        .param("userId", "2")
                        .content(asJsonString(memberDto))
                        ).andExpect(status().isForbidden());
    }
    @Test
    @WithMockCustomUser(isAdmin = "Y")
    public void testUpdateMember_ok() throws Exception {
        MemberDto memberDto = new MemberDto();
        memberDto.setUserName("userName");
        memberDto.setPassword("password");
        mockMvc.perform(put(AdminRestController.URL)
        .contentType(MediaType.APPLICATION_JSON)
                    .param("userId", "2")
                    .content(asJsonString(memberDto))
                    .accept(MediaType.APPLICATION_JSON)
                    ).andExpect(status().isOk());
    }
    
    
    @Test
    @WithMockCustomUser(isAdmin = "Y")
    public void testUpdateMember_ApplicationException() throws Exception {
        Mockito.doThrow(new ApplicationException("Testing Exception")).when(memberService).editMemberDetail(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
        MemberDto memberDto = new MemberDto();
        memberDto.setUserName("userName");
        memberDto.setPassword("password");
        mockMvc.perform(put(AdminRestController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId", "2")
                        .content(asJsonString(memberDto))
                        .accept(MediaType.APPLICATION_JSON)
                        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockCustomUser(isAdmin = "Y")
    public void testUpdateMember_MissingParam() throws Exception {
        mockMvc.perform(put(AdminRestController.URL)).andExpect(status().isBadRequest());
    }

    //----
    @Test
    public void testCreateAdminMember_forbidden() throws Exception {
        MemberDto memberDto = new MemberDto();
        memberDto.setUserName("userName");
        memberDto.setPassword("password");
        memberDto.setEmail("t@t.com");
        mockMvc.perform(post(AdminRestController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(memberDto))
                        ).andExpect(status().isForbidden());
    }
    @Test
    @WithMockCustomUser(isAdmin = "Y")
    public void testCreateAdminMember_ok() throws Exception {
        MemberDto memberDto = new MemberDto();
        memberDto.setUserName("userName");
        memberDto.setPassword("password");
        memberDto.setEmail("t@t.com");
        mockMvc.perform(post(AdminRestController.URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(memberDto))
                        .accept(MediaType.APPLICATION_JSON)
                        ).andExpect(status().isOk());
    }
    
    
    @Test
    @WithMockCustomUser(isAdmin = "Y")
    public void testCreateAdminMember_ApplicationException() throws Exception {
        Mockito.doThrow(new ApplicationException("Testing Exception")).when(memberService).createMember(Mockito.any(MemberDto.class), Mockito.anyBoolean());
        MemberDto memberDto = new MemberDto();
        memberDto.setUserName("userName");
        memberDto.setPassword("password");
        memberDto.setEmail("t@t.com");
        mockMvc.perform(post(AdminRestController.URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(memberDto))
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockCustomUser(isAdmin = "Y")
    public void testCreateAdminMember_MissingParam() throws Exception {
        mockMvc.perform(post(AdminRestController.URL)).andExpect(status().isBadRequest());
    }
}
