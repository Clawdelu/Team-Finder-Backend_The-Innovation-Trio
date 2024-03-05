package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.auth.AdminSignUpDto;
import com.theinnovationtrio.TeamFinderAPI.auth.SignUpDto;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    User createUser(SignUpDto signUpDto, UUID organizationId);
    User createUser(AdminSignUpDto adminSignUpDto);
    User getUserById(UUID userId);
    List<User> getAllUsers();
    User updateUser(UUID userId, UserDto userDto);
    void deleteUserById(UUID userId);

}
