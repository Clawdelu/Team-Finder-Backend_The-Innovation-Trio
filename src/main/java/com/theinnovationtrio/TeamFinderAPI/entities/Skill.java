package com.theinnovationtrio.TeamFinderAPI.entities;

import com.theinnovationtrio.TeamFinderAPI.department.Department;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor
@Entity
public class Skill {
    @Id
    private UUID id;
    private UUID createdBy;
    @ManyToOne
    private SkillCategory skillCategory;
    private String description;
    @OneToMany
    private List<Department> departments;
}
