package com.theinnovationtrio.TeamFinderAPI.auth1;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpDto {
    private String name;
    private String email;
    private String password;
}
