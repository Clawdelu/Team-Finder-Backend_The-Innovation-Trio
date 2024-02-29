package com.theinnovationtrio.TeamFinderAPI.entities;

import com.theinnovationtrio.TeamFinderAPI.enums.StatusOfMember;
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
public class User_Project_Role {
    @Id
    private UUID id;
    private UUID userId;
    private UUID projectId;
    //private List<TeamRole> teamRoles;
    private StatusOfMember statusOfMember;
}
