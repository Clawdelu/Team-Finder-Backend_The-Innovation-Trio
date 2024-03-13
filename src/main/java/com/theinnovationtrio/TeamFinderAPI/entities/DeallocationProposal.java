package com.theinnovationtrio.TeamFinderAPI.entities;

import com.theinnovationtrio.TeamFinderAPI.enums.ProposalStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DeallocationProposal {
    @Id
    private UUID id;
    private String deallocationReason;
    private UUID userId;
    private UUID projectId;
    private ProposalStatus status;

}
