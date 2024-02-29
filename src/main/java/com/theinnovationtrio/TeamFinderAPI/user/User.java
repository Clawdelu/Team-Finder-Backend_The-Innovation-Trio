package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.department.Department;
import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import com.theinnovationtrio.TeamFinderAPI.organization.Organization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    private UUID id;
//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @Column(nullable = false)
//    private Organization organization;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Department department;
    @Column(nullable = false)
    private String userName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private List<Role> roles;
    // private List<User_Skill> skillAssignment;
    private int availableHours;

}
