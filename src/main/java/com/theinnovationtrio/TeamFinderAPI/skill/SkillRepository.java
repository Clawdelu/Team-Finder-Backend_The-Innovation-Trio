package com.theinnovationtrio.TeamFinderAPI.skill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill, UUID> {
    @Query("select s from Skill s " +
            "join User u on s.createdBy = u.id " +
            "where u.organizationId = (select u1.organizationId from User u1 where u1.id = :userId)")
    List<Skill> findAllSameOrgById(UUID userId);

    @Query("select s from Skill s " +
            "join User u on s.createdBy = u.id " +
            "where u.department.id = :departmentId")
    List<Skill> findAllBySameDepartment(UUID departmentId);
}
