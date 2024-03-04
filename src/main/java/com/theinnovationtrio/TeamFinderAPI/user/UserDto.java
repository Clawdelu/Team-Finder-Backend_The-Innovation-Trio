package com.theinnovationtrio.TeamFinderAPI.user;
import com.theinnovationtrio.TeamFinderAPI.organization.Organization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID organizationId;
    private String userName;
    private String email;
    private String password;
}
