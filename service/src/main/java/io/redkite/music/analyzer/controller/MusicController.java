package io.redkite.music.analyzer.controller;

import static io.redkite.music.analyzer.common.Constants.Web.API_V1;

import io.redkite.music.analyzer.service.MusicService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = API_V1)
public class MusicController {

  private final MusicService musicService;

  @Autowired
  public MusicController(MusicService musicService) {
    this.musicService = musicService;
  }

  @RequestMapping(path = "/user/music", method = RequestMethod.POST)
  public ResponseEntity<String> saveMusic(@RequestParam("music") MultipartFile file) throws IOException {

    return ResponseEntity.ok("Audio file was uploaded");
  }
}
