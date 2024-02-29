package com.theinnovationtrio.TeamFinderAPI.user;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    User createUser(UserDto userDto);
    User getUserById(UUID id);
    List<User> getAllUsers();
    User updateUser(UUID userId, UserDto userDto);
    void deleteUserById(UUID userId);

}
