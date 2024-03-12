package com.theinnovationtrio.TeamFinderAPI.department;

import io.swagger.annotations.ApiOperation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/departments")
public class DepartmentController {
    private final IDepartmentService departmentService;

    @ApiOperation(value = "Create a department", notes = "AAAAAaaaasdasd")
    @PostMapping()
    public ResponseEntity<?> createDepartment(@RequestBody DepartmentDto departmentDto, Principal connectedUser) {
        try {
            Department savedDepartment = departmentService.createDepartment(connectedUser, departmentDto);
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

    @GetMapping()
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        if (departments.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(departments);
        }
    }

    @GetMapping("/same-organization")
    public ResponseEntity<?> getAllDepartmentsFromSameOrganization(Principal connectedUser) {
        try {
            List<Department> departments = departmentService.getAllSameOrgDepartments(connectedUser);
            if (departments.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(departments);
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }

    }

    @GetMapping("{departmentId}")
    public ResponseEntity<?> getDepartmentById(@PathVariable UUID departmentId) {
        try {
            Department department = departmentService.getDepartmentById(departmentId);
            //afisez pt test users
            System.out.println("Department ID: " + department.getId() + " Users: ");
            department.getUsers().forEach(u -> System.out.println(u.getUserName() + " User ID: " + u.getId()));
            return ResponseEntity.ok(department);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        }
    }

    @PutMapping("{departmentId}")
    public ResponseEntity<?> updateDepartment(@PathVariable UUID departmentId, @RequestBody DepartmentDto departmentDto,
                                              Principal connectedUser) {
        try {
            Department updatedDepartment = departmentService.updateDepartment(connectedUser, departmentId, departmentDto);
            return ResponseEntity.ok(updatedDepartment);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Message: " + ex.getMessage());
        }
    }

    @PatchMapping("/members")
    public ResponseEntity<?> addDepartmentToUsers(@RequestParam List<UUID> userToAssignIds, Principal connectedUser) {
        try {
            departmentService.addDepartmentToUser(connectedUser, userToAssignIds);
            return ResponseEntity.ok("User/s have been added successfully to department!");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @DeleteMapping("{departmentId}")
    public ResponseEntity<Object> deleteDepartmentById(@PathVariable UUID departmentId, Principal connectedUser) {
        try {
            departmentService.deleteDepartmentById(connectedUser, departmentId);
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
