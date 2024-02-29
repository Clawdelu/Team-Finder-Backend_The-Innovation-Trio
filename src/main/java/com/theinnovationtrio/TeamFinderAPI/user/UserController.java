package com.theinnovationtrio.TeamFinderAPI.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers(){return userService.getAllUsers();}

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable UUID id){
        try{
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);}
        catch (EntityNotFoundException ex){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto){
        User savedUser = userService.createUser(userDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable UUID id){ userService.deleteUserById(id);}

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody UserDto userDto){
        try{
            User updatedUser = userService.updateUser(id,userDto);
            return ResponseEntity.ok(updatedUser);
        } catch (EntityNotFoundException ex){
            return ResponseEntity.notFound().build();
        }
    }
}
