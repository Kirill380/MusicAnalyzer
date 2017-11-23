package io.redkite.music.analyzer.convertors;

import com.redkite.plantcare.common.dto.MusicProfileResponse;

import io.redkite.music.analyzer.model.MusicProfile;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MusicConverter {

   @Mapping(source = "predictedGenres", target = "predictedGenres", qualifiedByName = "bytesToString")
   MusicProfileResponse toMusicResponse(MusicProfile musicProfile);

   @Named("bytesToString")
   default String bytesToString(byte[] bytes) {
      return new String(bytes);
   }
}
