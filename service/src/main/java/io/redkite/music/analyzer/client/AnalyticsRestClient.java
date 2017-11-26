package io.redkite.music.analyzer.client;

import com.redkite.plantcare.common.dto.MusicFeaturesResponse;
import com.redkite.plantcare.common.dto.SignalSpectrogram;
import com.redkite.plantcare.common.dto.SignalTimeSeries;

import io.redkite.music.analyzer.MusicAnalyzerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Component
public class AnalyticsRestClient {

  @Autowired
  private RestTemplate restTemplate;

  @Value("${analyzer-service.base-url}")
  private String baseUrl;

  @Value("${analyzer-service.get-music-features}")
  private String getFeatures;


  @Value("${analyzer-service.get-time-series}")
  private String getTimeseries;


  @Value("${analyzer-service.get-spectrogram}")
  private String getSpectrogram;

  public MusicFeaturesResponse calculateMusicFeatures(String musicId) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
    final String path = baseUrl + getFeatures.replaceAll("\\{.*}", musicId);
    ResponseEntity<MusicFeaturesResponse> response = restTemplate.exchange(path, HttpMethod.POST, new HttpEntity<>(httpHeaders), MusicFeaturesResponse.class);
    if (!response.getStatusCode().equals(HttpStatus.OK)) {
      throw new MusicAnalyzerException(
              "Error happened during getting user. Status code: " + response.getStatusCode().getReasonPhrase(), HttpStatus.BAD_REQUEST);
    }

    return response.getBody();
  }


  public SignalTimeSeries getTimeSeries(Long musicId, Integer from, Integer to) {
    final String path = baseUrl + getTimeseries.replaceAll("\\{.*}", musicId.toString());
    UriComponentsBuilder builder = UriComponentsBuilder
            .fromUriString(path)
            .queryParam("from", from)
            .queryParam("to", to);

    ResponseEntity<SignalTimeSeries> response = restTemplate.getForEntity(builder.toUriString(), SignalTimeSeries.class);
    if (!response.getStatusCode().equals(HttpStatus.OK)) {
      throw new MusicAnalyzerException(
              "Error happened during getting signal time series. Status code: " + response.getStatusCode().getReasonPhrase(), HttpStatus.BAD_REQUEST);
    }
    return response.getBody();
  }

  public SignalSpectrogram getSpectrogram(Long musicId, int from, int to) {
    final String path = baseUrl + getSpectrogram.replaceAll("\\{.*}", musicId.toString());
    UriComponentsBuilder builder = UriComponentsBuilder
            .fromUriString(path)
            .queryParam("from", from)
            .queryParam("to", to);

    ResponseEntity<SignalSpectrogram> response = restTemplate.getForEntity(builder.toUriString(), SignalSpectrogram.class);
    if (!response.getStatusCode().equals(HttpStatus.OK)) {
      throw new MusicAnalyzerException(
              "Error happened during getting signal spectrogram. Status code: " + response.getStatusCode().getReasonPhrase(), HttpStatus.BAD_REQUEST);
    }
    return response.getBody();
  }
}
