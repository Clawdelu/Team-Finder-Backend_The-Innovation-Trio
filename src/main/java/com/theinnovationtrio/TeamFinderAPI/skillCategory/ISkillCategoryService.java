package com.theinnovationtrio.TeamFinderAPI.skillCategory;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface ISkillCategoryService {
    SkillCategory createSkillCategory(Principal connectedUser, SkillCategoryDto skillCategoryDto);
    SkillCategory getSkillCategoryById(UUID skillCategoryId);
    List<SkillCategory> getAllSkillCategories();
    List<SkillCategory> getAllSameOrgSkillCategories(Principal connectedUser);
    SkillCategory updateSkillCategory(Principal connectedUser, UUID skillCategoryId, SkillCategoryDto skillCategoryDto);
    void deleteSkillCategoryById(Principal connectedUser, UUID skillCategoryId);
}
