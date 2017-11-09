package io.redkite.music.analyzer.service.impl;


import com.redkite.plantcare.common.dto.MusicProfileResponse;

import io.redkite.music.analyzer.repository.MusicRepository;
import io.redkite.music.analyzer.service.MusicService;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class MusicServiceImpl implements MusicService{

  private final MusicRepository musicRepository;

  @Autowired
  public MusicServiceImpl(MusicRepository musicRepository) {
    this.musicRepository = musicRepository;
  }

  @Override
  public List<MusicProfileResponse> getAllMusicProfiles(String title) {
    return null;
  }

  @Override
  public MusicProfileResponse saveMusic(byte[] payload) {
    File music = new File("temp/music-" + UUID.randomUUID() + ".wav");
    FileUtils.writeByteArrayToFile(music, file.getBytes());
    return null;
  }
}
