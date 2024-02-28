package com.theinnovationtrio.TeamFinderAPI.entities;

import com.theinnovationtrio.TeamFinderAPI.enums.Experience;
import com.theinnovationtrio.TeamFinderAPI.enums.Level;
import lombok.Data;

@Data
public class SkillAssignment {
    private Skill skill;
    private Level level;
    private Experience experience;
}
