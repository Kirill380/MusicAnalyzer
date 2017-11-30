package io.redkite.music.analyzer.controller;

import static io.redkite.music.analyzer.common.Constants.Web.API_V1;

import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.MusicProfileResponse;
import com.redkite.plantcare.common.dto.SignalSpectrogram;
import com.redkite.plantcare.common.dto.SignalTimeSeries;

import io.redkite.music.analyzer.controller.filters.MusicFilter;
import io.redkite.music.analyzer.model.MusicProfile;
import io.redkite.music.analyzer.service.MusicService;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(API_V1)
public class MusicController {

  private final MusicService musicService;

  @Autowired
  public MusicController(MusicService musicService) {
    this.musicService = musicService;
  }

  @RequestMapping(value = "/user/musics", method = RequestMethod.POST)
  public ResponseEntity<String> saveMusic(@RequestParam("music") MultipartFile file, HttpServletRequest request) throws IOException {
    musicService.saveMusic(file);
    return ResponseEntity.ok("Audio file was uploaded");
  }

  @RequestMapping(value = "/user/musics", method = RequestMethod.GET)
  public ItemList<MusicProfileResponse> getMusicProfiles(MusicFilter musicFilter) {
    return musicService.getAllMusicProfiles(musicFilter);
  }

  @RequestMapping(value = "/user/musics/{id}/metadata", method = RequestMethod.GET)
  public MusicProfileResponse getMusicProfile(@PathVariable Long id) {
    return musicService.getMusicProfile(id);
  }

  @RequestMapping(value = "/user/musics/{id}/audio-file", method = RequestMethod.GET)
  public void getAudioFile(@PathVariable Long id, HttpServletResponse response) throws IOException {
    final InputStream audioFile = musicService.getAudioFile(id);

    IOUtils.copy(audioFile, response.getOutputStream());
    response.flushBuffer();
  }

  @RequestMapping(value = "/user/musics/{id}/time-series", method = RequestMethod.GET)
  public SignalTimeSeries getTimeSeries(@PathVariable Long id,
                                        @RequestParam int from,
                                        @RequestParam int to) {
    return musicService.getTimeSeries(id, from, to);
  }


  @RequestMapping(value = "/user/musics/{id}/spectrogram", method = RequestMethod.GET)
  public SignalSpectrogram getSpectrogram(@PathVariable Long id,
                                          @RequestParam int from,
                                          @RequestParam int to) {
    return musicService.getSpectrogram(id, from, to);
  }


  @ResponseStatus(HttpStatus.NO_CONTENT)
  @RequestMapping(value = "/user/musics/{id}", method = RequestMethod.DELETE)
  public void getMusicProfiles(@PathVariable Long id) {
     musicService.deleteMusic(id);
  }
}
