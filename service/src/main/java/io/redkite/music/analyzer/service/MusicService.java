package io.redkite.music.analyzer.service;


import com.redkite.plantcare.common.dto.MusicProfileResponse;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MusicService {

  List<MusicProfileResponse> getAllMusicProfiles(String title);

  MusicProfileResponse saveMusic(MultipartFile file);
}
