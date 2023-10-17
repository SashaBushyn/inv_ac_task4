package com.example.demo.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class UserResponseDto {
  private UUID id;
  private String firstName;
  private String lastName;
  private String email;
}
