package io.redkite.music.analyzer.controller;

import static io.redkite.music.analyzer.common.Constants.Web.API_V1;

import org.springframework.stereotype.Controller;
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
}
