package com.theinnovationtrio.TeamFinderAPI.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;


import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SkillCategory {
    @Id
    private UUID id;
    private UUID createdBy;
    private String skillCategoryName;
}
