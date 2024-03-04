package com.theinnovationtrio.TeamFinderAPI.organization;

import java.util.List;
import java.util.UUID;

public interface IOrganizationService {
    Organization createOrganization(OrganizationDto organizationDto);
    List<Organization> getAllOrganizations();
    Organization getOrganizationById(UUID organizationId);
    Organization updateOrganization(UUID organizationId, OrganizationDto organizationDto);
    void deleteOrganizationById(UUID organizationId);
}
