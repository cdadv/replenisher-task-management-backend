package com.walmart.labs.domain;

import javax.persistence.Entity;

@Entity
public class Corporation extends BasicDomain {

  /** Corporation name */
  private String name;

  public Corporation(){}

  public Corporation(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
