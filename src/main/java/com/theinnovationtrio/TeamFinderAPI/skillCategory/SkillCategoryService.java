package com.theinnovationtrio.TeamFinderAPI.skillCategory;

import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import com.theinnovationtrio.TeamFinderAPI.user.IUserService;
import com.theinnovationtrio.TeamFinderAPI.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillCategoryService implements ISkillCategoryService{

    private final SkillCategoryRepository skillCategoryRepository;
    private final IUserService userService;

    @Override
    public SkillCategory createSkillCategory(Principal connectedUser, SkillCategoryDto skillCategoryDto) {

        User departmentManagerUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        boolean hasDepartmentManagerRole = departmentManagerUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Department_Manager));

        if(hasDepartmentManagerRole){
            SkillCategory skillCategory = SkillCategory
                    .builder()
                    .id(UUID.randomUUID())
                    .skillCategoryName(skillCategoryDto.getSkillCategoryName())
                    .createdBy(departmentManagerUser.getId())
                    .build();
            return skillCategoryRepository.save(skillCategory);
        } else {
            throw new AccessDeniedException("Unauthorized access! Don't have the department manager role.");
        }
    }

    @Override
    public SkillCategory getSkillCategoryById(UUID skillCategoryId) {

        return skillCategoryRepository.findById(skillCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Skill category not found!"));
    }

    @Override
    public List<SkillCategory> getAllSkillCategories() {

        return skillCategoryRepository.findAll();
    }

    @Override
    public List<SkillCategory> getAllSameOrgSkillCategories(Principal connectedUser) {

        User departmentManagerUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        List<SkillCategory> skillCategories = getAllSkillCategories();
        boolean hasDepartmentManagerRole = departmentManagerUser.getRoles().stream()
                .anyMatch(role -> role.equals(Role.Department_Manager));

        if(hasDepartmentManagerRole){
            return skillCategories.stream()
                    .filter(skillCategory -> userService.getUserById(skillCategory.getCreatedBy()).getOrganizationId()
                            .equals(departmentManagerUser.getOrganizationId()))
                    .collect(Collectors.toList());

        } else {
            throw new AccessDeniedException("Unauthorized access! Don't have the department manager role.");
        }
    }

    @Override
    public SkillCategory updateSkillCategory(Principal connectedUser, UUID skillCategoryId, SkillCategoryDto skillCategoryDto) {

        User departmentManagerUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        SkillCategory skillCategoryToUpdate = getSkillCategoryById(skillCategoryId);

        boolean createdTheSkill = departmentManagerUser.getId()
                .equals(skillCategoryToUpdate.getCreatedBy());

        if(createdTheSkill){
            skillCategoryToUpdate.setSkillCategoryName(skillCategoryDto.getSkillCategoryName());

            return skillCategoryRepository.save(skillCategoryToUpdate);

        } else {
            throw new AccessDeniedException("Unauthorized access! You aren't the author.");
        }
    }

    @Override
    public void deleteSkillCategoryById(Principal connectedUser, UUID skillCategoryId) {

        User departmentManagerUser = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        SkillCategory skillCategoryToDelete = getSkillCategoryById(skillCategoryId);

        boolean createdTheSkill = departmentManagerUser.getId()
                .equals(skillCategoryToDelete.getCreatedBy());

        if(createdTheSkill){
            // TO DO: tb sa verifici fiecare Skill si sa scoti legatura cu acest SkillCategoryID
            skillCategoryRepository.deleteById(skillCategoryId);

        } else {
            throw new AccessDeniedException("Unauthorized access! You aren't the author.");
        }
    }
}
