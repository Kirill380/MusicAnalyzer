package com.redkite.plantcare.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MusicProfileResponse {
  
  private Long id;

  private String title;

  private String author;

  private String album;

  private Integer duration; //in seconds

  private Integer tempo;

  private Integer sampleRate;

  private String genre;

  private String predictedGenres;

  private String yearRecorded;

  private Integer channels;

  private LocalDateTime creationDate;

  private String image;

  private String waveformImage;
}
