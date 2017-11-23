package io.redkite.music.analyzer.repository;


import io.redkite.music.analyzer.model.MusicProfile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MusicRepository  extends JpaRepository<MusicProfile, Long> {

  @Query(value = "SELECT m FROM MusicProfile m JOIN m.owner o where (m.title like %:title% OR :title = NULL) and o.id = :userId",
          countQuery = "SELECT count(m) FROM MusicProfile m JOIN m.owner o where (m.title like %:title% OR :title = NULL) and o.id = :userId")
  Page<MusicProfile> findMusicProfilesByFilter(@Param("title") String title, @Param("userId") Long userId, Pageable pageable);

  @Query("SELECT m FROM MusicProfile m JOIN m.owner u where m.id = ?1 and u.id = ?2")
  Optional<MusicProfile> getMusicProfileByUser(Long musicId, Long userId);
}
