package com.theinnovationtrio.TeamFinderAPI.organization;

import com.theinnovationtrio.TeamFinderAPI.user.User;

import java.util.List;
import java.util.UUID;

public interface IOrganizationService {
    Organization createOrganization(OrganizationDto organizationDto, User user);
    List<Organization> getAllOrganizations();
    Organization getOrganizationById(UUID organizationId);
    Organization updateOrganization(UUID organizationId, OrganizationDto organizationDto);
    void deleteOrganizationById(UUID organizationId);
}
