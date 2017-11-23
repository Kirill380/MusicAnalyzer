package io.redkite.music.analyzer.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

  public static final String MUSIC_FILES_STORE = "/var/lib/music-analyzer/music/store";

  public static final String FILE_NAME = "musicFile";

  public static final String ART_IMAGE = "artImage";

  public static final String TS_IMAGE = "tsImage";

  public static final Integer ITEMS_PER_PAGE = 3;

  public static  final String IMAGE_PREFIX = "data:image/jpg;base64,";

  public static  final String IMAGE_SUFFIX = ".jpg";

  public static  final String DEFAULT_IMAGE = "default_note.jpg";


  public static class Web {

    public static final String API = "/api";
    public static final String V1 = "/v1";
    public static final String API_V1 = API + V1;
  }
}
