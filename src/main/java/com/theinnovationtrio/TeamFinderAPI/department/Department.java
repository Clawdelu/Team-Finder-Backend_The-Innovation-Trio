package com.theinnovationtrio.TeamFinderAPI.department;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theinnovationtrio.TeamFinderAPI.entities.Skill;
import com.theinnovationtrio.TeamFinderAPI.user.User;
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
public class Department {
    @Id
    private UUID id;
    private String departmentName;
    private UUID createdBy;
    private UUID departmentManager;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<User> users;
}
