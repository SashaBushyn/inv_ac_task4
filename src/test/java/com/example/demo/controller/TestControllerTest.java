package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser(roles = "USER")
  public void testUserEndpointAuthorized() throws Exception {
    mockMvc.perform(get("/api/test/users"))
        .andExpect(status().isOk())
        .andExpect(content().string("USER is authorized"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testUserEndpointForbidden() throws Exception {
    mockMvc.perform(get("/api/test/users"))
        .andExpect(status().isInternalServerError())
        .andExpect(result -> result.equals("Access Denied"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testAdminEndpointAuthorized() throws Exception {
    mockMvc.perform(get("/api/test/admins"))
        .andExpect(status().isOk())
        .andExpect(content().string("ADMIN is authorized"));
  }

  @Test
  @WithMockUser(roles = "USER")
  public void testAdminEndpointForbidden() throws Exception {
    mockMvc.perform(get("/api/test/admins"))
        .andExpect(status().isInternalServerError())
        .andExpect(result -> result.equals("Access Denied"));
  }
}
