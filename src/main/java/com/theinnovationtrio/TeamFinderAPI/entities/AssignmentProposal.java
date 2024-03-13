package com.theinnovationtrio.TeamFinderAPI.entities;

import com.theinnovationtrio.TeamFinderAPI.enums.ProposalStatus;
import com.theinnovationtrio.TeamFinderAPI.teamRole.TeamRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AssignmentProposal {
    @Id
    private UUID id;
    private int workHours;
    @ManyToMany
    private List<TeamRole> teamRoles;
    private String comments;
    private UUID userId;
    private UUID projectId;
    private ProposalStatus status;


}
