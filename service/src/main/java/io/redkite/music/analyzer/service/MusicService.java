package io.redkite.music.analyzer.service;


import com.redkite.plantcare.common.dto.ItemList;
import com.redkite.plantcare.common.dto.MusicProfileResponse;

import io.redkite.music.analyzer.controller.filters.MusicFilter;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MusicService {

  ItemList<MusicProfileResponse> getAllMusicProfiles(MusicFilter musicFilter);

  MusicProfileResponse saveMusic(MultipartFile file);
}
