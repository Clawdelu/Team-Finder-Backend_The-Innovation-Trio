package com.theinnovationtrio.TeamFinderAPI.teamRole;

import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import com.theinnovationtrio.TeamFinderAPI.user.IUserService;
import com.theinnovationtrio.TeamFinderAPI.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamRoleService implements ITeamRoleService {
    private final TeamRoleRepository teamRoleRepository;
    private final IUserService userService;


    @Override
    public TeamRole createTeamRole(UUID userId, TeamRoleDto teamRoleDto) {
        User adminUser = userService.getUserById(userId);
        boolean hasAdminRole = adminUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Organization_Admin));
        if (hasAdminRole) {
            TeamRole teamRole =
                    new TeamRole(UUID.randomUUID(), teamRoleDto.getRoleInProject(), userId);
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
    public TeamRole getTeamRoleById(UUID teamRoleId) {
        return teamRoleRepository.findById(teamRoleId).orElseThrow(() -> new EntityNotFoundException("Team Role not found!"));
    }

    @Override
    public TeamRole updateTeamRole(UUID userId, UUID teamRoleId, TeamRoleDto teamRoleDto) {
        User adminUser = userService.getUserById(userId);
        boolean hasAdminRole = adminUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Organization_Admin));
        if(hasAdminRole){
            try{
                TeamRole teamRole = getTeamRoleById(teamRoleId);
                teamRole.setCreatedBy(userId);
                teamRole.setRoleInProject(teamRoleDto.getRoleInProject());
                return teamRoleRepository.save(teamRole);
            } catch (EntityNotFoundException ex) {
                throw new EntityNotFoundException("Team role does not exist.");
            }
        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }
    }


    @Override
    public void deleteTeamRole(UUID userId, UUID teamRoleId) {
        User adminUser = userService.getUserById(userId);
        boolean hasAdminRole = adminUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Organization_Admin));
        if(hasAdminRole){
            try{
                getTeamRoleById(teamRoleId);
                teamRoleRepository.deleteById(teamRoleId);
            } catch (EntityNotFoundException ex) {
                throw new EntityNotFoundException("Team role does not exist.");
            }
        } else {
            throw new AccessDeniedException("Unauthorized access!");
        }

    }
}
