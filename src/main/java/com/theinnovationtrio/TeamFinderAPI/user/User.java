package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.entities.SkillAssignment;
import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@Document
public class User {
    @Id
    private UUID id;
    private UUID organizationId;
    private UUID departmentId;
    private String userName;
    private String email;
    private String password;
    private List<Role> roles;
    private List<SkillAssignment> skillAssignment;
    private int availableHours;

}
