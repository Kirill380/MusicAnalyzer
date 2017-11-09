package io.redkite.music.analyzer.repository;


import io.redkite.music.analyzer.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleDao extends JpaRepository<Role, Long> {

  public Role findByName(String name);
}
