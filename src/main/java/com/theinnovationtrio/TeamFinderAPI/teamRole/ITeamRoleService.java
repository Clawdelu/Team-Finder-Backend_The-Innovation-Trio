package com.theinnovationtrio.TeamFinderAPI.teamRole;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface ITeamRoleService {
    TeamRole createTeamRole(Principal connectedUser, TeamRoleDto teamRoleDto);
    List<TeamRole> getAllTeamRoles();
    List<TeamRole> getAllSameOrgTeamRoles(Principal connectedUser);
    TeamRole getTeamRoleById(UUID teamRoleId);
    TeamRole updateTeamRole(Principal connectedUser, UUID teamRoleId, TeamRoleDto teamRoleDto);
    void deleteTeamRole(Principal connectedUser,UUID teamRoleId);

}
