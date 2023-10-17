package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;
@Data
@Entity
@Table(name = "user_table")
public class UserEntity {
    @Id
    @GeneratedValue(generator = "org.hibernate.id.UUIDGenerator")
    @Column(name = "user_id", unique = true, nullable = false)
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Boolean isActive;
}
