package io.redkite.music.analyzer.model;



import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Data
@Entity
@EqualsAndHashCode(of = {"id"})
public class MusicProfile {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String title;

  @Column
  private String author;

  @Column
  private String album;

  @Column
  private Integer duration; //in seconds

  @Column
  private Integer tempo;

  @Column
  private Integer sampleRate;

  @Column
  private String genre;

  @Column(length = 2 << 10)
  private String predictedGenres;

  @Column
  private String yearRecorded;

  @Column
  private Integer channels;

  @Column(name = "creation_date")
  private LocalDateTime creationDate;

  @ManyToOne
  @JoinColumn(name = "owner_id", nullable = false)
  private User owner;


  public void setOwner(User user) {
    setOwner(user, true);
  }

  void setOwner(User user, boolean bi) {
    if (user != null) {
      this.owner = user;
      if (bi) {
        owner.addPlant(this, false);
      }
    }

  }

}
