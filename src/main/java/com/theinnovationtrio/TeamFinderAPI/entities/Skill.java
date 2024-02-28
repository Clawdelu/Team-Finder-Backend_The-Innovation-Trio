package com.theinnovationtrio.TeamFinderAPI.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@Document
public class Skill {
    @Id
    private UUID id;
    private UUID createdBy;
    private SkillCategory skillCategory;
    private String description;
    private List<String> departments;
}
