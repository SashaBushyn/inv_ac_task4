package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/")
public class TestController {

  @GetMapping("/users")
  @PreAuthorize("hasRole('USER')")
  public String usersEndPoint() {
    return "USER is authorized";
  }

  @GetMapping("/admins")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminsEndPoint() {
    return "ADMIN is authorized";
  }
}
