package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.auth.AdminRegisterRequest;
import com.theinnovationtrio.TeamFinderAPI.auth.UserRegisterRequest;
import com.theinnovationtrio.TeamFinderAPI.department.Department;
import com.theinnovationtrio.TeamFinderAPI.enums.Role;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    User createUser(AdminRegisterRequest adminRegisterRequest);

    User createUser(UserRegisterRequest userRegisterRequest, UUID organizationId);

    User getUserById(UUID userId);

    User getUserByEmail(String email);

    boolean existsByEmail(String email);

    List<UserDto> getAllUsers();

    List<UserDto> getOrganizationUsers();
    UserDto getConnectedUser();

    List<UserDto> getAllUnemployedUsers();

    void addRoleToUser(UUID userId, List<Role> roles);

    User removeDepartmentFromUser(UUID userToRemoveId);

    User addDepartmentToUser(UUID userToAssignId, Department department);

    void deleteUserById(UUID userId);

}
