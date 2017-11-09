package io.redkite.music.analyzer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class PagesController {

  @RequestMapping(value = {"/", "/home"})
  public String home(Map<String, Object> model) {
    return " ";
  }
}
