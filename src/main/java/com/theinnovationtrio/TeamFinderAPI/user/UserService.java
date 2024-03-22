package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.auth.AdminRegisterRequest;
import com.theinnovationtrio.TeamFinderAPI.auth.UserRegisterRequest;
import com.theinnovationtrio.TeamFinderAPI.department.Department;
import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import com.theinnovationtrio.TeamFinderAPI.organization.IOrganizationService;
import com.theinnovationtrio.TeamFinderAPI.organization.Organization;
import com.theinnovationtrio.TeamFinderAPI.organization.OrganizationDto;
import com.theinnovationtrio.TeamFinderAPI.skill.ISkillService;
import com.theinnovationtrio.TeamFinderAPI.skill.Skill;
import com.theinnovationtrio.TeamFinderAPI.user_skill.IUserSkillService;
import com.theinnovationtrio.TeamFinderAPI.user_skill.UserSkillDto;
import com.theinnovationtrio.TeamFinderAPI.user_skill.User_Skill;
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
    private final IUserSkillService userSkillService;
    private final ISkillService skillService;

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
        User user = getUserById(contextUser.getId());
        return userMapper.INSTANCE.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUnassignedUsers() {
        User departManagerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasDepartManagerRole = departManagerUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Department_Manager));
        if (hasDepartManagerRole) {
            var unassignedUsers = userRepository.findAllUnassignedEmp(departManagerUser.getOrganizationId());
            return userMapper.INSTANCE.mapToUserDto(unassignedUsers);
        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }
    }

    @Override
    public List<UserDto> getAllFreeDepartManagers() {
        User adminUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasAdminRole = adminUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Organization_Admin));
        if(hasAdminRole){
            var allUnassignedEmp = userRepository.findAllUnassignedEmp(adminUser.getOrganizationId());
            var filteredEmp =  allUnassignedEmp.stream()
                    .filter(user -> user.getRoles().contains(Role.Department_Manager))
                    .collect(Collectors.toList());
            return userMapper.INSTANCE.mapToUserDto(filteredEmp);
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
    public void removeUserSkillById(UUID userSkillId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userToUpdate = getUserById(user.getId());
        List<User_Skill> userSkills = userToUpdate.getSkills();
        userSkills.removeIf(userSkill -> userSkill.getId()
                .equals(userSkillId));
        userToUpdate.setSkills(userSkills);
        userRepository.save(userToUpdate);
        userSkillService.deleteUserSkillById(userSkillId);
    }



    @Override
    public void assignUserSkill(UUID skillId, UserSkillDto userSkillDto) {

        var createdUserSkill = userSkillService.createUserSkill(userSkillDto,skillId);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userToUpdate = getUserById(user.getId());

        List<User_Skill> userSkills = userToUpdate.getSkills();
        userSkills.add(createdUserSkill);
        userToUpdate.setSkills(userSkills);
        userRepository.save(userToUpdate);

    }

    @Override
    public List<User_Skill> getAllUserSkills() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getUserById(user.getId()).getSkills();
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
    public User_Skill updateUserSkill(UUID userSkillId, UserSkillDto userSkillDto) {
        return userSkillService.updateUserSkill(userSkillId,userSkillDto);
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


    @Override
    public void deleteSkillById(UUID skillId) {
        User departmentManagerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Skill skillToDelete = skillService.getSkillById(skillId);
        boolean createdTheSkill = departmentManagerUser.getId()
                .equals(skillToDelete.getCreatedBy());

        if (createdTheSkill) {
            List<User_Skill> user_skillsToDelete = userSkillService.getAllUserSkillsBySkill(skillToDelete);
            user_skillsToDelete.forEach(userSkill -> {
                var users = userRepository.findAllBySkills(userSkill);
                for (User user : users) {
                    user.getSkills().removeIf(userSkill1 -> userSkill1.getId().equals(userSkill.getId()));
                }
                userSkillService.deleteUserSkillById(userSkill.getId());
            });
            skillService.deleteSkillById(skillId);
        } else {
            throw new AccessDeniedException("Unauthorized access! You aren't the author.");
        }
    }
}
