package com.example.demo.service;

import com.example.demo.dto.RequestUserDto;
import com.example.demo.dto.ResponseUserDto;
import com.example.demo.dto.UpdateUserDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public ResponseUserDto save(RequestUserDto requestUserDTO) {
        UserEntity user = modelMapper.map(requestUserDTO, UserEntity.class);
        user.setIsActive(true);
        return modelMapper.map(userRepository.save(user), ResponseUserDto.class);
    }

    public ResponseUserDto getByEmail(String email) {
        return userRepository.findByEmail(email).map(u -> modelMapper.map(u, ResponseUserDto.class))
                .orElseThrow(() -> new EntityNotFoundException(String.format("user with email: %s was not found", email)));
    }

    public Page<ResponseUserDto> getAllUsers(Pageable pageable) {
        Page<UserEntity> userPage = userRepository.findAll(pageable);
        return userPage.map(userEntity -> modelMapper.map(userEntity, ResponseUserDto.class));
    }

    public void deleteByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    public ResponseUserDto updateUser(String email, UpdateUserDTO updatedCreateUserDto) {
        ResponseUserDto existingUser = getByEmail(email);
        UserEntity user = modelMapper.map(existingUser, UserEntity.class);
        user.setFirstName(updatedCreateUserDto.getFirstName());
        user.setLastName(updatedCreateUserDto.getLastName());
        return modelMapper.map(userRepository.save(user), ResponseUserDto.class);
    }
}
