package com.forum.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.forum.config.WithMockCustomUser;
import com.forum.service.MemberService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminRestController.class)
public class AdminRestControllerTest {
    @Autowired private MockMvc mockMvc;

    @MockBean private MemberService memberService;

    @Test
    public void testTest_forbidden() throws Exception {
        mockMvc.perform(get(AdminRestController.URL + "/test")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(userId = 1L, isAdmin = "Y")
    public void testTest_ok() throws Exception {
        mockMvc.perform(get(AdminRestController.URL + "/test")).andExpect(status().isOk());
    }
}
