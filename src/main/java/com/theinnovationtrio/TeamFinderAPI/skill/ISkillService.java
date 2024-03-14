package com.theinnovationtrio.TeamFinderAPI.skill;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface ISkillService {

    Skill createSkill(Principal connectedUser, SkillDto skillDto);
    Skill getSkillById(UUID skillId);
    List<Skill> getAllSkills();
    List<Skill> getAllSameOrgSkills(Principal connectedUser);
    void assignSkillToDepartment(Principal connectedUser, UUID skillId);
    void removeSkillFromDepartment(Principal connectedUser, UUID skillId);
    Skill updateSkill(Principal connectedUser, UUID skillId, SkillDto skillDto);
    void deleteSkillById(Principal connectedUser, UUID skillId);
}
