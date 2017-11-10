package io.redkite.music.analyzer.controller;

import com.redkite.plantcare.common.dto.ErrorDto;

import io.redkite.music.analyzer.MusicAnalyzerException;

import lombok.extern.slf4j.Slf4j;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

  /**
   * Handles exceptions and returns an appropriate HTTP code.
   */
  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ErrorDto> handleException(HttpServletRequest req, Throwable ex) {
    log.error(ex.getMessage(), ex);
    if (ex instanceof MusicAnalyzerException) {
      MusicAnalyzerException pcException = (MusicAnalyzerException) ex;
      ResponseEntity<ErrorDto> responce;
      if (pcException.getError() != null) {
        responce = new ResponseEntity<>(pcException.getError(), pcException.getStatus());
      } else {
        responce = new ResponseEntity<>(new ErrorDto(pcException.getMessage(), null), pcException.getStatus());
      }
      return responce;
    } else if (ex instanceof AccessDeniedException) {
      return new ResponseEntity<>(new ErrorDto(ex.getMessage(), null), HttpStatus.FORBIDDEN);
    } else {
      log.error(ex.getMessage(), ex);
      return new ResponseEntity<>(new ErrorDto(ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
