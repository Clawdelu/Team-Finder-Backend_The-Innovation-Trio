package com.theinnovationtrio.TeamFinderAPI.skillCategory;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/skills-category")
public class SkillCategoryController {

    private final ISkillCategoryService skillCategoryService;

    @GetMapping()
    public ResponseEntity<List<SkillCategory>> getAllSkillCategories() {

        List<SkillCategory> skillCategories = skillCategoryService.getAllSkillCategories();
        if (skillCategories.isEmpty()) {
            return ResponseEntity.noContent()
                    .header("Message", "There is no skill category.").build();

        } else {
            return ResponseEntity.ok(skillCategories);
        }
    }

    @GetMapping("/{skillCategoryId}")
    public ResponseEntity<?> getSkillCategoryById(@PathVariable UUID skillCategoryId) {

        try {
            SkillCategory skillCategory = skillCategoryService.getSkillCategoryById(skillCategoryId);
            return ResponseEntity.ok(skillCategory);

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("/same-organization")
    public ResponseEntity<?> getAllSkillCategoriesFromSameOrganization(Principal connectedUser){

        try{
            List<SkillCategory> skillCategories = skillCategoryService.getAllSameOrgSkillCategories(connectedUser);
            if(skillCategories.isEmpty()){
                return ResponseEntity.noContent().build();

            } else{
                return ResponseEntity.ok(skillCategories);
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<?> createSkillCategory(@RequestBody SkillCategoryDto skillCategoryDto, Principal connectedUser) {

        try {
            SkillCategory skillCategory = skillCategoryService.createSkillCategory(connectedUser, skillCategoryDto);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(skillCategory.getId())
                    .toUri();
            return ResponseEntity.created(location).build();

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());

        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @PutMapping("{skillCategoryId}")
    public ResponseEntity<?> updateSkillCategory(@PathVariable UUID skillCategoryId, @RequestBody SkillCategoryDto skillCategoryDto,
                                                 Principal connectedUser) {

        try {
            SkillCategory updatedSkillCategory = skillCategoryService.updateSkillCategory(connectedUser, skillCategoryId, skillCategoryDto);
            return ResponseEntity.ok(updatedSkillCategory);

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());

        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @DeleteMapping("{skillCategoryId}")
    public ResponseEntity<?> deleteSkillCategoryById(@PathVariable UUID skillCategoryId, Principal connectedUser) {

        try {
            skillCategoryService.deleteSkillCategoryById(connectedUser, skillCategoryId);
            return ResponseEntity.ok().build();

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

}
