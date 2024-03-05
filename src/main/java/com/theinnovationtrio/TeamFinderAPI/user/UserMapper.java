package com.theinnovationtrio.TeamFinderAPI.user;

import com.theinnovationtrio.TeamFinderAPI.auth.AdminSignUpDto;
import com.theinnovationtrio.TeamFinderAPI.auth.SignUpDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userDtotoUser(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    @Mapping(source = "name", target = "userName")
    User signUpDtotoUser(SignUpDto signUpDto);

    default User signUpDtotoUser(SignUpDto signUpDto, String password) {
        User user = signUpDtotoUser(signUpDto);
        user.setPassword(password);
        return user;
    }
    @Mapping(target = "password", ignore = true)
    @Mapping(source = "name", target = "userName")
    User adminSignUpDtotoUser(AdminSignUpDto adminSignUpDto);
    default User adminSignUpDtotoUser(AdminSignUpDto adminSignUpDto, String password){
        User user = adminSignUpDtotoUser(adminSignUpDto);
        user.setPassword(password);
        return user;
    }


}
