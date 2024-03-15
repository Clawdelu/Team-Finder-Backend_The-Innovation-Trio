package com.theinnovationtrio.TeamFinderAPI.department;

import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import com.theinnovationtrio.TeamFinderAPI.user.IUserService;
import com.theinnovationtrio.TeamFinderAPI.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentService implements IDepartmentService {

    private final DepartmentRepository departmentRepository;
    private final IUserService userService;

    @Override
    public Department createDepartment(DepartmentDto departmentDto) {

        User adminUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasAdminRole = adminUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Organization_Admin));

        if (hasAdminRole) {
            Department department = new Department();
            department.setId(UUID.randomUUID());
            department.setCreatedBy(adminUser.getId());
            department.setDepartmentName(departmentDto.getDepartmentName());
            if (departmentDto.getDepartmentManager() != null) {
                assignDepartmentManager(departmentDto, adminUser, department);
                departmentRepository.save(department);
                userService.addDepartmentToUser(departmentDto.getDepartmentManager(), department);
            } else {
                departmentRepository.save(department);
            }
            return department;
        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }
    }

    private void assignDepartmentManager(DepartmentDto departmentDto, User user, Department department) {

        try {

            User userManager = userService.getUserById(departmentDto.getDepartmentManager());
            boolean hasDepartmentManagerRoleAndSameOrganization = userManager.getRoles().stream()
                    .anyMatch(role -> role.equals(Role.Department_Manager))
                    && userManager.getOrganizationId().equals(user.getOrganizationId());
            if (hasDepartmentManagerRoleAndSameOrganization) {
                department.setDepartmentManager(departmentDto.getDepartmentManager());
            } else {
                throw new RuntimeException("The user you want to assign as a department manager is not eligible.");
            }
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException("Department manager you want to assign does not exist.");
        }
    }

    @Override
    public List<Department> getAllDepartments() {

        return departmentRepository.findAll();
    }

    @Override
    public List<Department> getAllSameOrgDepartments() {

        User adminUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasAdminRole = adminUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Organization_Admin));
        if (hasAdminRole) {
            return departmentRepository.findAllSameOrgById(adminUser.getId());

        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }
    }

    @Override
    public Department getDepartmentById(UUID departmentId) {

        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));
    }

    @Override
    public Department updateDepartment(UUID departmentId, DepartmentDto departmentDto) {

        User adminUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasAdminRole = adminUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Organization_Admin));

        Department department = getDepartmentById(departmentId);

        boolean hasSameOrganization = userService.getUserById(department.getCreatedBy()).getOrganizationId()
                .equals(adminUser.getOrganizationId());

        if (hasAdminRole && hasSameOrganization) {
            department.setCreatedBy(adminUser.getId());

            department.setDepartmentName(departmentDto.getDepartmentName());

            if (departmentDto.getDepartmentManager() != null) {
                if (userService.getUserById(departmentDto.getDepartmentManager()).getDepartment() == null) {
                    assignDepartmentManager(departmentDto, adminUser, department);

                    departmentRepository.save(department);

                    userService.addDepartmentToUser(departmentDto.getDepartmentManager(), department);
                } else {
                    throw new RuntimeException("The manager has already a department");
                }

            } else {
                userService.removeDepartmentFromUser(department.getDepartmentManager());
                department.setDepartmentManager(null);
            }
            departmentRepository.save(department);
            return department;

        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }
    }

    @Override
    public void addUsersToDepartment(List<UUID> userToAssignIds) {

        User departManagerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasDepartmentManagerRole = departManagerUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Department_Manager));
        if (hasDepartmentManagerRole) {
            Department department = getDepartmentById(departManagerUser.getDepartment().getId());
            List<User> departmentUsers = department.getUsers();
            userToAssignIds.forEach(userToAssignId -> {
                boolean hasSameOrganization = userService.getUserById(userToAssignId).getOrganizationId()
                        .equals(departManagerUser.getOrganizationId());
                if (hasSameOrganization) {
                    User user = userService.addDepartmentToUser(userToAssignId, department);
                    departmentUsers.add(user);
                } else {
                    throw new AccessDeniedException("Unauthorized access!");
                }
            });
            department.setUsers(departmentUsers);
            departmentRepository.save(department);
        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }
    }

    @Override
    public void removeUsersFromDepartment(List<UUID> userToRemoveIds) {

        User departManagerUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasDepartmentManagerRole = departManagerUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Department_Manager));
        if (hasDepartmentManagerRole) {
            Department department = getDepartmentById(departManagerUser.getDepartment().getId());
            List<User> departmentUsers = department.getUsers();
            userToRemoveIds.forEach(userToRemoveId -> {
                User user = userService.removeDepartmentFromUser(userToRemoveId);
                departmentUsers.remove(user);
            });
            department.setUsers(departmentUsers);
            departmentRepository.save(department);
        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }
    }

    @Override
    public void deleteDepartmentById(UUID departmentId) {

        User adminUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasAdminRole = adminUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Organization_Admin));
        Department department = getDepartmentById(departmentId);
        boolean hasSameOrganization = userService.getUserById(department.getCreatedBy()).getOrganizationId()
                .equals(adminUser.getOrganizationId());
        if (hasAdminRole && hasSameOrganization) {
            if (department.getUsers() != null) {
                department.getUsers().forEach(userToRemoveDepartment ->
                        userService.removeDepartmentFromUser(userToRemoveDepartment.getId()));
            }
            if (department.getDepartmentManager() != null) {
                userService.removeDepartmentFromUser(department.getDepartmentManager());
            }
            department.setDepartmentManager(null);
            department.setUsers(null);
            departmentRepository.deleteById(departmentId);
        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }
    }
}
