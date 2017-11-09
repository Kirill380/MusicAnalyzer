package io.redkite.music.analyzer.service;


import com.redkite.plantcare.common.dto.MusicProfileResponse;

import java.util.List;

public interface MusicService {

  List<MusicProfileResponse> getAllMusicProfiles(String title);

  MusicProfileResponse saveMusic(byte[] payload);
}
