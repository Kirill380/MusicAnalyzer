package io.redkite.music.analyzer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class UserController {

  @RequestMapping(value = {"/", "/home"})
  public String home(Map<String, Object> model) {
    return "home";
  }
}
