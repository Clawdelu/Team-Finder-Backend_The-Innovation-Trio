package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final IUserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable UUID userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (EntityNotFoundException ex) {
            String errorMessage = "The user with ID " + userId + " was not found.";
            ErrorMessage errorResponse = new ErrorMessage(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse);
        }
    }

    @GetMapping("/unemployed")
    public ResponseEntity<List<User>> getAllUnemployedUsers() {
        List<User> unemployedUsers = userService.getAllUnemployedUsers();
        if (unemployedUsers.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(unemployedUsers);
        }
    }

    @PatchMapping("/{userId}/roles")
    public ResponseEntity<?> addRoleToUser(@PathVariable UUID userId, @RequestParam List<Role> roles){
        try{
            userService.addRoleToUser(userId, roles);
            return ResponseEntity.ok("Roles have been added successfully!");
        } catch(EntityNotFoundException ex){
            return ResponseEntity.notFound().build();
        }

    }

//    @PostMapping("/users")
//    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
//        User savedUser = userService.createUser(userDto);
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(savedUser.getId())
//                .toUri();
//        return ResponseEntity.created(location).build();
//    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable UUID userId) {
        userService.deleteUserById(userId);
    }

    // UPDATE ROLE
//    @PutMapping("/{userId}")
//    public ResponseEntity<User> updateUser(@PathVariable UUID userId, @RequestBody UserDto userDto) {
//        try {
//            User updatedUser = userService.updateUser(userId, userDto);
//            return ResponseEntity.ok(updatedUser);
//        } catch (EntityNotFoundException ex) {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
