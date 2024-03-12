package com.theinnovationtrio.TeamFinderAPI.teamRole;

import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import com.theinnovationtrio.TeamFinderAPI.user.IUserService;
import com.theinnovationtrio.TeamFinderAPI.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamRoleService implements ITeamRoleService {
    private final TeamRoleRepository teamRoleRepository;
    private final IUserService userService;


    @Override
    public TeamRole createTeamRole(Principal connectedUser, TeamRoleDto teamRoleDto) {
        User adminUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        boolean hasAdminRole = adminUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Organization_Admin));
        if (hasAdminRole) {
            TeamRole teamRole =
                    new TeamRole(UUID.randomUUID(), teamRoleDto.getRoleInProject(), adminUser.getId());
            return teamRoleRepository.save(teamRole);
        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }
    }

    @Override
    public List<TeamRole> getAllTeamRoles() {
        return teamRoleRepository.findAll();
    }

    @Override
    public List<TeamRole> getAllSameOrgTeamRoles(Principal connectedUser) {
        List<TeamRole> teamRoles = getAllTeamRoles();
        User adminUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        boolean hasAdminRole = adminUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Organization_Admin));
        if(hasAdminRole){
            return teamRoles.stream()
                    .filter(teamRole -> userService.getUserById(teamRole.getCreatedBy()).getOrganizationId()
                            .equals(adminUser.getOrganizationId()))
                    .collect(Collectors.toList());
        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }
    }

    @Override
    public TeamRole getTeamRoleById(UUID teamRoleId) {
        return teamRoleRepository.findById(teamRoleId).orElseThrow(() -> new EntityNotFoundException("Team Role not found!"));
    }

    @Override
    public TeamRole updateTeamRole(Principal connectedUser, UUID teamRoleId, TeamRoleDto teamRoleDto) {
        User adminUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        boolean hasAdminRole = adminUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Organization_Admin));
        TeamRole teamRole = getTeamRoleById(teamRoleId);
        boolean hasSameOrganization = userService.getUserById(teamRole.getCreatedBy()).getOrganizationId()
                .equals(adminUser.getOrganizationId());
        if(hasAdminRole && hasSameOrganization){
                teamRole.setCreatedBy(adminUser.getId());
                teamRole.setRoleInProject(teamRoleDto.getRoleInProject());
                return teamRoleRepository.save(teamRole);
        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }
    }


    @Override
    public void deleteTeamRole(Principal connectedUser, UUID teamRoleId) {
        User adminUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        boolean hasAdminRole = adminUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Organization_Admin));
        TeamRole teamRole = getTeamRoleById(teamRoleId);
        boolean hasSameOrganization = userService.getUserById(teamRole.getCreatedBy()).getOrganizationId()
                .equals(adminUser.getOrganizationId());
        if(hasAdminRole && hasSameOrganization){
                teamRoleRepository.deleteById(teamRoleId);
        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }

    }
}
