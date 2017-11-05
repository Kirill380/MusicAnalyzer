package io.redkite.music.analyzer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class MusicController {
  @RequestMapping(value = "/music/upload", method = RequestMethod.POST)
  public ResponseEntity<String> uploadMusic(@RequestParam("music") MultipartFile file) throws IOException {
    File music = new File("temp/music-" + UUID.randomUUID() + ".wav");
    FileUtils.writeByteArrayToFile(music, file.getBytes());
    this.file = music;
    this.file = music;
    return ResponseEntity.ok("Audio file was uploaded");
  }
}
