package com.theinnovationtrio.TeamFinderAPI.skill;

import com.theinnovationtrio.TeamFinderAPI.skillCategory.SkillCategory;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillDto {

    private SkillCategory skillCategory;

    private String skillName;

    private String description;

    private boolean addedToDepartment;
}
