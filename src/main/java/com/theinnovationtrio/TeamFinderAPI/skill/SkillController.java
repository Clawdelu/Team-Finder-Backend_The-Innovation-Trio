package com.theinnovationtrio.TeamFinderAPI.skill;

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
@RequestMapping("/skills")
public class SkillController {

    private final ISkillService skillService;

    @GetMapping()
    public ResponseEntity<List<Skill>> getAllSkills() {
        List<Skill> skills = skillService.getAllSkills();
        if (skills.isEmpty()) {
            return ResponseEntity.noContent()
                    .header("Message", "There is no skill.").build();

        } else {
            return ResponseEntity.ok(skills);
        }
    }

    @GetMapping("{skillId}")
    public ResponseEntity<?> getSkillById(@PathVariable UUID skillId) {

        try {
            Skill skill = skillService.getSkillById(skillId);
            return ResponseEntity.ok(skill);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        }
    }

    @GetMapping("/same-organization")
    public ResponseEntity<?> getAllSkillsFromSameOrganization(Principal connectedUser) {

        try {
            List<Skill> skills = skillService.getAllSameOrgSkills(connectedUser);
            if (skills.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(skills);
            }
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createSkill(@RequestBody SkillDto skillDto, Principal connectedUser) {
        try {
            Skill skill = skillService.createSkill(connectedUser, skillDto);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(skill.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());

        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @PutMapping("{skillId}")
    public ResponseEntity<?> updateSkill(@PathVariable UUID skillId, @RequestBody SkillDto skillDto,
                                         Principal connectedUser) {

        try {
            Skill skill = skillService.updateSkill(connectedUser, skillId, skillDto);
            return ResponseEntity.ok(skill);

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());

        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @PatchMapping("{skillId}/add-skill-to-department")
    public ResponseEntity<?> addSkillToDepartment(@PathVariable UUID skillId, Principal connectedUser) {

        try {
            skillService.assignSkillToDepartment(connectedUser, skillId);
            return ResponseEntity.ok("Skill have been added successfully to department!");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @PatchMapping("{skillId}/remove-skill-from-department")
    public ResponseEntity<?> removeSkillFromDepartment(@PathVariable UUID skillId, Principal connectedUser) {

        try {
            skillService.removeSkillFromDepartment(connectedUser, skillId);
            return ResponseEntity.ok("Skill have been removed successfully from department!");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

    @DeleteMapping("{skillId}")
    public ResponseEntity<?> deleteSkill(@PathVariable UUID skillId, Principal connectedUser) {

        try {
            skillService.deleteSkillById(connectedUser, skillId);
            return ResponseEntity.ok().build();

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message: " + ex.getMessage());
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Message: " + ex.getMessage());
        }
    }

}
