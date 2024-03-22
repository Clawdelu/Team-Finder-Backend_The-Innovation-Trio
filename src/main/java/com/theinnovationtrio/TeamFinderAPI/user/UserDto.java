package com.theinnovationtrio.TeamFinderAPI.user;
import com.theinnovationtrio.TeamFinderAPI.department.Department;
import com.theinnovationtrio.TeamFinderAPI.user_skill.User_Skill;
import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private UUID id;

    private Department department;

    private String userName;

    private String email;

    private List<Role> roles;

    private List<User_Skill> skills;

    private int availableHours;

    private UUID organizationId;
}
