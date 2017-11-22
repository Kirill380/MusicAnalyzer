package io.redkite.music.analyzer.client;

import com.redkite.plantcare.common.dto.MusicFeaturesResponse;

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

@Component
public class AnalyticsRestClient {

  @Autowired
  private RestTemplate restTemplate;

  @Value("${analyzer-service.base-url}")
  private String baseUrl;

  @Value("${analyzer-service.get-music-features}")
  private String getFeatures;


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
}
