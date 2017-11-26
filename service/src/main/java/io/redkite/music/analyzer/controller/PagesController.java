package io.redkite.music.analyzer.controller;

import static io.redkite.music.analyzer.common.Constants.Web.API_V1;

import io.redkite.music.analyzer.security.UserContext;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class PagesController {

  @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
  public String home() {
    return "cabinet";
  }

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String signIn() {
    return "sign_in";
  }


  @RequestMapping(value = "/registration", method = RequestMethod.GET)
  public String registration() {
    return "registration_page";
  }

  @RequestMapping(value = "/music/{id}", method = RequestMethod.GET)
  public String musicPage(@PathVariable String id, Model model) {
    model.addAttribute("musicId", id);
    return "music_page";
  }


  @RequestMapping(value = "/user_info", method = RequestMethod.GET)
  public String userProfile(Model model) {
    return "profile";
  }
}
