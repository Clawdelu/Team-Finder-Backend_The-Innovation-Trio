package com.theinnovationtrio.TeamFinderAPI.department;

import java.util.List;
import java.util.UUID;

public interface IDepartmentService {
    Department createDepartment(UUID userId, DepartmentDto departmentDto);
    List<Department> getAllDepartments();
    Department getDepartmentById(UUID departmentId);
    Department updateDepartment(UUID userId, UUID departmentId, DepartmentDto departmentDto);
    void addDepartmentToUser(UUID userId,List<UUID> userToAssignIds);
    void deleteDepartmentById(UUID userId, UUID departmentId);
}
