package com.softserve.maklertaboo.controller;

import com.softserve.maklertaboo.dto.user.UserDto;
import com.softserve.maklertaboo.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/create")
    public void createUser(@Valid @RequestBody UserDto userDto) {
        userService.saveUser(userDto);
    }

    @ApiResponses({

            @ApiResponse(code = 200, message = "OK")
    })
    @GetMapping("/all")
    public List<UserDto> getAllUser() {
        return userService.findAllUser();
    }

    @GetMapping("/all/{page}/{size}")
    public Page<UserDto> getAllUser(@PathVariable Integer page, @PathVariable Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.findByPage(pageable);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @GetMapping("/email/{email}")
    public UserDto getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    @GetMapping("/username/{username}")
    public UserDto getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @GetMapping("/exists/email/{email}")
    public Boolean emailExists(@PathVariable String email) {
        return userService.emailExists(email);
    }

    @PutMapping("/update/all")
    public void updateUser(@RequestBody UserDto userDto) {
        userService.updateUser(userDto.getId(), userDto);
    }

    @PutMapping("/update/{photo}")
    public void updateUserPhoto(Long id, @PathVariable String photo) {
        userService.updateUserPhoto(id, photo);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}

