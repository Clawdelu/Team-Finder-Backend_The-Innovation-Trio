package com.theinnovationtrio.TeamFinderAPI.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document
public class SkillCategory {
    private UUID id;
    private UUID createdBy;
    private String skillName;
}
