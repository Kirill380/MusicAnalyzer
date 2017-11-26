package io.redkite.music.analyzer.service;


import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.MusicProfileResponse;
import com.redkite.plantcare.common.dto.SignalSpectrogram;
import com.redkite.plantcare.common.dto.SignalTimeSeries;

import io.redkite.music.analyzer.controller.filters.MusicFilter;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface MusicService {

  ItemList<MusicProfileResponse> getAllMusicProfiles(MusicFilter musicFilter);

  MusicProfileResponse saveMusic(MultipartFile file);

  void deleteMusic(Long musicId);

  MusicProfileResponse getMusicProfile(Long id);

  InputStream getAudioFile(Long id);

  SignalTimeSeries getTimeSeries(Long musicId, Integer from, Integer to);

  SignalSpectrogram getSpectrogram(Long id, int from, int to);
}
