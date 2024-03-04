package com.theinnovationtrio.TeamFinderAPI.entities;

import com.theinnovationtrio.TeamFinderAPI.enums.Experience;
import com.theinnovationtrio.TeamFinderAPI.enums.Level;
import com.theinnovationtrio.TeamFinderAPI.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User_Skill {
    @Id
    private UUID id;
    @ManyToOne(cascade = CascadeType.ALL)
    private Skill skill;
    private Level level;
    private Experience experience;

}
