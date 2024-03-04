package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    //private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public User createUser(UserDto userDto) {
        User user = userMapper.INSTANCE.userDtotoUser(userDto);

        user.setId(UUID.randomUUID());
        user.setRoles(new ArrayList<>(Arrays.asList(Role.Employee)));
        user.setAvailableHours(8);

        // manual mapper

//        user.setOrganization(userDto.getOrganization());
//        user.setUserName(userDto.getUserName());
//        user.setEmail(user.getEmail());
//        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User getUserById(UUID userId) {

        return  userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    @Override
    public User updateUser(UUID userId, UserDto userDto) {

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        User updatedUser = userMapper.INSTANCE.userDtotoUser(userDto);
        updatedUser.setId(user.getId());
        updatedUser.setRoles(user.getRoles());
        updatedUser.setAvailableHours(user.getAvailableHours());
        return userRepository.save(updatedUser);
    }

    @Override
    public void deleteUserById(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.deleteById(userId);

    }
}
