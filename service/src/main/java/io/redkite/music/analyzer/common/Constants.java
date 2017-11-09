package io.redkite.music.analyzer.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

  public static final String MUSIC_FILES_STORE = "/var/lib/music-analyzer/music/store";

  public static class Web {

    public static final String API = "/api";
    public static final String V1 = "/v1";
    public static final String API_V1 = API + V1;
  }
}
