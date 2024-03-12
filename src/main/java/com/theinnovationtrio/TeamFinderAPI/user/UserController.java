package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @GetMapping("/same-organization")
    public ResponseEntity<?> getAllUsersFromOrganization(Principal connectedUser) {
        try {
            List<User> organizationUsers = userService.getOrganizationUsers(connectedUser);
            if (organizationUsers.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(organizationUsers);
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }

    }

    @GetMapping("/unassigned")
    public ResponseEntity<?> getAllUnemployedUsers(Principal connectedUser) {
        try {
            List<User> unemployedUsers = userService.getAllUnemployedUsers(connectedUser);
            if (unemployedUsers.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(unemployedUsers);
            }
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @PatchMapping("{userId}/assign-roles/")
    public ResponseEntity<?> addRoleToUser(@PathVariable UUID userId, @RequestBody List<Role> roles, Principal connectedUser) {
        try {
            userService.addRoleToUser(connectedUser, userId, roles);
            return ResponseEntity.ok("Roles have been added successfully!");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID userId, Principal connectedUser) {
        try {
            userService.deleteUserById(userId,connectedUser);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
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
