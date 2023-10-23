package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.List;
import lombok.Data;

import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Entity
@Table(name = "user_table")
public class UserEntity implements UserDetails {
  @Id
  @GeneratedValue(generator = "org.hibernate.id.UUIDGenerator")
  @Column(name = "user_id", unique = true, nullable = false)
  private UUID id;
  private String firstName;
  private String lastName;
  @Column(unique = true)
  private String email;
  private String password;
  private Boolean isActive;
  @Enumerated(EnumType.STRING)
  private Role role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return isActive;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return isActive;
  }
}
