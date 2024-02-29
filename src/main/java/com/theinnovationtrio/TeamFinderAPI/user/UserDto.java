package com.theinnovationtrio.TeamFinderAPI.user;
import com.theinnovationtrio.TeamFinderAPI.organization.Organization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Organization organization;
    private String userName;
    private String email;
    private String password;
}
