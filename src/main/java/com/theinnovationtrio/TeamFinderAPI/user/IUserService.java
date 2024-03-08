package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.auth.AdminSignUpDto;
import com.theinnovationtrio.TeamFinderAPI.auth.SignUpDto;
import com.theinnovationtrio.TeamFinderAPI.department.Department;
import com.theinnovationtrio.TeamFinderAPI.enums.Role;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    User createUser(SignUpDto signUpDto, UUID organizationId);
    User createUser(AdminSignUpDto adminSignUpDto);
    User getUserById(UUID userId);
    List<User> getAllUsers();
    List<User> getOrganizationUsers(UUID userId);
    List<User> getAllUnemployedUsers(UUID userId);
    void addRoleToUser(UUID userId, UUID userRoleId, List<Role> roles);
    User removeDepartmentFromUser(UUID userToRemoveId);
    User addDepartmentToUser(UUID userToAssignId, Department department);
    User updateUserRole(UUID userId, UserDto userDto);
    void deleteUserById(UUID userId);

}
