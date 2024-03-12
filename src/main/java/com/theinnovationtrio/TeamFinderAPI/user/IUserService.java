package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.auth.AdminRegisterRequest;
import com.theinnovationtrio.TeamFinderAPI.auth.UserRegisterRequest;
import com.theinnovationtrio.TeamFinderAPI.auth1.AdminSignUpDto;
import com.theinnovationtrio.TeamFinderAPI.auth1.SignUpDto;
import com.theinnovationtrio.TeamFinderAPI.department.Department;
import com.theinnovationtrio.TeamFinderAPI.enums.Role;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    User createUser(SignUpDto signUpDto, UUID organizationId);
    User createUser(AdminSignUpDto adminSignUpDto);
    User createUser(AdminRegisterRequest adminRegisterRequest);
    User createUser(UserRegisterRequest userRegisterRequest, UUID organizationId);
    User getUserById(UUID userId);
    User getUserByEmail(String email);
    boolean existsByEmail(String email);
    List<User> getAllUsers();
    List<User> getOrganizationUsers(UUID userId);
    List<User> getAllUnemployedUsers(UUID userId);
    void addRoleToUser(UUID userId, UUID userRoleId, List<Role> roles);
    User removeDepartmentFromUser(UUID userToRemoveId);
    User addDepartmentToUser(UUID userToAssignId, Department department);
    User updateUserRole(UUID userId, UserDto userDto);
    void deleteUserById(UUID userId);

}
