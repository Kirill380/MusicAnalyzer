package io.redkite.music.analyzer.repository;




import io.redkite.music.analyzer.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByEmail(String email);

  @Query(value = "SELECT u FROM User u where u.email like %:email% OR :email = NULL",
          countQuery = "SELECT count(u.id) FROM User u where u.email like %:email% OR :email = NULL")
  Page<User> findUserByFilter(@Param("email") String email, Pageable pageable);

  @Query("SELECT count(u.id) > 0 FROM User u where u.email = ?1")
  boolean existsByEmail(String email);

}
