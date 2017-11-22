package io.redkite.music.analyzer.convertors;

import com.redkite.plantcare.common.dto.MusicProfileResponse;

import io.redkite.music.analyzer.model.MusicProfile;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MusicConverter {

   MusicProfileResponse toMusicResponse(MusicProfile musicProfile);
}
