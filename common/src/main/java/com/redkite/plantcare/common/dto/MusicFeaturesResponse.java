package com.redkite.plantcare.common.dto;

import lombok.Data;

@Data
public class MusicFeaturesResponse {

  private String title;

  private String author;

  private String album;

  private String genre;

  private String predictedGenres;

  private String yearRecorded;

  private Integer tempo;

  private Integer channels;

  private Integer duration;

  private Integer sampleRate;
}
