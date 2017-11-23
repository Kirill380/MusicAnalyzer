package io.redkite.music.analyzer.controller.filters;

import static io.redkite.music.analyzer.common.Constants.ITEMS_PER_PAGE;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Getter
@Setter
public class MusicFilter {

  private String title;

  private Integer page;
}
