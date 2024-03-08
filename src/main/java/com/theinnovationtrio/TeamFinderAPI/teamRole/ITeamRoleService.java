package com.theinnovationtrio.TeamFinderAPI.teamRole;

import java.util.List;
import java.util.UUID;

public interface ITeamRoleService {
    TeamRole createTeamRole(UUID userId, TeamRoleDto teamRoleDto);
    List<TeamRole> getAllTeamRoles();
    TeamRole getTeamRoleById(UUID teamRoleId);
    TeamRole updateTeamRole(UUID userId, UUID teamRoleId, TeamRoleDto teamRoleDto);
    void deleteTeamRole(UUID userId,UUID teamRoleId);

}
