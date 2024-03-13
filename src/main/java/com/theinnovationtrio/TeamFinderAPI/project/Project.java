package com.theinnovationtrio.TeamFinderAPI.project;

import com.theinnovationtrio.TeamFinderAPI.entities.Project_TeamRole;
import com.theinnovationtrio.TeamFinderAPI.entities.TechnologyStack;
import com.theinnovationtrio.TeamFinderAPI.enums.ProjectPeriod;
import com.theinnovationtrio.TeamFinderAPI.enums.ProjectStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Project {
    @Id
    private UUID id;
    private String projectName;
    private ProjectPeriod projectPeriod;
    private LocalDate startDate;
    @Column(nullable = true)
    private LocalDate deadlineDate;
    private ProjectStatus projectStatus;
    private String generalDescription;
    @OneToMany
    private List<TechnologyStack> technologyStack;
    @OneToMany
    private List<Project_TeamRole> teamRoles;
    private UUID createdBy;




}
