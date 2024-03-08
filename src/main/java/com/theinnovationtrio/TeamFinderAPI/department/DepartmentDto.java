package com.theinnovationtrio.TeamFinderAPI.department;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {
    private String departmentName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID departmentManager;
}
