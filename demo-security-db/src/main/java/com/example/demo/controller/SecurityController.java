package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SecurityController {

  @GetMapping("private")
  public String privatePage() throws Exception {
    return "this is private page!";
  }
  @GetMapping("public")
  public String publicPage() throws Exception {
    return "this is public page!";
  }
}
