package io.redkite.music.analyzer.service.impl;


import com.redkite.plantcare.common.dto.MusicProfileResponse;

import io.redkite.music.analyzer.common.Constants;
import io.redkite.music.analyzer.model.MusicProfile;
import io.redkite.music.analyzer.repository.MusicRepository;
import io.redkite.music.analyzer.repository.UserRepository;
import io.redkite.music.analyzer.service.MusicService;

import lombok.SneakyThrows;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
public class MusicServiceImpl implements MusicService{

  private final MusicRepository musicRepository;

  private final UserRepository userRepository;

  @Autowired
  public MusicServiceImpl(MusicRepository musicRepository,
                          UserRepository userRepository) {
    this.musicRepository = musicRepository;
    this.userRepository = userRepository;
  }

  @Override
  public List<MusicProfileResponse> getAllMusicProfiles(String title) {
    return null;
  }

  @Override
  @SneakyThrows
  public MusicProfileResponse saveMusic(MultipartFile file) {
    MusicProfile newMusic = new MusicProfile();
    newMusic.setOwner(userRepository.findAll().get(0));
    final MusicProfile savedMusic = musicRepository.save(newMusic);

    File music = new File(Constants.MUSIC_FILES_STORE + "/" + savedMusic.getId());
    FileUtils.writeByteArrayToFile(music, file.getBytes());
    return null;
  }



}
