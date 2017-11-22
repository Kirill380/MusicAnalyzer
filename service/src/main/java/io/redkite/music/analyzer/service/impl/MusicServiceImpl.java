package io.redkite.music.analyzer.service.impl;


import com.redkite.plantcare.common.dto.MusicFeaturesResponse;
import com.redkite.plantcare.common.dto.MusicProfileResponse;

import io.redkite.music.analyzer.MusicAnalyzerException;
import io.redkite.music.analyzer.client.AnalyticsRestClient;
import io.redkite.music.analyzer.common.Constants;
import io.redkite.music.analyzer.controller.MusicController;
import io.redkite.music.analyzer.convertors.MusicConverter;
import io.redkite.music.analyzer.model.MusicProfile;
import io.redkite.music.analyzer.model.User;
import io.redkite.music.analyzer.repository.MusicRepository;
import io.redkite.music.analyzer.repository.UserRepository;
import io.redkite.music.analyzer.security.UserContext;
import io.redkite.music.analyzer.service.MusicService;

import lombok.SneakyThrows;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

@Service
public class MusicServiceImpl implements MusicService{

  private final MusicRepository musicRepository;

  private final UserRepository userRepository;

  private final AnalyticsRestClient analyticsRestClient;


  private final MusicConverter musicConverter;

  @Autowired
  public MusicServiceImpl(MusicRepository musicRepository,
                          UserRepository userRepository,
                          AnalyticsRestClient analyticsRestClient,
                          MusicConverter musicConverter) {
    this.musicRepository = musicRepository;
    this.userRepository = userRepository;
    this.analyticsRestClient = analyticsRestClient;
    this.musicConverter = musicConverter;
  }

  @Override
  public List<MusicProfileResponse> getAllMusicProfiles(String title) {
    return null;
  }

  @Override
  @SneakyThrows
  @Transactional
  public MusicProfileResponse saveMusic(MultipartFile file) {
    if (file.getBytes() == null || file.getBytes().length == 0 ) {
      throw new MusicAnalyzerException("Uploaded file is empty", HttpStatus.BAD_REQUEST);
    }

    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    final User user = userRepository.getOne(currentUser.getUserId());
    MusicProfile newMusic = new MusicProfile();
    newMusic.setCreationDate(LocalDateTime.now());
    newMusic.setOwner(user);
    final MusicProfile savedMusic = musicRepository.save(newMusic);
    File music = new File(Constants.MUSIC_FILES_STORE + "/" + savedMusic.getId() + "/" + Constants.FILE_NAME);
    music.getParentFile().mkdirs();
    FileUtils.writeByteArrayToFile(music, file.getBytes());

    final MusicFeaturesResponse features = analyticsRestClient.calculateMusicFeatures(savedMusic.getId().toString());
    savedMusic.setTitle(features.getTitle());
    savedMusic.setAuthor(features.getAuthor());
    savedMusic.setAlbum(features.getAlbum());
    savedMusic.setYearRecorded(features.getYearRecorded());
    savedMusic.setGenre(features.getGenre());
    savedMusic.setPredictedGenres(features.getPredictedGenres());
    savedMusic.setTempo(features.getTempo());
    savedMusic.setChannels(features.getChannels());
    savedMusic.setDuration(features.getDuration());
    savedMusic.setSampleRate(features.getSampleRate());

    return musicConverter.toMusicResponse(savedMusic);
  }



}
