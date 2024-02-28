package com.theinnovationtrio.TeamFinderAPI.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor
@Entity
public class Skill {
    @Id
    private UUID id;
    private UUID createdBy;
    @OneToOne
    private SkillCategory skillCategory;
    private String description;
    //private List<String> departments;
}
