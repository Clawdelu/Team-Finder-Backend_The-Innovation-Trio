package com.theinnovationtrio.TeamFinderAPI.skill;

import java.util.List;
import java.util.UUID;

public interface ISkillService {

    Skill createSkill(SkillDto skillDto);

    Skill getSkillById(UUID skillId);

    List<Skill> getAllSkills();

    List<Skill> getAllSameOrgSkills();

    List<Skill> getAllSameDepartmentSkills();

    void assignSkillToDepartment(UUID skillId);

    void removeSkillFromDepartment(UUID skillId);

    Skill updateSkill(UUID skillId, SkillDto skillDto);

    void deleteSkillById(UUID skillId);
}
