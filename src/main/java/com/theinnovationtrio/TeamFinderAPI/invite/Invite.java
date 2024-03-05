package com.theinnovationtrio.TeamFinderAPI.invite;

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
public class Invite {
    @Id
    private UUID id;
    private UUID organizationId;
    private UUID createdBy;
    private boolean available;

}
