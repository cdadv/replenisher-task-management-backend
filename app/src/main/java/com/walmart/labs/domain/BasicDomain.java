package com.walmart.labs.domain;

import java.io.Serializable;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class BasicDomain implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(updatable = false)
  private Date timeCreated;

  private Date timeUpdated;

  @PrePersist
  void onCreate() {
    Date now = Date.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant());
    setTimeCreated(now);
    setTimeUpdated(now);
  }

  @PreUpdate
  void onUpdate() {
    Date now = Date.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant());
    setTimeUpdated(now);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getTimeCreated() {
    return timeCreated;
  }

  public void setTimeCreated(Date timeCreated) {
    this.timeCreated = timeCreated;
  }

  public Date getTimeUpdated() {
    return timeUpdated;
  }

  public void setTimeUpdated(Date timeUpdated) {
    this.timeUpdated = timeUpdated;
  }
}
