package io.redkite.music.analyzer.service.impl;


import static io.redkite.music.analyzer.common.Constants.ART_IMAGE;
import static io.redkite.music.analyzer.common.Constants.IMAGE_PREFIX;
import static io.redkite.music.analyzer.common.Constants.IMAGE_SUFFIX;
import static io.redkite.music.analyzer.common.Constants.ITEMS_PER_PAGE;
import static io.redkite.music.analyzer.common.Constants.MUSIC_FILES_STORE;
import static io.redkite.music.analyzer.common.Constants.TS_IMAGE;

import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.MusicFeaturesResponse;
import com.redkite.plantcare.common.dto.MusicProfileResponse;

import io.redkite.music.analyzer.MusicAnalyzerException;
import io.redkite.music.analyzer.client.AnalyticsRestClient;
import io.redkite.music.analyzer.common.Constants;
import io.redkite.music.analyzer.controller.filters.MusicFilter;
import io.redkite.music.analyzer.convertors.MusicConverter;
import io.redkite.music.analyzer.model.MusicProfile;
import io.redkite.music.analyzer.model.User;
import io.redkite.music.analyzer.repository.MusicRepository;
import io.redkite.music.analyzer.repository.UserRepository;
import io.redkite.music.analyzer.security.UserContext;
import io.redkite.music.analyzer.service.MusicService;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Slf4j
@Service
public class MusicServiceImpl implements MusicService {

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
  public ItemList<MusicProfileResponse> getAllMusicProfiles(MusicFilter musicFilter) {
    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    final int page = musicFilter.getPage() == null ? 0 : musicFilter.getPage() - 1;

    final Page<MusicProfile> musicProfiles = musicRepository.findMusicProfilesByFilter(musicFilter.getTitle(), currentUser.getUserId(),
            new PageRequest(page, ITEMS_PER_PAGE));

    final List<MusicProfileResponse> responses = musicProfiles.getContent().stream()
            .map(musicConverter::toMusicResponse)
            .peek(this::setImages)
            .collect(Collectors.toList());


    return new ItemList<>(responses, musicProfiles.getTotalElements());
  }

  @SneakyThrows(IOException.class)
  private void setImages(MusicProfileResponse musicProfileResponse) {
    File artWorkFile = new File(MUSIC_FILES_STORE + "/" + musicProfileResponse.getId() + "/" + ART_IMAGE + IMAGE_SUFFIX);
    final File tsImageFile = new File(MUSIC_FILES_STORE + "/" + musicProfileResponse.getId() + "/" + TS_IMAGE + IMAGE_SUFFIX);

    if (!artWorkFile.exists()) {
      artWorkFile = new File(MUSIC_FILES_STORE + "/" + Constants.DEFAULT_IMAGE);
    }

    final InputStream artWork = new FileInputStream(artWorkFile);
    musicProfileResponse.setImage(IMAGE_PREFIX + Base64.getEncoder().encodeToString(IOUtils.toByteArray(artWork)));

    if (tsImageFile.exists()) {
      final InputStream tsImage = new FileInputStream(tsImageFile);
      musicProfileResponse.setWaveformImage(IMAGE_PREFIX + Base64.getEncoder().encodeToString(IOUtils.toByteArray(tsImage)));
    }
  }


  @Override
  @SneakyThrows
  @Transactional
  public MusicProfileResponse saveMusic(MultipartFile file) {
    if (file.getBytes() == null || file.getBytes().length == 0) {
      throw new MusicAnalyzerException("Uploaded file is empty", HttpStatus.BAD_REQUEST);
    }

    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    final User user = userRepository.getOne(currentUser.getUserId());
    MusicProfile newMusic = new MusicProfile();
    newMusic.setCreationDate(LocalDateTime.now());
    newMusic.setOwner(user);
    final MusicProfile savedMusic = musicRepository.save(newMusic);
    File music = new File(MUSIC_FILES_STORE + "/" + savedMusic.getId() + "/" + Constants.FILE_NAME);
    music.getParentFile().mkdirs();
    FileUtils.writeByteArrayToFile(music, file.getBytes());

    final MusicFeaturesResponse features = analyticsRestClient.calculateMusicFeatures(savedMusic.getId().toString());

    String title = StringUtils.isNotBlank(features.getTitle()) ? features.getTitle() : getMusicName(file.getOriginalFilename());

    savedMusic.setTitle(title);
    savedMusic.setAuthor(features.getAuthor());
    savedMusic.setAlbum(features.getAlbum());
    savedMusic.setYearRecorded(features.getYearRecorded());
    savedMusic.setGenre(features.getGenre());
    savedMusic.setPredictedGenres(features.getPredictedGenres().getBytes());
    savedMusic.setTempo(features.getTempo());
    savedMusic.setChannels(features.getChannels());
    savedMusic.setDuration(features.getDuration());
    savedMusic.setSampleRate(features.getSampleRate());

    return musicConverter.toMusicResponse(savedMusic);
  }

  @Override
  @SneakyThrows(IOException.class)
  public void deleteMusic(Long musicId) {
    log.debug("Deleting music with id {}", musicId);
    UserContext currentUser = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    final Long userId = currentUser.getUserId();
    final User owner = userRepository.getOne(userId);

    MusicProfile musicProfile = musicRepository.getMusicProfileByUser(musicId, userId)
            .orElseThrow(() -> new MusicAnalyzerException("User with id [" + userId + "] does not have music with id [" + musicId + "]", HttpStatus.NOT_FOUND));

    final File dir = new File(MUSIC_FILES_STORE + "/" + musicId);
    if (dir.exists()) {
      FileUtils.deleteDirectory(dir);
    }

    musicProfile.setOwner(null);
    owner.getMusicProfiles().remove(musicProfile);
    musicRepository.delete(musicProfile);
  }


  private String getMusicName(String fileName) {
    return fileName.contains(".") ? fileName.split("\\.")[0] : fileName;
  }

}
