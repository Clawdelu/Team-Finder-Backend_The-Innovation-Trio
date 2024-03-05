package com.theinnovationtrio.TeamFinderAPI.invite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, UUID> {
    boolean existsById(UUID id);
}
