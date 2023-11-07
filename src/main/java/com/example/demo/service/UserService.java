package com.example.demo.service;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.entity.Role;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserDao;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserDao userDao;
    private final ModelMapper modelMapper;

    public UserResponseDto save(UserRequestDto userRequestDTO) {
        UserEntity user = modelMapper.map(userRequestDTO, UserEntity.class);
        user.setIsActive(true);
        user.setRole(Role.ROLE_USER);
        return modelMapper.map(userDao.save(user), UserResponseDto.class);
    }

    public UserResponseDto getByEmail(String email) {
        return userDao.findByEmail(email).map(u -> modelMapper.map(u, UserResponseDto.class))
                .orElseThrow(() -> new EntityNotFoundException(String.format("user with email: %s was not found", email)));
    }

    public List<UserResponseDto> getAllUsers() {
        List<UserEntity> userEntities = userDao.findAll();
        return userEntities.stream().map(userEntity -> modelMapper.map(userEntity, UserResponseDto.class)).toList();
    }

    public void deleteById(UUID id) {
        userDao.deleteUser(id);
    }

    public UserResponseDto updateUser(UUID id, UserUpdateDTO updatedCreateUserDto) {
        UserEntity existingUser = userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("user with id: %s was not found", id)));
        existingUser.setFirstName(updatedCreateUserDto.getFirstName());
        existingUser.setLastName(updatedCreateUserDto.getLastName());
        return modelMapper.map(userDao.save(existingUser), UserResponseDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException(String.format("user with email: %s was not found", username)));
    }
}
