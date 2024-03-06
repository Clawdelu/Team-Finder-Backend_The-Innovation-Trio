package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.auth.AdminSignUpDto;
import com.theinnovationtrio.TeamFinderAPI.auth.SignUpDto;
import com.theinnovationtrio.TeamFinderAPI.enums.Role;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    User createUser(SignUpDto signUpDto, UUID organizationId);
    User createUser(AdminSignUpDto adminSignUpDto);
    User getUserById(UUID userId);
    List<User> getAllUsers();
    List<User> getAllUnemployedUsers();
    void addRoleToUser(UUID userId,List<Role> roles);
    User updateUser(UUID userId, UserDto userDto);
    void deleteUserById(UUID userId);

}
