package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.experimental.Accessors;



import java.util.UUID;

@Data
@Accessors(chain = true)
public class RequestUserDto {
  @Valid
  @Null
  private UUID id;
  @Valid
  @NotEmpty
  private String firstName;
  @Valid
  @NotEmpty
  private String lastName;
  @Valid
  @NotEmpty(message = "email mustn't be blank")
  @Email
  private String email;
  @Valid
  @NotEmpty(message = "password mustn't be blank")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
}
