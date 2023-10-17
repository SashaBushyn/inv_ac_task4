package com.example.demo.controller;

import com.example.demo.dto.RequestUserDto;
import com.example.demo.dto.ResponseUserDto;
import com.example.demo.dto.UpdateUserDTO;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping("/find")
    public ResponseEntity<ResponseUserDto> findUserByEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.getByEmail(email));
    }

    @GetMapping("/users")
    public ResponseEntity<Page<ResponseUserDto>> getUsers(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<ResponseUserDto> usersPage = userService.getAllUsers(pageable);
        return ResponseEntity.ok(usersPage);
    }

    @PutMapping()
    public ResponseEntity<ResponseUserDto> updateUser(@RequestParam("email") String email,
                                                      @RequestBody @Valid UpdateUserDTO updatedCreateUserDto) {
        ResponseUserDto updatedUser = userService.updateUser(email, updatedCreateUserDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteUser(@RequestParam("email") String email) {
        userService.deleteByEmail(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("User with email " + email + " has been successfully deleted.");
    }

    @PostMapping()
    public ResponseEntity<ResponseUserDto> saveUser(@RequestBody @Valid RequestUserDto requestUserDTO) {
        return ResponseEntity.ok(userService.save(requestUserDTO));
    }
}
