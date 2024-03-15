package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.auth.AdminRegisterRequest;
import com.theinnovationtrio.TeamFinderAPI.auth.UserRegisterRequest;
import com.theinnovationtrio.TeamFinderAPI.department.Department;
import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import com.theinnovationtrio.TeamFinderAPI.organization.IOrganizationService;
import com.theinnovationtrio.TeamFinderAPI.organization.Organization;
import com.theinnovationtrio.TeamFinderAPI.organization.OrganizationDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    public User createUser(UserRegisterRequest userRegisterRequest, UUID organizationId) {
        String encryptedPassword = passwordEncoder.encode(userRegisterRequest.getPassword());
        User user = userMapper.mapToUser(userRegisterRequest, encryptedPassword);
        user.setRoles(new ArrayList<>(List.of(Role.Employee)));
        user.setId(UUID.randomUUID());
        user.setAvailableHours(8);
        user.setOrganizationId(organizationId);
        return userRepository.save(user);
    }

    @Override
    public User createUser(AdminRegisterRequest adminRegisterRequest) {
        String encryptedPassword = passwordEncoder.encode(adminRegisterRequest.getPassword());
        User user = userMapper.mapToUser(adminRegisterRequest, encryptedPassword);
        user.setRoles(new ArrayList<>(Arrays.asList(Role.Organization_Admin, Role.Employee)));
        user.setId(UUID.randomUUID());
        user.setAvailableHours(8);
        Organization organization = organizationService
                .createOrganization(new OrganizationDto(adminRegisterRequest.getOrganizationName(), adminRegisterRequest.getHeadquarterAddress()), user);
        user.setOrganizationId(organization.getId());
        return userRepository.save(user);
    }


    @Override
    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.INSTANCE.mapToUserDto(userRepository.findAll());
    }

    @Override
    public List<UserDto> getOrganizationUsers() {
        User adminUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasAdminRole = adminUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Organization_Admin));
        if (hasAdminRole) {
            List<User> users = userRepository.findAllByOrganizationId(adminUser.getOrganizationId());
            return userMapper.INSTANCE.mapToUserDto(users);
        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }

    }

    @Override
    public UserDto getConnectedUser() {
        User contextUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user =getUserById(contextUser.getId());
        return userMapper.INSTANCE.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUnemployedUsers() {
        User adminUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasAdminRole = adminUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Organization_Admin));
        if (hasAdminRole) {
            List<UserDto> unemployedUsers = getAllUsers();
            return unemployedUsers.stream()
                    .filter(user -> user.getDepartment() == null
                            && user.getOrganizationId().equals(adminUser.getOrganizationId()))
                    .collect(Collectors.toList());
        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }
    }

    @Override
    public void addRoleToUser(UUID userId, List<Role> roles) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasAdminRole = user.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Organization_Admin));
        if (hasAdminRole) {
            boolean hasEmployeeRole = roles.stream()
                    .anyMatch(role -> role == Role.Employee);
            if (!hasEmployeeRole) {
                roles.add(Role.Employee);
            }
            try {
                User userChangeRole = getUserById(userId);
                userChangeRole.setRoles(roles);
                userRepository.save(userChangeRole);
            } catch (EntityNotFoundException ex) {
                throw new EntityNotFoundException("The user you want to assign roles to does not exist.");
            }

        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }

    }

    @Override
    public User removeDepartmentFromUser(UUID userToRemoveId) {
        User userToRemoveDepartment = getUserById(userToRemoveId);
        userToRemoveDepartment.setDepartment(null);
        return userRepository.save(userToRemoveDepartment);
    }

    @Override
    public User addDepartmentToUser(UUID userToAssignId, Department department) {
        User userToAssignDepartment = getUserById(userToAssignId);
        userToAssignDepartment.setDepartment(department);
        return userRepository.save(userToAssignDepartment);
    }


    @Override
    public void deleteUserById(UUID userId) {
        User adminUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasAdminRole = adminUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Organization_Admin));
        User userToDelete = getUserById(userId);
        boolean hasSameOrganization = userToDelete.getOrganizationId()
                .equals(adminUser.getOrganizationId());
        if (hasAdminRole && hasSameOrganization) {
            userRepository.deleteById(userId);
        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }
    }
}
