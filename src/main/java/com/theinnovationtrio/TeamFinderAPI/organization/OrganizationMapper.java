package com.theinnovationtrio.TeamFinderAPI.organization;

import com.theinnovationtrio.TeamFinderAPI.auth.SignUpDto;
import com.theinnovationtrio.TeamFinderAPI.user.User;
import com.theinnovationtrio.TeamFinderAPI.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

    Organization mappToOrganization(OrganizationDto organizationDto);
}
