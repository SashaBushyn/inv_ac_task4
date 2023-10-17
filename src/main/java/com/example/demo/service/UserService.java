package com.example.demo.service;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.dto.UpdateUserDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  public UserResponseDto save(UserRequestDto userRequestDTO) {
    UserEntity user = modelMapper.map(userRequestDTO, UserEntity.class);
    user.setIsActive(true);
    return modelMapper.map(userRepository.save(user), UserResponseDto.class);
  }

  public UserResponseDto getByEmail(String email) {
    return userRepository.findByEmail(email).map(u -> modelMapper.map(u, UserResponseDto.class))
        .orElseThrow(() -> new EntityNotFoundException(String.format("user with email: %s was not found", email)));
  }

  public Page<UserResponseDto> getAllUsers(Pageable pageable) {
    Page<UserEntity> userPage = userRepository.findAll(pageable);
    return userPage.map(userEntity -> modelMapper.map(userEntity, UserResponseDto.class));
  }

  public void deleteById(UUID id) {
    userRepository.deleteById(id);
  }

  public UserResponseDto updateUser(UUID id, UpdateUserDTO updatedCreateUserDto) {
    UserEntity existingUser = userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(String.format("user with id: %s was not found", id)));
    existingUser.setFirstName(updatedCreateUserDto.getFirstName());
    existingUser.setLastName(updatedCreateUserDto.getLastName());
    return modelMapper.map(userRepository.save(existingUser), UserResponseDto.class);
  }
}
