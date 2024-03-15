package com.theinnovationtrio.TeamFinderAPI.department;

import java.util.List;
import java.util.UUID;

public interface IDepartmentService {
    Department createDepartment(DepartmentDto departmentDto);

    List<Department> getAllDepartments();

    List<Department> getAllSameOrgDepartments();

    Department getDepartmentById(UUID departmentId);

    Department updateDepartment(UUID departmentId, DepartmentDto departmentDto);

    void addUsersToDepartment(List<UUID> userToAssignIds);

    void removeUsersFromDepartment(List<UUID> userToRemoveIds);

    void deleteDepartmentById(UUID departmentId);
}
