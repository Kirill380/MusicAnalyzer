package io.redkite.music.analyzer.model;


import io.redkite.music.analyzer.common.DabConstants;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Data
@ToString(of = {"id", "email"})
@EqualsAndHashCode(of = {"email"})
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = DabConstants.UserTable.EMAIL, unique = true, nullable = false)
  private String email;

  @Column(name = DabConstants.UserTable.FIRST_NAME)
  private String firstName;

  @Column(name = DabConstants.UserTable.LAST_NAME)
  private String lastName;

  @Column(name = DabConstants.UserTable.PASSWORD_HASH, nullable = false)
  private String passwordHash;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
  private Set<MusicProfile> musicProfiles;

  public void addPlant(MusicProfile musicProfile) {
    addPlant(musicProfile, true);
  }

  void addPlant(MusicProfile musicProfile, boolean bi) {
    if (musicProfile != null) {
      musicProfiles.add(musicProfile);
      if (bi) {
        musicProfile.setOwner(this, false);
      }
    }
  }


  @Column(name = "creation_date")
  private LocalDateTime creationDate;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Role role;


}