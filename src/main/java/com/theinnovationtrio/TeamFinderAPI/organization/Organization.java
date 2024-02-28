package com.theinnovationtrio.TeamFinderAPI.organization;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document
public class Organization {
    private UUID id;
    private UUID createdBy;
    private String organizationName;
    private String headquarterAddress;
}
