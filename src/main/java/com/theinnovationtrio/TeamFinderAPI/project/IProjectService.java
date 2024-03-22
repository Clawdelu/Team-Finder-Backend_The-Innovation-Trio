package com.theinnovationtrio.TeamFinderAPI.project;

import java.util.List;
import java.util.UUID;

public interface IProjectService {

    Project createProject(ProjectDto projectDto);
    Project getProjectById(UUID projectId);
    List<Project> getAllProjects();
    List<Project> getAllSameOrgProjects();
    Project updateProject(UUID projectId, ProjectDto projectDto);
    void deleteProjectById(UUID projectId);
}
