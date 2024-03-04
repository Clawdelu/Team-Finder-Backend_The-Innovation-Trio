package com.theinnovationtrio.TeamFinderAPI.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable UUID userId){
        try{
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);}
        catch (EntityNotFoundException ex){
            String errorMessage = "Utilizatorul cu ID-ul " + userId + " nu a fost găsit.";
            ErrorMessage errorResponse = new ErrorMessage(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse);
          //  return ResponseEntity.notFound().build();
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
