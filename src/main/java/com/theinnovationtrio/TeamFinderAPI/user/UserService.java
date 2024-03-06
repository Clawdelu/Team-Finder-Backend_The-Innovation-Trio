package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.auth.AdminSignUpDto;
import com.theinnovationtrio.TeamFinderAPI.auth.SignUpDto;
import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import com.theinnovationtrio.TeamFinderAPI.invite.IInviteService;
import com.theinnovationtrio.TeamFinderAPI.organization.IOrganizationService;
import com.theinnovationtrio.TeamFinderAPI.organization.Organization;
import com.theinnovationtrio.TeamFinderAPI.organization.OrganizationDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final IOrganizationService organizationService;
    private final IInviteService inviteService;

    @Override
    public User createUser(SignUpDto signUpDto, UUID organizationId) {
        String encryptedPassword = passwordEncoder.encode(signUpDto.getPassword());
        User user = userMapper.signUpDtotoUser(signUpDto, encryptedPassword);
        user.setRoles(new ArrayList<>(Arrays.asList(Role.Employee)));
        user.setId(UUID.randomUUID());
        user.setAvailableHours(8);
        user.setOrganizationId(organizationId);
        // Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        // user.setRoles(Collections.singleton(roles));
        return userRepository.save(user);
    }

    @Override
    public User createUser(AdminSignUpDto adminSignUpDto) {
        String encryptedPassword = passwordEncoder.encode(adminSignUpDto.getPassword());
        User user = userMapper.adminSignUpDtotoUser(adminSignUpDto, encryptedPassword);
        user.setRoles(new ArrayList<>(Arrays.asList(Role.Organization_Admin, Role.Employee)));
        user.setId(UUID.randomUUID());
        user.setAvailableHours(8);
        Organization organization = organizationService
                .createOrganization(new OrganizationDto(adminSignUpDto.getOrganizationName(), adminSignUpDto.getHeadquarterAddress()), user);
        user.setOrganizationId(organization.getId());
        return userRepository.save(user);
    }

    @Override
    public User getUserById(UUID userId) {

        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    @Override
    public List<User> getAllUnemployedUsers() {
        List<User> unemployedUsers = getAllUsers();
        return unemployedUsers.stream()
                .filter(user -> user.getDepartment() == null)
                .collect(Collectors.toList());
    }

    @Override
    public void addRoleToUser(UUID userId, List<Role> roles) {
        User user = getUserById(userId);
        boolean hasEmplpoyeeRole = roles.stream()
                        .anyMatch(role -> role == Role.Employee);
        if(!hasEmplpoyeeRole){
            roles.add(Role.Employee);
        }
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public User updateUser(UUID userId, UserDto userDto) {

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setRoles(userDto.getRoles());
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.deleteById(userId);

    }
}
