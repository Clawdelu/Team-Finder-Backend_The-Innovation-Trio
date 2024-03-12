package com.theinnovationtrio.TeamFinderAPI.department;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface IDepartmentService {
    Department createDepartment(Principal connectedUser, DepartmentDto departmentDto);
    List<Department> getAllDepartments();
    List<Department> getAllSameOrgDepartments(Principal connectedUser);
    Department getDepartmentById(UUID departmentId);
    Department updateDepartment(Principal connectedUser, UUID departmentId, DepartmentDto departmentDto);
    void addDepartmentToUser(Principal connectedUser,List<UUID> userToAssignIds);
    void deleteDepartmentById(Principal connectedUser, UUID departmentId);
}
