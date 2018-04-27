package com.walmart.labs.domain.mapping;

import com.walmart.labs.domain.User;
import com.walmart.labs.domain.UserRole;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_role_mapping")
public class UserRoleMapping implements Serializable {
  private User user;
  private UserRole role;

  @Id
  @ManyToOne
  @JoinColumn(name = "user_id")
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Id
  @ManyToOne
  @JoinColumn(name = "role_id")
  public UserRole getRole() {
    return role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }
}
