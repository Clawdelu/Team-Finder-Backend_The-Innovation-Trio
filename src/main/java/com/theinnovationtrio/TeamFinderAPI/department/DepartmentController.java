package com.theinnovationtrio.TeamFinderAPI.department;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class DepartmentController {
    private final IDepartmentService departmentService;

    @PostMapping("{userId}/departments")
    public ResponseEntity<?> createDepartment(@PathVariable UUID userId, @RequestBody DepartmentDto departmentDto) {
        try {
            Department savedDepartment = departmentService.createDepartment(userId, departmentDto);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedDepartment.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        if (departments.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(departments);
        }
    }

    @GetMapping("/departments/{departmentId}")
    public ResponseEntity<?> getDepartmentById(@PathVariable UUID departmentId) {
        try {
            Department department = departmentService.getDepartmentById(departmentId);
            //afisez pt test users
            System.out.println("Department ID: " + department.getId() + " Users: " );
                    department.getUsers().forEach(u->System.out.println(u.getUserName() + " User ID: "+ u.getId()));

            return ResponseEntity.ok(department);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        }
    }

    @PutMapping("/{userId}/departments/{departmentId}")
    public ResponseEntity<?> updateDepartment(@PathVariable UUID userId, @PathVariable UUID departmentId, @RequestBody DepartmentDto departmentDto) {
        try {
            Department updatedDepartment = departmentService.updateDepartment(userId, departmentId, departmentDto);
            return ResponseEntity.ok(updatedDepartment);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }

    @PatchMapping("/{userId}/departments")
    public ResponseEntity<?> addDepartmentToUsers(@PathVariable UUID userId, @RequestParam List<UUID> userToAssignIds) {
        try {
            departmentService.addDepartmentToUser(userId, userToAssignIds);
            return ResponseEntity.ok("User/s have been added successfully to department!");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{userId}/departments/{departmentId}")
    public ResponseEntity<Object> deleteDepartmentById(@PathVariable UUID userId, @PathVariable UUID departmentId) {
        try {
            departmentService.deleteDepartmentById(userId, departmentId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }
}
