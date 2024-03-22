package com.theinnovationtrio.TeamFinderAPI.project;

import com.theinnovationtrio.TeamFinderAPI.entities.Project_TeamRole;
import com.theinnovationtrio.TeamFinderAPI.entities.TechnologyStack;
import com.theinnovationtrio.TeamFinderAPI.enums.ProjectPeriod;
import com.theinnovationtrio.TeamFinderAPI.enums.ProjectStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private String projectName;

    private ProjectPeriod projectPeriod;

    private LocalDate startDate;

    private LocalDate deadlineDate;

    private ProjectStatus projectStatus;

    private String generalDescription;

    private List<TechnologyStack> technologyStack;

    private List<Project_TeamRole> teamRoles;
}
