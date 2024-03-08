package com.theinnovationtrio.TeamFinderAPI.teamRole;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamRoleRepository extends JpaRepository<TeamRole, UUID> {
}
