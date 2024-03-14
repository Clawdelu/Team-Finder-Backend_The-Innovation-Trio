package com.theinnovationtrio.TeamFinderAPI.skillCategory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SkillCategoryRepository extends JpaRepository<SkillCategory, UUID> {
}
