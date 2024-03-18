package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findAllByOrganizationId(UUID organizationId);

    @Query("select u from User u " +
            "where u.organizationId = :organizationId " +
            "and u.department is null")
    List<User> findAllUnassignedEmp(@Param("organizationId") UUID organizationId);

}
