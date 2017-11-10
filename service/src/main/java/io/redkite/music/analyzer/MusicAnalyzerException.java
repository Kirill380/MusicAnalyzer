package io.redkite.music.analyzer;


import com.redkite.plantcare.common.dto.ErrorDto;

import lombok.Getter;

import org.springframework.http.HttpStatus;

/**
 * The type music analyzer exception.
 */
public class MusicAnalyzerException extends RuntimeException {

  @Getter
  private HttpStatus status;

  @Getter
  private ErrorDto error;

  /**
   * Instantiates a new Plant care exception.
   *
   * @param message the message
   * @param status  the status
   */
  public MusicAnalyzerException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  /**
   * Instantiates a plant care exception.
   *
   * @param error  the error
   * @param status the http status
   */
  public MusicAnalyzerException(ErrorDto error, HttpStatus status) {
    super(error.getMessage());
    this.status = status;
    this.error = error;
  }
}


